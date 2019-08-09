package be.pocketgames.asyncTask;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import be.pocketgames.models.Country;
import be.pocketgames.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountAsyncTask extends AsyncTask<FirebaseAuth, Void, FirebaseUser> {
    @Override
    protected FirebaseUser doInBackground(FirebaseAuth... firebaseAuths) {
        return null;
    }
}
