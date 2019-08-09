package be.pocketgames.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import be.pocketgames.R;
import be.pocketgames.asyncTask.LogOutAsyncTask;
import be.pocketgames.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class UserFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "WishlistFragment";

    private CallbackFragment mCallbackFragment;

    private TextView mUserFirstanme, mUserLastname, mUserEmail, mUserCountry;
    private Button mButton_logout;
    //***************************************************************************************************
    //constructor
    public UserFragment() {}
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_user, container, false);
        init(v);
        initUserInfos();
        return v;
    }
    //***************************************************************************************************
    private void init(View v) {
        mUserFirstanme = v.findViewById(R.id.tv_userFragment_firstname);
        mUserLastname = v.findViewById(R.id.tv_userFragment_lastname);
        mUserEmail = v.findViewById(R.id.tv_userFragment_email);
        mUserCountry = v.findViewById(R.id.tv_userFragment_country);
        mButton_logout = v.findViewById(R.id.btn_userFragment_logout);
        mButton_logout.setOnClickListener(this);
    }
    //***************************************************************************************************
    private void initUserInfos() {
        DataSnapshot sn = mCallbackFragment.getSnapshot();
        if (sn != null) {
            String cUser = mCallbackFragment.getCurrentUser().getUid();
            User dataCurrentUser = sn.child("users").child(cUser).getValue(User.class);
            mUserFirstanme.setText(dataCurrentUser.getmFirstname());
            mUserLastname.setText(dataCurrentUser.getmLastname());
            mUserEmail.setText(dataCurrentUser.getmEmail());
            mUserCountry.setText(dataCurrentUser.getmCountry().getName());
        }
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
        switch (v.getId()) {
            case R.id.btn_userFragment_logout:
                LogOutAsyncTask task = new LogOutAsyncTask();
                task.execute(mCallbackFragment.getAuth().getInstance());
                mCallbackFragment.updateUI(null);
                break;
        }
    }
    //***************************************************************************************************
    public interface CallbackFragment {
        DataSnapshot getSnapshot();
        FirebaseUser getCurrentUser();
        FirebaseAuth getAuth();
        void updateUI(FirebaseUser user);
    }
    //***************************************************************************************************
} // end class
