package uk.co.transferx.app.dto;

import android.os.Parcel;
import android.os.Parcelable;

import uk.co.transferx.app.pojo.Recipient;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientDto implements Parcelable {
    private final static String EMPTY = "";
    private final static String SPACE = " ";
    private final String id;
    private final String name;
    private final String imgUrl;
    private final String country;
    private final String phone;

    public RecipientDto(String id, String name, String imgUrl, String country, String number) {
        this.id = id == null ? EMPTY : id;
        this.name = name == null ? EMPTY : name;
        this.imgUrl = imgUrl == null ? EMPTY : imgUrl;
        this.country = country == null ? EMPTY : country;
        this.phone = number == null ? EMPTY : number;
    }

    public String getFavoriteName() {
        StringBuilder stringBuilder = new StringBuilder();
        String firstName = name.replaceAll(" .*", SPACE);
        char LastNameLetter = name.charAt(firstName.length());
        stringBuilder.append(firstName);
        stringBuilder.append(LastNameLetter);
        stringBuilder.append('.');
        return stringBuilder.toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return name.split(SPACE)[0];
    }

    public String getLastName() {
        return name.split(SPACE)[1];
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    protected RecipientDto(Parcel in) {
        id = in.readString();
        name = in.readString();
        imgUrl = in.readString();
        country = in.readString();
        phone = in.readString();
    }

    public RecipientDto(Recipient recipient) {
        id = recipient.getId() == null ? EMPTY : recipient.getId();
        name = getName(recipient.getFirstname(), recipient.getLastname());
        imgUrl = EMPTY;
        country = recipient.getCountry() == null ? EMPTY : recipient.getCountry();
        phone = recipient.getPhone() == null ? EMPTY : recipient.getPhone();
    }


    private String getName(String firstName, String lastName) {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName != null ? firstName : EMPTY);
        sb.append(firstName != null ? SPACE : EMPTY);
        sb.append(lastName != null ? lastName : EMPTY);
        return sb.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imgUrl);
        dest.writeString(country);
        dest.writeString(phone);
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

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (imgUrl != null ? !imgUrl.equals(that.imgUrl) : that.imgUrl != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        return phone != null ? phone.equals(that.phone) : that.phone == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
