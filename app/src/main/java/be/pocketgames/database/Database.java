package be.pocketgames.database;

import be.pocketgames.models.GameCollection;
import be.pocketgames.models.GameDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database INSTANCE = new Database();
    private static DataSnapshot dataSnapshot;

    private Database()
    {}

    public static Database getInstance() {
        return INSTANCE;
    }

    public void updateDataSnapshot (DataSnapshot data) {
        dataSnapshot = data;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public static boolean EanExist(String eanToSearch, DataSnapshot dataSnapshot) {
        for (DataSnapshot g : dataSnapshot.child("games").getChildren() ) {
            for (DataSnapshot ean : g.child("mEAN").getChildren()) {
                if(eanToSearch.equals(ean.getValue().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean linkBarcodeToGame(GameDatabase gameToLink, String barcodeToLink) {
        gameToLink.addEAN(barcodeToLink);
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("games")
                .child(gameToLink.getGameID())
                .setValue(gameToLink);
        return true;
    }

    public static GameDatabase getGameByEan(String ean, DataSnapshot dataSnapshot) {
        if(EanExist(ean, dataSnapshot)) {
            for (DataSnapshot g : dataSnapshot.child("games").getChildren() ) {
                for (DataSnapshot eanG : g.child("mEAN").getChildren()) {
                    if(ean.equals(eanG.getValue().toString())) {
                        return g.getValue(GameDatabase.class);
                    }
                }
            }
        }
        return null;
    }

    public static List<GameDatabase> getGamesByEan(String ean, DataSnapshot dataSnapshot) {
        List<GameDatabase> resultGames = new ArrayList<>();
        if(EanExist(ean, dataSnapshot)) {
            for (DataSnapshot g : dataSnapshot.child("games").getChildren() ) {
                for (DataSnapshot eanG : g.child("mEAN").getChildren()) {
                    if(ean.equals(eanG.getValue().toString())) {
                        resultGames.add(
                                g.getValue(GameDatabase.class)
                        );
                    }
                }
            }
        }
        return resultGames;
    }

    public static boolean deleteGameFromWishlist(String userID, String gameID, String from) {
        FirebaseDatabase.getInstance().getReference()
                .child(from)
                .child(userID)
                .child(gameID)
                .removeValue();
        return true;
    }

    public static boolean editGameCollectionState(String userID, GameCollection gameEdited) {
        FirebaseDatabase.getInstance().getReference()
                .child("games_collection")
                .child(userID)
                .child(gameEdited.getGameID())
                .setValue(gameEdited);
        return true;
    }
} // end class
