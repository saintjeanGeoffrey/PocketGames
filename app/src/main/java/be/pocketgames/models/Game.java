package be.pocketgames.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public abstract class Game implements Parcelable {

    protected String mCover;
    protected String mTitle;
    protected Platform mPlatform;
    protected Editor mEditor;
    protected Genre mGenre;
    protected List<String> mEAN;

    public Game() {}

    protected Game(String mCover,
                   String mTitle,
                   Platform mPlatform,
                   Editor mEditor,
                   Genre mGenre,
                   List<String> mEAN
    ) {
        this.mCover = mCover;
        this.mTitle = mTitle;
        this.mPlatform = mPlatform;
        this.mEditor = mEditor;
        this.mGenre = mGenre;
        if(mEAN == null) {
            this.mEAN = new ArrayList<>();
        }
        else {
            this.mEAN = mEAN;
        }
    }

    protected Game(Parcel in) {
        mCover = in.readString();
        mTitle = in.readString();
        mPlatform = (Platform) in.readSerializable();
        mEditor = (Editor) in.readSerializable();
        mGenre = (Genre) in.readSerializable();
        mEAN = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCover);
        dest.writeString(mTitle);
        dest.writeSerializable(mPlatform);
        dest.writeSerializable(mEditor);
        dest.writeSerializable(mGenre);
        dest.writeStringList(mEAN);
    }

    public String getmCover() {
        return mCover;
    }

    public String getmTitle() {
        return mTitle;
    }

    public Platform getmPlatform() {
        return mPlatform;
    }

    public Editor getmEditor() {
        return mEditor;
    }

    public Genre getmGenre() {
        return mGenre;
    }

    public List<String> getmEAN() {
        return mEAN;
    }

    public void setmCover(String mCover) {
        this.mCover = mCover;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmPlatform(Platform mPlatform) {
        this.mPlatform = mPlatform;
    }

    public void setmEditor(Editor mEditor) {
        this.mEditor = mEditor;
    }

    public void setmGenre(Genre mGenre) {
        this.mGenre = mGenre;
    }

    public void setmEAN(List<String> mEAN) {
        this.mEAN = mEAN;
    }

    @Override
    public String toString() {
        return "Game{" +
                "mCover='" + mCover + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mPlatform=" + mPlatform +
                ", mEditor='" + mEditor + '\'' +
                ", mGenre=" + mGenre + '\'' +
                ", mEAN='" + mEAN + '\'' +
                '}';
    }
}
