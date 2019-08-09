package be.pocketgames.models;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

public class GameDatabase extends Game  {

    private String gameID;

    public GameDatabase() {
    }

    public GameDatabase(String mCover,
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

    public void addEAN(String barcode) {
        if(this.mEAN == null ) {
            this.mEAN = new ArrayList<>();
            this.mEAN.add(barcode);
        } else {
            this.mEAN.add(barcode);
        }
    }

    protected GameDatabase(Parcel in) {
        super(in);
        gameID = in.readString();
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public static final Creator<GameDatabase> CREATOR = new Creator<GameDatabase>() {
        @Override
        public GameDatabase createFromParcel(Parcel in) {
            return new GameDatabase(in);
        }

        @Override
        public GameDatabase[] newArray(int size) {
            return new GameDatabase[size];
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
