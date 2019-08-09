package be.pocketgames.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import be.pocketgames.R;
import be.pocketgames.database.DAO.GameCollectionCRUD;
import be.pocketgames.fragments.main.GameCollectionDetailFragment;
import be.pocketgames.models.GameCollection;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CollectionListAdapter  extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder>{

    private List<GameCollection> mUserGamesCollection;
    private Context mContext;
    private FragmentManager mFragmentManager;
    //***************************************************************************************************
    public CollectionListAdapter(Context mContext, List<GameCollection> mUserGamesCollection, FragmentManager fragmentManager) {
        this.mUserGamesCollection = mUserGamesCollection;
        this.mContext = mContext;
        this.mFragmentManager = fragmentManager;
    }
    //***************************************************************************************************
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_collection_item, viewGroup, false);
        return new ViewHolder(v);
    }
    //***************************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);
        Glide.with(mContext)
//                .load(m.get(i))
                .load(mUserGamesCollection.get(i).getmCover())
                .apply(requestOptions)
                .into(viewHolder.mImage);
        //-----------------------------------------------------------------------------------------------
        viewHolder.itemView.setOnLongClickListener(v -> {

            ImageView delete = viewHolder.itemView.findViewById(R.id.iv_collection_delete_icon);
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(item -> {

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure to delete ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GameCollectionCRUD db = new GameCollectionCRUD();
                        db.delete( mUserGamesCollection.get(i) );
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        viewHolder.itemView.findViewById(R.id.iv_collection_delete_icon).setVisibility(View.GONE);
                        // finish();
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            });
            return true;
        }); // end longClickListener
        //-----------------------------------------------------------------------------------------------
        viewHolder.itemView.setOnClickListener(v -> {
            if(viewHolder.itemView.findViewById(R.id.iv_collection_delete_icon).getVisibility() == View.VISIBLE) {
                viewHolder.itemView.findViewById(R.id.iv_collection_delete_icon).setVisibility(View.GONE);
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("gameToDetail",mUserGamesCollection.get(i));
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                GameCollectionDetailFragment fragment = new GameCollectionDetailFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.LL_mainActivity_fragmentView,fragment);
//                transaction.commit();
                transaction.commitAllowingStateLoss();
            }
        });
        //-----------------------------------------------------------------------------------------------
//      viewHolder.mTitle.setText(mTitles.get(i));
        viewHolder.mTitle.setText(mUserGamesCollection.get(i).getmTitle());
        //-----------------------------------------------------------------------------------------------
    } //end on bind listener
    //***************************************************************************************************
    @Override
    public int getItemCount() {
        return mUserGamesCollection.size();
    }
    //***************************************************************************************************
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.iv_layoutCollectionItem_image);
            mTitle = itemView.findViewById(R.id.tv_layoutCollectionItem_title);
        }
    } // end ViewHolder
    //***************************************************************************************************
} //end CollectionAdapter
