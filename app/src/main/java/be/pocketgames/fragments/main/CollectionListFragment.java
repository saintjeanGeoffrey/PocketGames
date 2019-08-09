package be.pocketgames.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageButton;
import be.pocketgames.R;
import be.pocketgames.UserActivity;
import be.pocketgames.adapters.CollectionListAdapter;
import be.pocketgames.asyncTask.GetCollectionGameAsyncTask;
import be.pocketgames.database.DAO.GameCollectionCRUD;
import be.pocketgames.database.Database;
import be.pocketgames.models.GameCollection;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CollectionListFragment extends Fragment {

    private static final String TAG = "CollectionListFragment";

    private CallbackFragment mCallbackFragment;
    private List<GameCollection> mCollection_games;
    private EditText mSearch;
    private CircleImageView mUserProfilIcon;
    private ImageButton mSearchBtn;


    //***************************************************************************************************
    //constructor
    public CollectionListFragment() {}
    //***************************************************************************************************
    //interface de callback
    public interface CallbackFragment {
        void setCustomActionBar(int layout);
    }
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collection_list, container, false);
        initActionbar();
        initValues(v);
        initCollection();
        showCollection(v, 2);
        return v;
    }

    private void initActionbar() {
        mCallbackFragment.setCustomActionBar(R.layout.custom_actionbar_main);
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
    private void initValues(View view) {
        GameCollectionCRUD db = new GameCollectionCRUD();
        List<GameCollection> games = db.getAllGameCollection();
        mCollection_games = new ArrayList<>();
        mSearch = view.findViewById(R.id.et_collectionListFragment_search);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCollection_games.removeAll(mCollection_games);

                if ( ! games.isEmpty() ) {
                    for ( GameCollection game : games) {
                        if( ! (mSearch.getText().toString().isEmpty()) ) {
                            if (game.getmTitle().toLowerCase().contains(mSearch.getText().toString().toLowerCase())) {
                                mCollection_games.add(game);
                            }
                        }
                        else {
                            mCollection_games = games;
                        }
                    }
                    showCollection(view, 2);
                }
                else {
                    Log.d("GAMES", "snapshot null");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mUserProfilIcon = getActivity().findViewById(R.id.civ_customActionbar_userProfil);
        mUserProfilIcon.setOnClickListener(v -> {
            startActivity( new Intent(getContext(), UserActivity.class));
        });
        mSearchBtn = getActivity().findViewById(R.id.ibtn_custom_actionbar_search);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchField = view.findViewById(R.id.et_collectionListFragment_search);
                if(searchField.getVisibility() == View.GONE) {
                    mSearchBtn.setColorFilter(Color.RED);
                    searchField.setVisibility(View.VISIBLE);
                }
                else {
                    mSearchBtn.setColorFilter(Color.BLACK);
                    searchField.setVisibility(View.GONE);
                    searchField.setText("");
                }
            }
        });
    }
    //***************************************************************************************************
    private void initCollection() {
        GetCollectionGameAsyncTask asyncTask = new GetCollectionGameAsyncTask();
        try {
            mCollection_games = asyncTask.execute( Database.getInstance().getDataSnapshot() ).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //***************************************************************************************************
    private void showCollection(View v, int nb_column) {
        RecyclerView recyclerView = v.findViewById(R.id.rv_collectionListFragment_recyclerView);
        CollectionListAdapter collectionListAdapter = new CollectionListAdapter(getContext(), mCollection_games, getFragmentManager());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(nb_column, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(collectionListAdapter);
    }
    //***************************************************************************************************
    //***************************************************************************************************
} // end class
