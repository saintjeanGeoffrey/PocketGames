package be.pocketgames.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import be.pocketgames.AddGameActivity;
import be.pocketgames.R;
import be.pocketgames.database.DAO.GameDatabaseCRUD;
import be.pocketgames.models.GameDatabase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class ResultSearchListBarcodeAdapter extends RecyclerView.Adapter<ResultSearchListBarcodeAdapter.ViewHolder>{

    private DataSnapshot mDataSnapshot;
    private String mBarcodeToLink;
    private List<GameDatabase> mResultSearchGames;
    private Context mContext;
    private FragmentManager mFragmentManager;


    public ResultSearchListBarcodeAdapter(Context mContext, List<GameDatabase> mUserGamesCollection, FragmentManager fragmentManager, String barecode, DataSnapshot dataSnapshot) {
        this.mResultSearchGames = mUserGamesCollection;
        this.mContext = mContext;
        this.mFragmentManager = fragmentManager;
        this.mBarcodeToLink = barecode;
        this.mDataSnapshot = dataSnapshot;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_result_search_barcode_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);
        Glide.with(mContext)
                .load(mResultSearchGames.get(i).getmCover())
                .apply(requestOptions)
                .into(viewHolder.mImage);

        viewHolder.mTitle.setText(mResultSearchGames.get(i).getmTitle());

        ImageButton linkBtn = viewHolder.itemView.findViewById(R.id.btn_layoutResultSearch_barcode_link);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Are you sure to link this barcode ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mResultSearchGames.get(i).addEAN(mBarcodeToLink);
                        GameDatabaseCRUD db = new GameDatabaseCRUD();
                        db.update(mResultSearchGames.get(i));

                        // go to addgamecollection
                        Intent intent = new Intent(mContext, AddGameActivity.class);
                        intent.putExtra("from", "barcode");
                        intent.putExtra("gameToAddFromBarcode",mResultSearchGames.get(i));
                        mContext.startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResultSearchGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.iv_layoutResultSearch_barcode_image);
            mTitle = itemView.findViewById(R.id.tv_layoutResultSearch_barcode_title);
        }
    } // end ViewHolder
} //end CollectionAdapter

