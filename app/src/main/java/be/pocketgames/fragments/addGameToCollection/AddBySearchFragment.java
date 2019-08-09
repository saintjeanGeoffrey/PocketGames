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

import android.widget.Button;
import android.widget.EditText;
import be.pocketgames.R;
import be.pocketgames.adapters.ResultSearchListAdapter;
import be.pocketgames.models.GameDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddBySearchFragment extends Fragment implements  View.OnClickListener {

    private CallbackFragment mCallbackFragment;
    private static int NUM_COLUMNS_R = 1;

    private EditText mUserSearch;
    private Button  mAddGameToCollection;
    private RecyclerView mResultRecyclerView;

    private List<GameDatabase> mGamesResult;
    private DataSnapshot mDataSnapshot;

    //***************************************************************************************************
    public AddBySearchFragment() {}
    //***************************************************************************************************
    public interface CallbackFragment {
        DataSnapshot getSnapshot();
        void showFragment(Fragment fragment);
    }
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_by_search, container, false);
        initValues(v);
        return v;
    }

    //***************************************************************************************************
    private void initValues(View v) {
        mResultRecyclerView = v.findViewById(R.id.rv_addBySearch_recyclerView);
        mGamesResult = new ArrayList<>();
        mUserSearch = v.findViewById(R.id.et_addBySearchFragment_search);
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
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackFragment = null;
    }
    //***************************************************************************************************
    private void initResultList() {
        //on donne à l'adapter le resultat à afficher dans le recycler
        ResultSearchListAdapter resultSearchListAdapter = new ResultSearchListAdapter(getContext(), mGamesResult, getFragmentManager());
        //On ajuste les settings du layout pour le recycler
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS_R, LinearLayoutManager.VERTICAL);
        //on applique les settings layout au recycler
        mResultRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        //on ajoute les données dapatées dans le recycler
        mResultRecyclerView.setAdapter(resultSearchListAdapter);
    }
    //***************************************************************************************************
    @Override
    public void onClick(View v) {

    }
    //***************************************************************************************************
} // end class
