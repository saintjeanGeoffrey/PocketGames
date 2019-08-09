package be.pocketgames.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Book  implements Parcelable {

    private Game_component_state mBook_state ;

    public Book() {
        mBook_state = Game_component_state.NO;
    }

    public Book(Game_component_state mBook_state) {
        this.mBook_state = mBook_state;
    }

    protected Book(Parcel in) {
        mBook_state = (Game_component_state) in.readSerializable();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Game_component_state getmBook_state() {
        return mBook_state;
    }

    public void setmBook_state(Game_component_state mBook_state) {
        this.mBook_state = mBook_state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mBook_state);
    }
}
