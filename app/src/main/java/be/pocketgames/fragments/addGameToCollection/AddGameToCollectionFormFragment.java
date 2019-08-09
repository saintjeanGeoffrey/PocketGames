package be.pocketgames.fragments.addGameToCollection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import be.pocketgames.MainActivity;
import be.pocketgames.R;
import be.pocketgames.database.DAO.GameCollectionCRUD;
import be.pocketgames.database.DAO.GameWishlistCRUD;
import be.pocketgames.models.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static be.pocketgames.models.Country.getEnumNames;

public class AddGameToCollectionFormFragment extends Fragment implements View.OnClickListener {

    private CallbackFragment mCallbackFragment;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private GameDatabase mGameToadd;
    private GameWishlist mGameToRemove;
    private ImageView mCover;
    private TextView mTitle;
    private Spinner mSelectBoxState, mSelectBookState, mSelectDiscState;
    private  EditText mDescription;
    private Button mFinalAddGame;
    //***************************************************************************************************
    public AddGameToCollectionFormFragment() {}
    //***************************************************************************************************
    public interface CallbackFragment {
        DataSnapshot getSnapshot();
    }
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_game_to_collection_form, container, false);

        if(getArguments().getString("from") != null ) {
            String from = getArguments().getString("from");
            if (from.equals("collection") || from.equals("barcode_to_collection") ) {
                this.mGameToadd = getArguments().getParcelable("gameToAdd");

                mCover = v.findViewById(R.id.iv_addGameToCollection_cover);
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(getContext())
                        .load(mGameToadd.getmCover())
                        .apply(requestOptions)
                        .into(mCover);

                mTitle = v.findViewById(R.id.tv_addGameToCollection_title);
                mTitle.setText(mGameToadd.getmTitle());

                initValues(v);
            } else if (from.equals("wishlist_to_collection") || from.equals("barcode_to_collection")) {
                if(from.equals("wishlist_to_collection")) {
                    this.mGameToRemove = getArguments().getParcelable("gameToAddFromW");

                }
                else if (from.equals("barcode_to_collection")) {
                    this.mGameToRemove = getArguments().getParcelable("gameToAddFromBarcode");
                }

                mCover = v.findViewById(R.id.iv_addGameToCollection_cover);
                RequestOptions requestOptions = new RequestOptions();
                Glide.with(getContext())
                        .load(mGameToRemove.getmCover())
                        .apply(requestOptions)
                        .into(mCover);

                mTitle = v.findViewById(R.id.tv_addGameToCollection_title);
                mTitle.setText(mGameToRemove.getmTitle());

                initValues(v);
            } else if (from.equals("wishlist")) {
                this.mGameToadd = getArguments().getParcelable("gameToAdd");
                addTowishlist();
            }
        }

        return v;
    }
    //***************************************************************************************************
    private void initValues(View v) {

        mSelectBoxState = v.findViewById(R.id.Sp_addGameToCollectionFragment_box);
        List<String> game_box = getEnumNames(Game_component_state.class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, game_box);
        mSelectBoxState.setAdapter(adapter);

        mSelectBookState = v.findViewById(R.id.Sp_addGameToCollectionFragment_book);
        List<String> game_book = getEnumNames(Game_component_state.class);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, game_book);
        mSelectBookState.setAdapter(adapter);

        mSelectDiscState = v.findViewById(R.id.Sp_addGameToCollectionFragment_disc);
        List<String> game_disc = getEnumNames(Game_component_state.class);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, game_disc);
        mSelectDiscState.setAdapter(adapter);

        mDescription = v.findViewById(R.id.et_addGameToCollectionFragment_description);

        mFinalAddGame = v.findViewById(R.id.btn_addGameToCollectionFragment_addGame);
        mFinalAddGame.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_addGameToCollectionFragment_addGame) {
            if(isValidForm()) {
                GameCollectionCRUD db = new GameCollectionCRUD();
                db.insert(
                        getArguments().getString("from").equals("wishlist_to_collection") ?
                                gameWishToCol() : gameCollectionConstruction()
                );
                startActivity(new Intent(getContext(), MainActivity.class));
            }
            if(getArguments().getString("from").equals("wishlist_to_collection")) {
                GameWishlistCRUD db = new GameWishlistCRUD();
                db.delete(mGameToRemove);
            }
        }
    }
    //***************************************************************************************************
    private GameCollection gameWishToCol(String newGameKey) {
        GameCollection g = new GameCollection(
                mGameToRemove.getmCover(),
                mGameToRemove.getmTitle(),
                mGameToRemove.getmPlatform(),
                mGameToRemove.getmEditor(),
                mGameToRemove.getmGenre(),
                mGameToRemove.getmEAN(),
                newGameKey,
                new Box(Game_component_state.valueOf( mSelectBoxState.getSelectedItem().toString().toUpperCase() )),
                new Book(Game_component_state.valueOf( mSelectBookState.getSelectedItem().toString().toUpperCase() )),
                new Disc(Game_component_state.valueOf( mSelectDiscState.getSelectedItem().toString().toUpperCase() )),
                mDescription.getText().toString()
        );
        return g;
    }
    //***************************************************************************************************
    private GameCollection gameWishToCol() {
        GameCollection g = new GameCollection(
                mGameToRemove.getmCover(),
                mGameToRemove.getmTitle(),
                mGameToRemove.getmPlatform(),
                mGameToRemove.getmEditor(),
                mGameToRemove.getmGenre(),
                mGameToRemove.getmEAN(),
                "",
                new Box(Game_component_state.valueOf( mSelectBoxState.getSelectedItem().toString().toUpperCase() )),
                new Book(Game_component_state.valueOf( mSelectBookState.getSelectedItem().toString().toUpperCase() )),
                new Disc(Game_component_state.valueOf( mSelectDiscState.getSelectedItem().toString().toUpperCase() )),
                mDescription.getText().toString()
        );
        return g;
    }
    //***************************************************************************************************
    private GameCollection gameCollectionConstruction(String newGameKey) {
        GameCollection g = new GameCollection(
                mGameToadd.getmCover(),
                mGameToadd.getmTitle(),
                mGameToadd.getmPlatform(),
                mGameToadd.getmEditor(),
                mGameToadd.getmGenre(),
                mGameToadd.getmEAN(),
                newGameKey,
                new Box(Game_component_state.valueOf( mSelectBoxState.getSelectedItem().toString().toUpperCase() )),
                new Book(Game_component_state.valueOf( mSelectBookState.getSelectedItem().toString().toUpperCase() )),
                new Disc(Game_component_state.valueOf( mSelectDiscState.getSelectedItem().toString().toUpperCase() )),
                mDescription.getText().toString()
        );
        return g;
    }
    //***************************************************************************************************
    private GameCollection gameCollectionConstruction() {
        GameCollection g = new GameCollection(
                mGameToadd.getmCover(),
                mGameToadd.getmTitle(),
                mGameToadd.getmPlatform(),
                mGameToadd.getmEditor(),
                mGameToadd.getmGenre(),
                mGameToadd.getmEAN(),
                "",
                new Box(Game_component_state.valueOf( mSelectBoxState.getSelectedItem().toString().toUpperCase() )),
                new Book(Game_component_state.valueOf( mSelectBookState.getSelectedItem().toString().toUpperCase() )),
                new Disc(Game_component_state.valueOf( mSelectDiscState.getSelectedItem().toString().toUpperCase() )),
                mDescription.getText().toString()
        );
        return g;
    }
    //***************************************************************************************************
    private GameWishlist gameWishlistConstruction(String newGameKey) {
        GameWishlist g = new GameWishlist(
                mGameToadd.getmCover(),
                mGameToadd.getmTitle(),
                mGameToadd.getmPlatform(),
                mGameToadd.getmEditor(),
                mGameToadd.getmGenre(),
                mGameToadd.getmEAN(),
                newGameKey
        );
        return g;
    }
    //***************************************************************************************************
    private GameWishlist gameWishlistConstruction() {
        GameWishlist g = new GameWishlist(
                mGameToadd.getmCover(),
                mGameToadd.getmTitle(),
                mGameToadd.getmPlatform(),
                mGameToadd.getmEditor(),
                mGameToadd.getmGenre(),
                mGameToadd.getmEAN(),
                ""
        );
        return g;
    }
    //***************************************************************************************************
    private boolean isValidForm() {
        boolean isValid = true;
        return isValid;
    }
    //***************************************************************************************************
    private void addTowishlist() {

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String newGameKey = mDatabase
                .child("games_wishlist")
                .child( currentUser )
                .push()
                .getKey();

        mDatabase
                .child("games_wishlist")
                .child( currentUser )
                .child( newGameKey )
                .setValue( gameWishlistConstruction(newGameKey) )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("ADD", "GameWishlist ajouté");
                        //Toast.makeText(getContext(), "GameCollection ajouté", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ADD", "GameWishlist echec ajout");
                //Toast.makeText(getContext(), "GameCollection echec ajout", Toast.LENGTH_LONG).show();
            }
        });

        startActivity(new Intent(getContext(), MainActivity.class));
    } //end addToWishlist

}// end class
