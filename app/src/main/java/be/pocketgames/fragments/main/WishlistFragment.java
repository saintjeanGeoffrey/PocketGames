package be.pocketgames.fragments.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.pocketgames.R;
import be.pocketgames.adapters.WishlistAdapter;
import be.pocketgames.asyncTask.GetWishlistGameAsyncTask;
import be.pocketgames.models.GameWishlist;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WishlistFragment extends Fragment {

    private CallbackFragment mCallbackFragment;
    private List<GameWishlist> mWishlist_games;

    //***************************************************************************************************
    //constructor
    public WishlistFragment() {}
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wishlist, container, false);
        initActionbar();
        initValues(v);
        initWishlist();
        showWishlist(v, 1);
        return v;
    }

    private void initActionbar() {
        mCallbackFragment.setCustomActionBar(R.layout.custom_actionbar_wishlist);
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
    private void initValues(View v) {
        mWishlist_games = new ArrayList<>();
    }
    //***************************************************************************************************
    private void initWishlist() {
        GetWishlistGameAsyncTask asyncTask = new GetWishlistGameAsyncTask();
        try {
            mWishlist_games = asyncTask.execute( mCallbackFragment.getSnapshot() ).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //***************************************************************************************************
    private void showWishlist(View v, int nb_column) {
        RecyclerView recyclerView = v.findViewById(R.id.rv_wishlistFragment_recyclerView);
        WishlistAdapter collectionListAdapter = new WishlistAdapter(getContext(), mWishlist_games, getFragmentManager());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(nb_column, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(collectionListAdapter);
    }
    //***************************************************************************************************
    //interface de callback
    public interface CallbackFragment {
        DataSnapshot getSnapshot();
        void setCustomActionBar(int layout);
    }
    //***************************************************************************************************
} // end class

