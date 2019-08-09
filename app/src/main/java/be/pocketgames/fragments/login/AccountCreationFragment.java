package be.pocketgames.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import be.pocketgames.R;
import be.pocketgames.models.Country;
import be.pocketgames.models.GameCollection;
import be.pocketgames.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static be.pocketgames.models.Country.getEnumNames;

public class AccountCreationFragment extends Fragment implements View.OnClickListener{

    private static String TAG = "CREATE_ACCOUNT";

    private CallbackFragment mCallbackFragment;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DataSnapshot mDataSnapshot;

    private EditText mFirstname, mLastname, mEmail, mPassword, mConfirmPassword;
    private Spinner mSelectCountry;
    private Button mConfirmForm;

    //***************************************************************************************************
    public AccountCreationFragment() {}
    //***************************************************************************************************
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_creation, container, false);
        v = init(v);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accountFragment_confirmeAccCreation :
                createAccount(mCallbackFragment,
                        mEmail.getText().toString(),
                        mPassword.getText().toString(),
                        mConfirmPassword.getText().toString()
                );
                break;
        }
    }
    //***************************************************************************************************
    public interface CallbackFragment {
        // TODO: Update argument type and name
        void showFragment(Fragment fragment);
        FirebaseAuth getAuth();
        void updateUI(FirebaseUser user);
        DataSnapshot getSnapshot();

    }
    //***************************************************************************************************
    private View init(View v) {

        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mDataSnapshot = mCallbackFragment.getSnapshot();

        mConfirmForm = v.findViewById(R.id.btn_accountFragment_confirmeAccCreation);
        mFirstname = v.findViewById(R.id.et_accountCrFragment_firstname);
        mLastname = v.findViewById(R.id.et_accountCrFragment_lastname);
        mEmail = v.findViewById(R.id.et_accountCrFragment_email);
        mPassword = v.findViewById(R.id.et_accountCrFragment_password);
        mConfirmPassword = v.findViewById(R.id.et_accountCrFragment_confirmPassword);

        mSelectCountry = v.findViewById(R.id.Sp_accountCrFragment_listCountires);
        List<String> countries = getEnumNames(Country.class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, countries);
        mSelectCountry.setAdapter(adapter);

        mConfirmForm.setOnClickListener(this);

        return v;
    }
    //***************************************************************************************************
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallbackFragment) {
            mCallbackFragment = (CallbackFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CollectionListFragment");
        }
    }
    //***************************************************************************************************
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackFragment = null;
    }
    //***************************************************************************************************
    private void createAccount(AccountCreationFragment.CallbackFragment mLoginActivity, String email, String password, String confirmPassword) {
        //validation
        if (!validateForm()) {
            return; // on sort de la fonction si form non valide
        }

        //creation
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(), new User(
                                    "",
                                    mFirstname.getText().toString(),
                                    mLastname.getText().toString(),
                                    mEmail.getText().toString(),
                                    Country.valueOf(mSelectCountry.getSelectedItem().toString().toUpperCase())
                            ));
                            mCallbackFragment.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("ERROR", "onComplete fail :   " + task.getException().getMessage());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            mCallbackFragment.updateUI(null);
                        }
                    }
                });
    }
    //***************************************************************************************************
    private boolean validateForm() {
        boolean valid = true;

        String firstname = mFirstname.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            mFirstname.setError("Required.");
            valid = false;
        } else {
            mFirstname.setError(null);
        }

        String lastname = mLastname.getText().toString();
        if (TextUtils.isEmpty(lastname)) {
            mLastname.setError("Required.");
            valid = false;
        } else {
            mLastname.setError(null);
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        String confirmPassword = mConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError("Required.");
            valid = false;
        } else if (!(password.equals(confirmPassword))) {
            valid = false;
            mConfirmPassword.setError("wrong password");
        }  else {
            mPassword.setError(null);
        }

        return valid;
    }
    //***************************************************************************************************
    private void writeNewUser(String userID, User newUser) {
        mDatabase.child("users").child(userID).setValue(newUser);
        mDatabase.child("games_collection").child(userID).setValue(" ");
    }
} // end class
