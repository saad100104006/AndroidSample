package uk.co.transferx.app.view.pinview;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by sergey on 04.12.17.
 */

public class PinEditSaveState extends View.BaseSavedState {

    int[] pinValues;

    PinEditSaveState(Parcelable superState) {
        super(superState);
    }

    private PinEditSaveState(Parcel in) {
        super(in);
        pinValues = in.createIntArray();

    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeIntArray(pinValues);

    }

    public static final Parcelable.Creator<PinEditSaveState> CREATOR
            = new Parcelable.Creator<PinEditSaveState>() {
        public PinEditSaveState createFromParcel(Parcel in) {
            return new PinEditSaveState(in);
        }

        public PinEditSaveState[] newArray(int size) {
            return new PinEditSaveState[size];
        }
    };
}
