package uk.co.transferx.app.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientDto implements Parcelable {
    private final static String EMPTY = "";
    private final String name;
    private final String imgUrl;
    private final String country;

    public RecipientDto(String name, String imgUrl, String country) {
        this.name = name == null ? EMPTY : name;
        this.imgUrl = imgUrl == null ? EMPTY : imgUrl;
        this.country = country == null ? EMPTY : country;
    }

    public String getFavoriteName() {
        StringBuilder stringBuilder = new StringBuilder();
        String firstName = name.replaceAll(" .*", " ");
        char LastNameLetter = name.charAt(firstName.length());
        stringBuilder.append(firstName);
        stringBuilder.append(LastNameLetter);
        stringBuilder.append('.');
        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCountry() {
        return country;
    }

    protected RecipientDto(Parcel in) {
        name = in.readString();
        imgUrl = in.readString();
        country = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imgUrl);
        dest.writeString(country);
    }

    @Override
    public int describeContents() {
        return 0;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipientDto that = (RecipientDto) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getImgUrl() != null ? !getImgUrl().equals(that.getImgUrl()) : that.getImgUrl() != null)
            return false;
        return getCountry() != null ? getCountry().equals(that.getCountry()) : that.getCountry() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getImgUrl() != null ? getImgUrl().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        return result;
    }
}
