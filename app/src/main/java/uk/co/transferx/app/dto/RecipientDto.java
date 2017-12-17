package uk.co.transferx.app.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientDto implements Parcelable {
    private final String name;
    private final String imgUrl;

    public RecipientDto(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }


    protected RecipientDto(Parcel in) {
        name = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<RecipientDto> CREATOR = new Creator<RecipientDto>() {
        @Override
        public RecipientDto createFromParcel(Parcel in) {
            return new RecipientDto(in);
        }

        @Override
        public RecipientDto[] newArray(int size) {
            return new RecipientDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imgUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipientDto that = (RecipientDto) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        return getImgUrl() != null ? getImgUrl().equals(that.getImgUrl()) : that.getImgUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getImgUrl() != null ? getImgUrl().hashCode() : 0);
        return result;
    }
}
