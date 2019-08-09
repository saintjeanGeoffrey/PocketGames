package be.pocketgames.fragments.addGameToCollection;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import be.pocketgames.R;
import be.pocketgames.adapters.ResultSearchListBarcodeAdapter;
import be.pocketgames.models.GameDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AssociateBarcodeFragment extends Fragment {

    private CallbackFragment mCallbackFragment;

    private EditText mUserSearch;
    private RecyclerView mResultRecyclerView;
    private List<GameDatabase> mGamesResult;
    private DataSnapshot mDataSnapshot;

    //***************************************************************************************************
    public AssociateBarcodeFragment() { }
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_associate_barcode, container, false);
        initValues(v);
        return v;
    }
    //***************************************************************************************************
    private void initValues(View v) {
        mResultRecyclerView = v.findViewById(R.id.rv_associateBarcodeFragment_recyclerView);
        mGamesResult = new ArrayList<>();
        mUserSearch = v.findViewById(R.id.et_associateBarcodeFragment_search);
        mUserSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDataSnapshot = mCallbackFragment.getSnapshot();
                mGamesResult.removeAll(mGamesResult);
                if (mDataSnapshot != null) {
                    for ( DataSnapshot data_game : mDataSnapshot.child("games").getChildren()) {
                        if ( data_game.child("mTitle").getValue().toString().toLowerCase().startsWith(mUserSearch.getText().toString().toLowerCase()) ) {
                            mGamesResult.add(data_game.getValue(GameDatabase.class));
                        }
                    }
                    initResultList();
                }
                else {
                    Log.d("GAMES", "snapshot null");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
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
    private void initResultList() {
        String barcode = getArguments().getString("barcode");
        //on donne à l'adapter le resultat à afficher dans le recycler
        ResultSearchListBarcodeAdapter resultSearchListAdapter = new ResultSearchListBarcodeAdapter(getContext(), mGamesResult, getFragmentManager(), barcode, mCallbackFragment.getSnapshot());
        //On ajuste les settings du layout pour le recycler
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        //on applique les settings layout au recycler
        mResultRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        //on ajoute les données dapatées dans le recycler
        mResultRecyclerView.setAdapter(resultSearchListAdapter);
    }
    //***************************************************************************************************
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackFragment = null;
    }
    //***************************************************************************************************
    public interface CallbackFragment {
        DataSnapshot getSnapshot();
        void showFragment(Fragment fragment);
    }
    //***************************************************************************************************
} // end class
