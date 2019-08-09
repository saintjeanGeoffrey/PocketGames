package be.pocketgames.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import be.pocketgames.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment  implements View.OnClickListener {

    private static String TAG = "CREATE_ACCOUNT";

    private CallbackFragment mCallbackFragment;
    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private Button mCreateAccountButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    public interface CallbackFragment {
        FirebaseAuth getAuth();
        void updateUI(FirebaseUser currentUser);
         void showFragment(Fragment fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailField = view.findViewById(R.id.et_loginActivity_email);
        mPasswordField = view.findViewById(R.id.et_loginActivity_password);
        mLoginButton = view.findViewById(R.id.btn_loginActivity_login);
        mCreateAccountButton = view.findViewById(R.id.btn_loginActivity_createAccount);

        mCreateAccountButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

        this.mAuth = mCallbackFragment.getAuth();
        //Log.i(TAG, mAuth.getUid());
        return view;
    }


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

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackFragment = null;
    }

    //***************************************************************************************************

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    //***************************************************************************************************

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loginActivity_login:
                signIn(mCallbackFragment, mEmailField.getText().toString().trim(), mPasswordField.getText().toString().trim());
                break;
            case R.id.btn_loginActivity_createAccount:
                mCallbackFragment.showFragment(new AccountCreationFragment());
                break;
        }
    }

    //***************************************************************************************************
//
//    private void createAccount(CallbackFragment mLoginActivity, String email, String password) {
//        //validation
//        if (!validateForm()) {
//            return; // on sort de la fonction si form non valide
//        }
//
//        //creation
//        this.mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            mCallbackFragment.updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(getContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            mCallbackFragment.updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

    //***************************************************************************************************

    private void signIn(CallbackFragment mLoginActivity, String email, String password) {
        Log.d(TAG, "signIn:" + email);

        if (!validateForm()) {
            return;
        }
        //showProgressDialog();

        // [START sign_in_with_email]
        this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mCallbackFragment.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mCallbackFragment.updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
//                        }
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    //***************************************************************************************************

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    //***************************************************************************************************

//    private void sendEmailVerification() {
//        // Disable button
//        findViewById(R.id.verifyEmailButton).setEnabled(false);
//
//        // Send verification email
//        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        // Re-enable button
//                        findViewById(R.id.verifyEmailButton).setEnabled(true);
//
//                        if (task.isSuccessful()) {
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        // [END_EXCLUDE]
//                    }
//                });
//        // [END send_email_verification]
//    }


}
