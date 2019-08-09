package be.pocketgames.database.DAO;

import android.support.annotation.NonNull;
import be.pocketgames.models.GameWishlist;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameWishlistCRUD implements CRUD_interface<GameWishlist> {

    private final DatabaseReference DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference().child("games_wishlist");
    private final FirebaseUser CURRENT_USER = FirebaseAuth.getInstance().getCurrentUser();
    private boolean success;

    @Override
    public boolean insert(GameWishlist entity) {
        return false;
    }

    @Override
    public boolean update(GameWishlist entity) {
        return false;
    }

    @Override
    public boolean delete(GameWishlist entity) {
        this.success = false;
        DATABASE_REFERENCE
                .child(CURRENT_USER.getUid())
                .child(entity.getGameID())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        success = true;
                    }
                });
        return success;
    }

    @Override
    public GameWishlist getEntityById(String id) {
        return null;
    }
}
