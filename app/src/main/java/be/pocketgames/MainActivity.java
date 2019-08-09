package be.pocketgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import be.pocketgames.database.Database;
import be.pocketgames.fragments.main.CollectionListFragment;
import be.pocketgames.fragments.main.GameCollectionDetailFragment;
import be.pocketgames.fragments.main.WishlistFragment;
import be.pocketgames.fragments.user.UserFragment;
import be.pocketgames.models.Editor;
import be.pocketgames.models.GameDatabase;
import be.pocketgames.models.Genre;
import be.pocketgames.models.Platform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        PopupMenu.OnMenuItemClickListener,
        View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        CollectionListFragment.CallbackFragment,
        UserFragment.CallbackFragment,
        WishlistFragment.CallbackFragment,
        GameCollectionDetailFragment.CallbackFragment
{

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private String mUserID = mCurrentUser.getUid();

    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton mFab_add;

    //***************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ab.hide();
//        addGame();
//        new GeneratorGamesAsyncTask()
//                .execute();
        initValues();
    }

    private void addGame() {
       String key = mDatabase.child("games").push().getKey();
        List<String> mEAN = new ArrayList<>();
        mEAN.add("3307216080428");
        GameDatabase g = new GameDatabase(
                "http://image.jeuxvideo.com/medias/152876/1528757808-685-jaquette-avant.jpg",
                "Tom Clancy's The Division 2",
                Platform.PLAYSTATION4,
                Editor.UBISOFT,
                Genre.AVENTURE,
                mEAN,
                key
        );
        mDatabase.child("games").child(key).setValue(g);
    }

    //***************************************************************************************************
    private void initValues() {
        //Listener sur la database lancé à chaque changement dans la db
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Database.getInstance().updateDataSnapshot(dataSnapshot);
                updateUI(mCurrentUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("SN", "on canceled user");
            }
        });

        //on init la BottomNav
        //initializing BottomNavigationView
        mBottomNavigationView = findViewById(R.id.bnv_mainActivity_bottom_navigation);
        // set the item "collection" current selected item
        mBottomNavigationView.setSelectedItemId(R.id.item_bottomNav_collection);
        //ajout du listener de changement d'item dans la bottomNav
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        //récup du fab + ajout du click listener
        mFab_add = findViewById(R.id.fab_wishlistFragment_add);
        mFab_add.setOnClickListener(this);

    }
    //***************************************************************************************************
    //add action bar ( search) menu in top bar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //on met notre menu top_bar dans la topbar du systeme( appelé auto )
//        getMenuInflater().inflate(R.menu.top_actions_bar_menu, menu);
//        return true;
//    }
    //***************************************************************************************************
    //implement click search icon, listener sur les icons du menu top bar
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.item_topBar_search:
//                Toast.makeText(this, "item TEST", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    //***************************************************************************************************
    @Override
    public void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.addToBackStack("backstack");
        transaction.replace(R.id.LL_mainActivity_fragmentView, fragment);
//        transaction.commit();
        transaction.commitAllowingStateLoss();
    }
    //***************************************************************************************************
    //implement callbacl frag
    @Override
    public void updateUI(FirebaseUser user) {
        if (user == null ) {
            Toast.makeText(this, "User not logged", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {
            showFragment(new CollectionListFragment());
        }
    }
    //***************************************************************************************************
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_bottomNav_collection:
                if(!menuItem.isChecked()) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.LL_mainActivity_fragmentView, new CollectionListFragment())
                            .commitAllowingStateLoss();
                }
                break;
            case R.id.item_bottomNav_wishlist:
                if(!menuItem.isChecked()) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.LL_mainActivity_fragmentView, new WishlistFragment())
                            .commitAllowingStateLoss();
                }
                break;
        }
        return true;
    }
    //***************************************************************************************************
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab_wishlistFragment_add:
                PopupMenu popupMenu = new PopupMenu(this, v);
                popupMenu.getMenuInflater().inflate(R.menu.popmenu_add_game_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
        }
    }
    //***************************************************************************************************
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_popmenu_addBySearch:
                startActivity( new Intent(this, AddGameActivity.class)
                        .putExtra("from", "")
                        .putExtra("addBy", "SEARCH")
                );
                break;
            case R.id.item_popmenu_addByScan:
                startActivity( new Intent(this, AddGameActivity.class)
                        .putExtra("from", "")
                        .putExtra("addBy", "SCAN")
                );
                break;
        }
        return true;
    }

    @Override
    public DataSnapshot getSnapshot() {
        return null;
    }

    //***************************************************************************************************
    //callbacl fragments
    @Override
    public FirebaseUser getCurrentUser() {
        return mCurrentUser;
    }
    //***************************************************************************************************
    //callbacl fragments
    @Override
    public FirebaseAuth getAuth() {
        return mAuth;
    }
    //***************************************************************************************************

    @Override
    public void setCustomActionBar(int layout) {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(layout);
    }
} // end class
