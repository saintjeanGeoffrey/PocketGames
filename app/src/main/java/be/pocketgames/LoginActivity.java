package be.pocketgames;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import be.pocketgames.asyncTask.LogOutAsyncTask;
import be.pocketgames.fragments.login.AccountCreationFragment;
import be.pocketgames.fragments.login.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.CallbackFragment,
        AccountCreationFragment.CallbackFragment {

    private static String TAG = "LOGIN";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    private DataSnapshot mDataSnapshot;


    //***************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        Log.wtf("MY_TEST", "init login");

        //on initialise les valeurs de login activity
        mCurrentUser = mAuth.getCurrentUser();
        //on charge le fragment
        updateUI(mAuth.getCurrentUser());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("SN", "on canceled user");
            }
        });

    }

    //***************************************************************************************************
    @Override
    protected void onStop() {
        super.onStop();
        //        logOut();
    }
    //***************************************************************************************************
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //***************************************************************************************************
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);

    }
    //***************************************************************************************************
    @Override
    public void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.LL_loginActivity_fragmentView, fragment);
//        transaction.commit();
        transaction.commitAllowingStateLoss();
    }
    //***************************************************************************************************
    public void logOut() {
        LogOutAsyncTask task = new LogOutAsyncTask();
        task.execute(mAuth.getInstance());
        updateUI(null);
    }
    //***************************************************************************************************
    //interface de callback, permet le transfert de données entre fragment et activity parente
    @Override
    public void updateUI(FirebaseUser user) {
        if (user == null ) {
            showFragment( new LoginFragment() );
        }
        else {
            //showFragment( new LoggedFragment() );
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

            //Clear l'activité "Login"
            finish();
        }
    }
    //***************************************************************************************************
    @Override
    public FirebaseAuth getAuth() {
        return mAuth;
    }
    //***************************************************************************************************
    @Override
    public DataSnapshot getSnapshot() {
        return mDataSnapshot;
    }
//***************************************************************************************************
} //end class
