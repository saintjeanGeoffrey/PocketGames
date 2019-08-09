package be.pocketgames.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Box implements Parcelable {

    private Game_component_state mBoxState;

    public Box() {
        mBoxState = Game_component_state.NO;
    }

    public Box(Game_component_state mBoxState) {
        this.mBoxState = mBoxState;
    }

    public Game_component_state getmBoxState() {
        return mBoxState;
    }
    public void setmBoxState(Game_component_state mBoxState) {
        this.mBoxState = mBoxState;
    }

    //***************************************************************************************************

    protected Box(Parcel in) {
        mBoxState = (Game_component_state) in.readSerializable();
    }

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mBoxState);
    }
}
