package be.pocketgames.database.DAO;

import android.support.annotation.NonNull;
import be.pocketgames.models.GameDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameDatabaseCRUD implements CRUD_interface<GameDatabase> {

    private final DatabaseReference DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference().child("games");
    private boolean success;

    @Override
    public boolean insert(GameDatabase entity) {
        return false;
    }

    @Override
    public boolean update(GameDatabase entity) {
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
    public boolean delete(GameDatabase entity) {
        return false;
    }

    @Override
    public GameDatabase getEntityById(String id) {
        return null;
    }

    public boolean EanExist(String eanToSearch, DataSnapshot dataSnapshot) {
        for (DataSnapshot g : dataSnapshot.child("games").getChildren() ) {
            for (DataSnapshot ean : g.child("mEAN").getChildren()) {
                if(eanToSearch.equals(ean.getValue().toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
