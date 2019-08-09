package be.pocketgames.asyncTask;

import android.os.AsyncTask;
import com.google.firebase.auth.FirebaseAuth;

public class LogOutAsyncTask extends AsyncTask<FirebaseAuth, Void, Integer> {
    @Override
    protected Integer doInBackground(FirebaseAuth... firebaseAuths) {
        firebaseAuths[0].signOut();
        return 0;
    }
}
