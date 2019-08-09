package be.pocketgames.asyncTask;

import android.os.AsyncTask;
import android.util.Log;
import be.pocketgames.models.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GetCollectionGameAsyncTask extends AsyncTask<DataSnapshot, Void, List<GameCollection>> {
    @Override
    protected List<GameCollection> doInBackground(DataSnapshot... dataSnapshots) {
        List<GameCollection> userGamesCollection = new ArrayList<>();
        DataSnapshot dataSnapshot = dataSnapshots[0];
        if (dataSnapshot != null) {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            for ( DataSnapshot data_game : dataSnapshot.child("games_collection").child(currentUser).getChildren()) {
                userGamesCollection.add(data_game.getValue(GameCollection.class));
            }

//            List<Genre> genres= new ArrayList<>();
//            genres.add(Genre.AVENTURE);
//            userGamesCollection.add( new GameCollection(
//                            "http://image.jeuxvideo.com/medias/155447/1554470262-1760-jaquette-avant.jpg",
//                            "Days gone",
//                            Platform.PLAYSTATION4,
//                            Editor.ACTIVISION,
//                            genres,
//                            "26-04-2019",
//                            new ArrayList<>(),
//                            new Box(Game_component_state.NEW),
//                            new Book(Game_component_state.NEW),
//                            new Disc(Game_component_state.NEW),
//                            ""
//                    )
//            );

        }
        return userGamesCollection;
    }
}
