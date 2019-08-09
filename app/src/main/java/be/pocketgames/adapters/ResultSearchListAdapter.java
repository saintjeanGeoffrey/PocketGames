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
import android.widget.*;
import be.pocketgames.R;
import be.pocketgames.fragments.addGameToCollection.AddGameToCollectionFormFragment;
import be.pocketgames.models.GameDatabase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ResultSearchListAdapter extends RecyclerView.Adapter<ResultSearchListAdapter.ViewHolder>{

    private static final String TAG = "ResultSearchListAdapter";

    private List<GameDatabase> mResultSearchGames;
    private Context mContext;
    private FragmentManager mFragmentManager;


    public ResultSearchListAdapter(Context mContext, List<GameDatabase> mUserGamesCollection, FragmentManager fragmentManager) {
        this.mResultSearchGames = mUserGamesCollection;
        this.mContext = mContext;
        this.mFragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_result_search_item, viewGroup, false);
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

        ImageButton addBtn = viewHolder.itemView.findViewById(R.id.btn_layoutResultSearch_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Add to :");

                // add a list
                String[] list = {"Collection", "Wishlist"};
                builder.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        FragmentTransaction transaction;
                        switch (which) {
                            case 0: // collection
                                    bundle.putString("from", "collection");
                                break;
                            case 1: // wishlist
                                bundle.putString("from", "wishlist");
                                break;
                        }
                        bundle.putParcelable("gameToAdd",mResultSearchGames.get(i));
                        transaction = mFragmentManager.beginTransaction();
                        AddGameToCollectionFormFragment fragment = new AddGameToCollectionFormFragment();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.LL_addGameToCollectionActivity_fragmentView,fragment);
//                      transaction.commit();
                        transaction.commitAllowingStateLoss();
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
            mImage = itemView.findViewById(R.id.iv_layoutResultSearch_image);
            mTitle = itemView.findViewById(R.id.tv_layoutResultSearch_title);
        }
    } // end ViewHolder
} //end CollectionAdapter
