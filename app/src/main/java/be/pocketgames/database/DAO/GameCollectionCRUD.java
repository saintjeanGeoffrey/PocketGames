package be.pocketgames.database.DAO;

import android.support.annotation.NonNull;
import be.pocketgames.database.Database;
import be.pocketgames.models.GameCollection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameCollectionCRUD implements CRUD_interface<GameCollection> {

    private final DatabaseReference DATABASE_REFERENCE;
    private  final FirebaseUser CURRENT_USER;
    private DataSnapshot dataSnapshot;
    private boolean success;

    public GameCollectionCRUD() {
        this.DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference().child("games_collection");
        this.CURRENT_USER = FirebaseAuth.getInstance().getCurrentUser();
        this.dataSnapshot = Database.getInstance().getDataSnapshot().child("games_collection");
        this.success = false;
    }

    @Override
    public boolean insert(GameCollection entity) {
        this.success = false;

        String newGameKey = DATABASE_REFERENCE
                .child( CURRENT_USER.getUid() )
                .push()
                .getKey();

        entity.setGameID(newGameKey);

        DATABASE_REFERENCE
                .child( CURRENT_USER.getUid() )
                .child( newGameKey )
                .setValue( entity )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        success = true;
                    }
                });

        return success;
    }

    @Override
    public boolean update(GameCollection entity) {
        this.success = false;
        DATABASE_REFERENCE
                .child(entity.getGameID())
                .setValue(entity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        success = true;
                    }
                });
        return success;
    }

    @Override
    public boolean delete(GameCollection entity) {
        this.success = false;
        DATABASE_REFERENCE
                .child(CURRENT_USER.getUid())
                .child(entity.getGameID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                success = true;
            }
        });
        return success;
    }

    @Override
    public GameCollection getEntityById(String id) {
        return null;
    }

    public List<GameCollection> getAllGameCollection() {
        List<GameCollection> gameList = new ArrayList<>();
        for ( DataSnapshot game : this.dataSnapshot.child(CURRENT_USER.getUid()).getChildren()) {
            gameList.add(game.getValue(GameCollection.class));
        }
        return gameList;
    }

}
