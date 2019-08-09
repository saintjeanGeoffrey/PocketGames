package be.pocketgames.models;

import android.os.Parcel;

import java.util.List;

public class GameWishlist extends Game  {

    private String gameID;

    public GameWishlist() {
    }

    public GameWishlist(String mCover,
                        String mTitle,
                        Platform mPlatform,
                        Editor mEditor,
                        Genre mGenre,
                        List<String> mEAN,
                        String gameID) {
        super(mCover, mTitle, mPlatform, mEditor, mGenre, mEAN);
        this.gameID = gameID;
    }

    public String getGameID() {
        return this.gameID;
    }

    protected GameWishlist(Parcel in) {
        super(in);
        gameID = in.readString();
    }

    public static final Creator<GameWishlist> CREATOR = new Creator<GameWishlist>() {
        @Override
        public GameWishlist createFromParcel(Parcel in) {
            return new GameWishlist(in);

        }

        @Override
        public GameWishlist[] newArray(int size) {
            return new GameWishlist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        super.writeToParcel(parcel, flags);
        parcel.writeString(gameID);

    }

}
