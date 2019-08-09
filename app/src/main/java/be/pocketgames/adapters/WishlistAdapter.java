package be.pocketgames.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import be.pocketgames.AddGameActivity;
import be.pocketgames.R;
import be.pocketgames.database.DAO.GameWishlistCRUD;
import be.pocketgames.models.GameWishlist;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter  extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{

    private static final String TAG = "ResultSearchListAdapter";

    private List<GameWishlist> mUserGamesWishlist;
    private Context mContext;
    private FragmentManager mFragmentManager;
    //***************************************************************************************************
    public WishlistAdapter(Context mContext, List<GameWishlist> mUserGamesWishlist, FragmentManager fragmentManager) {
        this.mUserGamesWishlist = mUserGamesWishlist;
        this.mContext = mContext;
        this.mFragmentManager = fragmentManager;
    }
    //***************************************************************************************************
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_wishlist_item, viewGroup, false);
        return new ViewHolder(v);
    }
    //***************************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);
        Glide.with(mContext)
//                .load(m.get(i))
                .load(mUserGamesWishlist.get(i).getmCover())
                .apply(requestOptions)
                .into(viewHolder.mImage);

        //transfert de la wishlist vers la collection
        ImageButton moveBtn = viewHolder.itemView.findViewById(R.id.btn_layoutResultSearch_moveto);
        //-----------------------------------------------------------------------------------------------
        moveBtn.setOnClickListener(v -> {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Move to collection ?");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(mContext, AddGameActivity.class);
                    // dire que l'object vient de la wishlist
                    intent.putExtra("from", "wishlist");
                    // put l'object gameDatabase a mettre dans la collection depuis la wishlist
                    intent.putExtra("gameToAddFromWishlist", mUserGamesWishlist.get(i));
                    mContext.startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // finish();
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }); // end moveBtn
        //-----------------------------------------------------------------------------------------------
        viewHolder.mTitle.setText(mUserGamesWishlist.get(i).getmTitle());
        //-----------------------------------------------------------------------------------------------
        viewHolder.itemView.setOnLongClickListener(v -> {

            ImageView delete = viewHolder.itemView.findViewById(R.id.iv_wishlist_delete_icon);
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(item -> {

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure to delete ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GameWishlistCRUD db = new GameWishlistCRUD();
                        db.delete(mUserGamesWishlist.get(i));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        viewHolder.itemView.findViewById(R.id.iv_wishlist_delete_icon).setVisibility(View.GONE);
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
            if(viewHolder.itemView.findViewById(R.id.iv_wishlist_delete_icon).getVisibility() == View.VISIBLE) {
                viewHolder.itemView.findViewById(R.id.iv_wishlist_delete_icon).setVisibility(View.GONE);
            }
        });
        //-----------------------------------------------------------------------------------------------
    } // end on bind listener
    //***************************************************************************************************
    @Override
    public int getItemCount() {
        return mUserGamesWishlist.size();
    }
    //***************************************************************************************************
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.iv_layoutResultSearch_image);
            mTitle = itemView.findViewById(R.id.tv_layoutResultSearch_title);
        }
    } // end ViewHolder
    //***************************************************************************************************
} //end CollectionAdapter
