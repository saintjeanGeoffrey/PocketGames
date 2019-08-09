package be.pocketgames.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Disc  implements Parcelable {
    private Game_component_state mDisc_state;

    public Disc() {
        mDisc_state = Game_component_state.NO;
    }

    public Disc(Game_component_state mDisc_state) {
        this.mDisc_state = mDisc_state;
    }

    protected Disc(Parcel in) {
        mDisc_state = (Game_component_state) in.readSerializable();
    }

    public static final Creator<Disc> CREATOR = new Creator<Disc>() {
        @Override
        public Disc createFromParcel(Parcel in) {
            return new Disc(in);
        }

        @Override
        public Disc[] newArray(int size) {
            return new Disc[size];
        }
    };

    public Game_component_state getmDisc_state() {
        return mDisc_state;
    }

    public void setmDisc_state(Game_component_state mDisc_state) {
        this.mDisc_state = mDisc_state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mDisc_state);
    }
}
