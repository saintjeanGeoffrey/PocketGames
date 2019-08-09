package be.pocketgames.fragments.addGameToCollection;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.*;
import android.widget.TextView;
import be.pocketgames.R;
import be.pocketgames.adapters.ResultSearchListAdapter;
import be.pocketgames.database.Database;
import be.pocketgames.models.GameDatabase;
import be.pocketgames.utils.AnimationUtils;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddByScanFragment extends Fragment {

    private CallbackFragment mCallbackFragment;

    private SurfaceView mCameraView;
    private TextView mResultTitle;
    private boolean codebarReaded = false;
    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;
    private RecyclerView mResultScanRecyclerView;

    private List<GameDatabase> mResultScanList;

    //***************************************************************************************************
    public AddByScanFragment() {}
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_by_scan, container, false);
        initValues(v);

        return v;

    }
    //***************************************************************************************************
    private void initValues(View v) {
        mResultScanList = new ArrayList<>();
        mResultScanRecyclerView = v.findViewById(R.id.rv_addByScan_recyclerView);
        mResultTitle = v.findViewById(R.id.tv_addByScan_resultTitle);
        mCameraView = v.findViewById(R.id.sv_addByScan_surfaceView);

        mBarcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.EAN_13)
                .build();
        mCameraSource = new CameraSource.Builder(getContext(), mBarcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();
        //***************************************************************************************************
        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            //-----------------------------------------------------------------------------------------------
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 101);
                }
                try {
                    mCameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //-----------------------------------------------------------------------------------------------
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
            //-----------------------------------------------------------------------------------------------
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
            //-----------------------------------------------------------------------------------------------
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            //-----------------------------------------------------------------------------------------------
            @Override
            public void release() { }
            //-----------------------------------------------------------------------------------------------
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcode = detections.getDetectedItems();
                if((barcode.size() != 0) && (!codebarReaded)) {
                    codebarReaded = true;
                    mResultScanRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(500);
                            String barcodeDetected = barcode.valueAt(0).displayValue;

                            if (Database.EanExist(barcodeDetected , mCallbackFragment.getDatasnapshot())) {

//                              GameDatabase gameScanned = Database.getGameByEan(barcodeDetected, mCallbackFragment.getDatasnapshot());
                                mResultScanList = Database.getGamesByEan(barcodeDetected, mCallbackFragment.getDatasnapshot());

                                AnimationUtils.slideView(getView().findViewById(R.id.rl_addByScan_scanSurface_container), mCameraView.getHeight(), 0, 700);
                                initResultList();

                            }
                            else {
                                // setup the alert builder
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("No game associated to this barcode");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        codebarReaded = false;
                                    }
                                });
                                builder.setNeutralButton("Link to a game ?", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("barcode", barcodeDetected);
                                        AssociateBarcodeFragment f = new AssociateBarcodeFragment();
                                        f.setArguments(bundle);
                                        mCallbackFragment.showFragment(f);
                                    }
                                });
                                // create and show the alert dialog
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            } //end receiveDetection
            //-----------------------------------------------------------------------------------------------
        });
    }
    //***************************************************************************************************
    private void initResultList() {
        //on donne à l'adapter le resultat à afficher dans le recycler
        ResultSearchListAdapter resultSearchListAdapter = new ResultSearchListAdapter(getContext(), mResultScanList, getFragmentManager());
        //On ajuste les settings du layout pour le recycler
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        //on applique les settings layout au recycler
        mResultScanRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        //on ajoute les données dapatées dans le recycler
        mResultScanRecyclerView.setAdapter(resultSearchListAdapter);
    }
    //***************************************************************************************************
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallbackFragment) {
            mCallbackFragment = (CallbackFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CallbackFragment");
        }
    }
    //***************************************************************************************************
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackFragment = null;
    }
    //***************************************************************************************************
    public interface CallbackFragment {
        DataSnapshot getDatasnapshot();
        void showFragment(Fragment fragment);
    }
    //***************************************************************************************************
}
