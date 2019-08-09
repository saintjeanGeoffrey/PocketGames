package be.pocketgames.models;

import android.os.Parcel;

import java.util.List;

public class GameCollection extends Game {

    private String gameID;
    private Box mBox;
    private Book mBook;
    private Disc mDisc;
    private String mDescription;

    public GameCollection() {
    }

    public GameCollection(String mCover,
                          String mTitle,
                          Platform mPlatform,
                          Editor mEditor,
                          Genre mGenre,
                          List<String> mEAN,
                          String gameID,
                          Box mBox,
                          Book mBook,
                          Disc mDisc,
                          String mDescription) {
        super(mCover, mTitle, mPlatform, mEditor, mGenre, mEAN);
        this.gameID = gameID;
        this.mBox = mBox;
        this.mBook = mBook;
        this.mDisc = mDisc;
        this.mDescription = mDescription;
    }

    public GameCollection(String mCover,
                          String mTitle,
                          Platform mPlatform,
                          Editor mEditor,
                          Genre mGenre,
                          List<String> mEAN,
                          Box mBox,
                          Book mBook,
                          Disc mDisc,
                          String mDescription
    ) {
        super(mCover, mTitle, mPlatform, mEditor, mGenre, mEAN);
        this.mBox = mBox;
        this.mBook = mBook;
        this.mDisc = mDisc;
        this.mDescription = mDescription;
    }

    protected GameCollection(Parcel in) {
        super(in);
        gameID = in.readString();
        mBox = in.readParcelable(Box.class.getClassLoader());
        mBook = in.readParcelable(Book.class.getClassLoader());
        mDisc = in.readParcelable(Disc.class.getClassLoader());
        mDescription = in.readString();
    }

    public static final Creator<GameCollection> CREATOR = new Creator<GameCollection>() {
        @Override
        public GameCollection createFromParcel(Parcel in) {
            return new GameCollection(in);
        }

        @Override
        public GameCollection[] newArray(int size) {
            return new GameCollection[size];
        }
    };

    public Box getmBox() {
        return mBox;
    }

    public Book getmBook() {
        return mBook;
    }

    public Disc getmDisc() {
        return mDisc;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getGameID() {
        return gameID;
    }

    public void setmBox(Box mBox) {
        this.mBox = mBox;
    }

    public void setmBook(Book mBook) {
        this.mBook = mBook;
    }

    public void setmDisc(Disc mDisc) {
        this.mDisc = mDisc;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(gameID);
        dest.writeParcelable(mBox, flags);
        dest.writeParcelable(mBook, flags);
        dest.writeParcelable(mDisc, flags);
        dest.writeString(mDescription);
    }
}
