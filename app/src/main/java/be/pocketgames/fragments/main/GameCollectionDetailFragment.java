package be.pocketgames.fragments.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import be.pocketgames.R;
import be.pocketgames.asyncTask.UrlToBitmapAsyncTask;
import be.pocketgames.database.Database;
import be.pocketgames.models.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import jp.wasabeef.blurry.Blurry;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static be.pocketgames.models.Country.getEnumNames;

public class GameCollectionDetailFragment extends Fragment {

    private CallbackFragment mCallbackFragment;
    private GameCollection mGameToDetail;

    private TextView mTitle, mEditor, mPlatform, mGenre, mBox, mBook, mDisc, mDescription;
    private Spinner mBoxEdit, mBookEdit, mDiscEdit;
    private EditText mDescriptionEdit;
    private ImageView mCover, mBgBlur;
    private ImageButton mOptions, mBack;
    //***************************************************************************************************
    public GameCollectionDetailFragment() {}
    //***************************************************************************************************
    public interface CallbackFragment {
        void setCustomActionBar(int layout);
        void showFragment(Fragment fragment);
    }
    //***************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_collection_detail, container, false);
        initActionbar(container);
        initValues(v);
        return v;
    }
    //***************************************************************************************************
    private void initActionbar (View v) {
        mCallbackFragment.setCustomActionBar(R.layout.custom_actionbar_game_detail);
    }
    private void initValues(View v) {

        this.mGameToDetail = (GameCollection) getArguments().getParcelable("gameToDetail");

        mCover =v.findViewById(R.id.iv_gameDetailFragment_cover);
        mTitle = v.findViewById(R.id.tv_gameDetailFragment_title);
        mEditor = v.findViewById(R.id.tv_gameDetailFragment_editor);
        mPlatform = v.findViewById(R.id.tv_gameDetailFragment_platform);
        mGenre = v.findViewById(R.id.tv_gameDetailFragment_genre);
        mBox = v.findViewById(R.id.tv_gameDetailFragment_box);
        mBook = v.findViewById(R.id.tv_gameDetailFragment_book);
        mDisc = v.findViewById(R.id.tv_gameDetailFragment_disc);
        mDescription = v.findViewById(R.id.tv_gameDetailFragment_description);

        mCover = v.findViewById(R.id.iv_gameDetailFragment_cover);
        mBgBlur = v.findViewById(R.id.iv_gameDetailFragment_bgBlur);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(getContext())
                .load(mGameToDetail.getmCover())
                .apply(requestOptions)
                .into(mCover);

        UrlToBitmapAsyncTask async = new UrlToBitmapAsyncTask();
        Bitmap i = null;
        try {
            i =  async.execute(mGameToDetail.getmCover()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Blurry.with(getContext())
                .color(Color.argb(75, 0, 0, 0))
                .radius(15)
                .from( i )
                .into(mBgBlur);

        mTitle.setText( mGameToDetail.getmTitle() );
        mEditor.setText( mGameToDetail.getmEditor().getName() );
        if(mGameToDetail.getmPlatform() != null) {
            mPlatform.setText( mGameToDetail.getmPlatform().getName() );
        } else {
            mGenre.setText( "-" );
        }
        if(mGameToDetail.getmGenre() != null) {
            mGenre.setText( mGameToDetail.getmGenre().getName() );
        } else {
            mGenre.setText( "-" );
        }

        mBox.setText( mGameToDetail.getmBox().getmBoxState().getName() );
        mBook.setText( mGameToDetail.getmBook().getmBook_state().getName() );
        mDisc.setText( mGameToDetail.getmDisc().getmDisc_state().getName() );
        mDescription.setText(mGameToDetail.getmDescription() );

        mOptions = getActivity().findViewById(R.id.ibtn_custom_actionbar_detail_options);
        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popmenu_game_details_options, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // setup the alert builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Edit informations :");
                        // Get the layout inflater
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.layout_edit_detail_modal, null);
                        builder.setView(dialogView);

                        mBoxEdit = dialogView.findViewById(R.id.sp_layoutEditDetail_box_edit);
                        List<String> game_box = getEnumNames(Game_component_state.class);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, game_box);
                        mBoxEdit.setAdapter(adapter);
                        mBoxEdit.setSelection( adapter.getPosition(mBox.getText().toString()) );

                        mBookEdit = dialogView.findViewById(R.id.sp_layoutEditDetail_book_edit);
                        List<String> game_book = getEnumNames(Game_component_state.class);
                        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, game_book);
                        mBookEdit.setAdapter(adapter);
                        mBookEdit.setSelection( adapter.getPosition(mBook.getText().toString()) );

                        mDiscEdit  = dialogView.findViewById(R.id.sp_layoutEditDetail_disc_edit);
                        List<String> game_disc = getEnumNames(Game_component_state.class);
                        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, game_disc);
                        mDiscEdit.setAdapter(adapter);
                        mDiscEdit.setSelection( adapter.getPosition(mDisc.getText().toString()) );

                        mDescriptionEdit  = dialogView.findViewById(R.id.et_layoutEditDetail_description_edit);
                        mDescriptionEdit.setText(mDescription.getText().toString());


                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //change infos in database
                                GameCollection gameEdited = mGameToDetail;
                                gameEdited.setmBox(new Box(Game_component_state.valueOf(mBoxEdit.getSelectedItem().toString().replace(" ", "_").toUpperCase())));
                                gameEdited.setmBook(new Book(Game_component_state.valueOf(mBookEdit.getSelectedItem().toString().replace(" ", "_").toUpperCase())));
                                gameEdited.setmDisc(new Disc(Game_component_state.valueOf(mDiscEdit.getSelectedItem().toString().replace(" ", "_").toUpperCase())));
                                gameEdited.setmDescription(mDescriptionEdit.getText().toString());
                                Database.editGameCollectionState(
                                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        gameEdited
                                );
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        mBack = getActivity().findViewById(R.id.ibtn_custom_actionbar_detail_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackFragment.showFragment(new CollectionListFragment());
            }
        });

    }
    //***************************************************************************************************
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
    //***************************************************************************************************
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackFragment = null;
    }
    //***************************************************************************************************

} // end class
