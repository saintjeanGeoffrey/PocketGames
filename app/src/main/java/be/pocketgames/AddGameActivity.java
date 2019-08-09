package be.pocketgames;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import be.pocketgames.fragments.addGameToCollection.*;
import be.pocketgames.fragments.main.CollectionListFragment;
import be.pocketgames.models.GameDatabase;
import be.pocketgames.models.GameWishlist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class AddGameActivity extends AppCompatActivity implements
        AddByScanFragment.CallbackFragment,
        AddBySearchFragment.CallbackFragment,
        CollectionListFragment.CallbackFragment,
        AddNewGameDatabaseFragment.CallbackFragment,
        AddGameToCollectionFormFragment.CallbackFragment,
        AssociateBarcodeFragment.CallbackFragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DataSnapshot mDataSnapshot = null;

    //***************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        updateUI(mCurrentUser);
        initValues();
    }
    //***************************************************************************************************
    private void initValues() {
        //Listener sur la database lancé à chaque changement dans la db
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataSnapshot = dataSnapshot;
                Intent i = getIntent();
                if(i.getStringExtra("from").equals("wishlist")){
                    Bundle b = new Bundle();
                    b.putString("from", "wishlist_to_collection");
                    GameWishlist game = i.getParcelableExtra("gameToAddFromWishlist");
                    b.putParcelable("gameToAddFromW", game);
                    AddGameToCollectionFormFragment f = new AddGameToCollectionFormFragment();
                    f.setArguments(b);
                    showFragment(f);
                }
                else if(i.getStringExtra("from").equals("barcode")) {
                    Bundle b = new Bundle();
                    b.putString("from", "barcode_to_collection");
                    GameDatabase game = i.getParcelableExtra("gameToAddFromBarcode");
                    b.putParcelable("gameToAdd", game);
                    AddGameToCollectionFormFragment f = new AddGameToCollectionFormFragment();
                    f.setArguments(b);
                    showFragment(f);
                }
                else {
                    initFragment();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("SN", "on canceled user");
            }
        });
    }
    //***************************************************************************************************
    public void updateUI(FirebaseUser user) {
        if (user == null ) {
            Toast.makeText(this, "User not logged", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }
    //***************************************************************************************************
    private void initFragment() {
        Intent i = getIntent();

        switch(i.getStringExtra("addBy")) {
            case "SCAN" :
                showFragment(new AddByScanFragment());
                break;
            case "SEARCH" :
                showFragment(new AddBySearchFragment());
                break;
        }
    }
    //***************************************************************************************************
    @Override
    public void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.LL_addGameToCollectionActivity_fragmentView, fragment);
//       transaction.commit();
        transaction.commitAllowingStateLoss();
    }
    //***************************************************************************************************
    @Override
    public DataSnapshot getSnapshot() {
        return mDataSnapshot;
    }
    //***************************************************************************************************
    @Override
    public void setCustomActionBar(int layout) {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(layout);
    }

    //***************************************************************************************************
    @Override
    public DataSnapshot getDatasnapshot() {
        return mDataSnapshot;
    }
    //***************************************************************************************************
}
