package be.pocketgames.asyncTask;

import android.os.AsyncTask;
import be.pocketgames.models.GameDatabase;
import be.pocketgames.models.Platform;
import be.pocketgames.utils.DataGameGenerator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeneratorGamesAsyncTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        int cpt = 1;
        String key = "";
        for (GameDatabase g: DataGameGenerator.generateGameDatas(Platform.PLAYSTATION4,"http://www.jeuxvideo.com/tous-les-jeux/machine-20/") ) {
            key = db.child("games").push().getKey();
            g.setGameID(key);
            db.child("games").child(key).setValue(g);
            cpt++;
        }

        return null;
    }
}