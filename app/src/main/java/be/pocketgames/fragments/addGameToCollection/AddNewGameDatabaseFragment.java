package be.pocketgames.fragments.addGameToCollection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import be.pocketgames.R;
import be.pocketgames.models.Editor;
import be.pocketgames.models.Game;
import be.pocketgames.models.Genre;
import be.pocketgames.models.Platform;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static be.pocketgames.models.Country.getEnumNames;

public class AddNewGameDatabaseFragment extends Fragment implements View.OnClickListener {

    private EditText mTitle;
    private Spinner mSelectPlatform, mSelectGenre, mSelectEditor;
    private Button mBtnSelectImage, mbtnAddGame;
    private ImageView mImageFromGallery;

    private CallbackFragment mCallbackFragment;
    private Uri mPathImage = null;

    public AddNewGameDatabaseFragment() {
        // Required empty public constructor
    }

    public interface CallbackFragment {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_new_game_database, container, false);
        initValues(v);
        return v;
    }

    private void initValues(View v) {
        mTitle = v.findViewById(R.id.et_addNewGameDatabaseFragment_title);

        mImageFromGallery = v.findViewById(R.id.iv_addNewGameDatabaseFragment_imageSelected);

        mBtnSelectImage = v.findViewById(R.id.btn_addNewGameDatabaseFragment_selectImage);
        mBtnSelectImage.setOnClickListener(this);

        mbtnAddGame = v.findViewById(R.id.btn_addNewGameDatabaseFragment_addgame);
        mbtnAddGame.setOnClickListener(this);

        mSelectPlatform = v.findViewById(R.id.Sp_addNewGameDatabaseFragment_listPlatforms);
        List<String> platforms = getEnumNames(Platform.class);
        Collections.sort(platforms);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, platforms);
        mSelectPlatform.setAdapter(adapter);

        mSelectGenre = v.findViewById(R.id.Sp_addNewGameDatabaseFragment_listGenre);
        List<String> genres = getEnumNames(Genre.class);
        Collections.sort(genres);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, genres);
        mSelectGenre.setAdapter(adapter);

        mSelectEditor = v.findViewById(R.id.Sp_addNewGameDatabaseFragment_listEditor);
        List<String> editors = getEnumNames(Editor.class);
        Collections.sort(editors);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, editors);
        mSelectEditor.setAdapter(adapter);
    }


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

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackFragment = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_addNewGameDatabaseFragment_selectImage) {
            Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        }
        if (v.getId() == R.id.btn_addNewGameDatabaseFragment_addgame) {
            if(isFormValid()) {
                addGameToDatabase();

            }
        }

    }

    private boolean isFormValid() {
        Boolean isValid = true;

        String title = mTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            mTitle.setError("Required.");
            isValid = false;
        } else {
            mTitle.setError(null);
        }

        return isValid;
    }

    private void addGameToDatabase() {
        Game gameToAdd ;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();


        StorageReference riversRef = storageRef.child("games_cover/" + UUID.randomUUID().toString());
        UploadTask uploadTask = riversRef.putFile(mPathImage);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("PATH", exception.getMessage());
                Log.i("PATH", "onSuccess: no ok ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

                Log.i("PATH", "onSuccess: ok ");
                Log.i("PATH", "onSuccess: " +
                        taskSnapshot.getMetadata().getName() +
                        "  |  " +
                        taskSnapshot.getMetadata().getPath()
                );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 101) {
                Uri pickedImage = data.getData();
                mPathImage = pickedImage;
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(pickedImage));
                    mImageFromGallery.setImageBitmap(bitmap);
                }
                catch(FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
