package be.pocketgames.asyncTask;

import android.os.AsyncTask;
import android.util.Log;
import be.pocketgames.models.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GetWishlistGameAsyncTask extends AsyncTask<DataSnapshot, Void, List<GameWishlist>> {
    @Override
    protected List<GameWishlist> doInBackground(DataSnapshot... dataSnapshots) {

        List<GameWishlist> userGamesCollection = new ArrayList<>();

        DataSnapshot dataSnapshot = dataSnapshots[0];

        if (dataSnapshot != null) {

            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            for ( DataSnapshot data_game : dataSnapshot.child("games_wishlist").child(currentUser).getChildren()) {

                userGamesCollection.add(data_game.getValue(GameWishlist.class));

            }

        }

        return userGamesCollection;
    }
}
