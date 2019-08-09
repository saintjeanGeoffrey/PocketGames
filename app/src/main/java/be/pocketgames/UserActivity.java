package be.pocketgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import be.pocketgames.fragments.user.UserFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class UserActivity extends AppCompatActivity implements
        UserFragment.CallbackFragment{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DataSnapshot mDataSnapshot;
    private FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

    //***************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initActionBar();
        initValues();
    }
    //***************************************************************************************************
    private void initActionBar() {
        setCustomActionBar(R.layout.custom_actionbar_user);
    }
    //***************************************************************************************************
    private void initValues() {

        //Listener sur la database lancé à chaque changement dans la db
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataSnapshot = dataSnapshot;
                updateUI(mCurrentUser);
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
        } else {
            showFragment(new UserFragment());
        }
    }
    //***************************************************************************************************
    public void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.addToBackStack("backstack");
        transaction.replace(R.id.LL_userActivity_fragmentView, fragment);
//        transaction.commit();
        transaction.commitAllowingStateLoss();
    }
    //***************************************************************************************************
    @Override
    public DataSnapshot getSnapshot() {
        return mDataSnapshot;
    }
    //***************************************************************************************************
    @Override
    public FirebaseUser getCurrentUser() {
        return mCurrentUser;
    }
    //***************************************************************************************************
    @Override
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }
    //***************************************************************************************************
    public void setCustomActionBar(int layout) {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(layout);
        Log.i("test", "setCustomActionBar: custom added");
    }
} // end class
