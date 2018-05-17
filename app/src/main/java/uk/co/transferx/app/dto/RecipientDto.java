package uk.co.transferx.app.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import uk.co.transferx.app.pojo.Recipient;

import static uk.co.transferx.app.util.Constants.EMPTY;

/**
 * Created by sergey on 17.12.17.
 */

public class RecipientDto implements Parcelable {
    private final String id;
    private String firstName;
    private String lastName;
    private String imgUrl;
    private String country;
    private String phone;

    public RecipientDto(String id, String firstName, String lastname, String imgUrl, String country, String number) {
        this.id = id == null ? EMPTY : id;
        this.firstName = firstName == null ? EMPTY : firstName;
        this.lastName = lastname == null ? EMPTY : lastname;
        this.imgUrl = imgUrl == null ? EMPTY : imgUrl;
        this.country = country == null ? EMPTY : country;
        this.phone = number == null ? EMPTY : number;
    }

    public RecipientDto(Recipient recipient) {
        id = recipient.getId() == null ? EMPTY : recipient.getId();
        this.firstName = recipient.getFirstname() == null ? EMPTY : recipient.getFirstname();
        this.lastName = recipient.getLastname() == null ? EMPTY : recipient.getLastname();
        imgUrl = EMPTY;
        country = recipient.getCountry() == null ? EMPTY : recipient.getCountry();
        phone = recipient.getPhone() == null ? EMPTY : recipient.getPhone();
    }


    protected RecipientDto(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        imgUrl = in.readString();
        country = in.readString();
        phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
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

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipientDto that = (RecipientDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(imgUrl, that.imgUrl) &&
                Objects.equals(country, that.country) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, imgUrl, country, phone);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
