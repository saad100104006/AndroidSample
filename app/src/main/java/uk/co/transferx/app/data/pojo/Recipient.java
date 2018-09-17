package uk.co.transferx.app.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sergey on 03/01/2018.
 */

public class Recipient {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("phone")
    @Expose
    private String phone;

    private Recipient(Builder builder) {
        id = builder.id;
        firstname = builder.firstname;
        lastname = builder.lastname;
        country = builder.country;
        phone = builder.phone;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public static final class Builder {
        private String id;
        private String firstname;
        private String lastname;
        private String country;
        private String phone;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder firstname(String val) {
            firstname = val;
            return this;
        }

        public Builder lastname(String val) {
            lastname = val;
            return this;
        }

        public Builder country(String val) {
            country = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Recipient build() {
            return new Recipient(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipient recipient = (Recipient) o;

        if (id != null ? !id.equals(recipient.id) : recipient.id != null) return false;
        if (firstname != null ? !firstname.equals(recipient.firstname) : recipient.firstname != null)
            return false;
        if (lastname != null ? !lastname.equals(recipient.lastname) : recipient.lastname != null)
            return false;
        if (country != null ? !country.equals(recipient.country) : recipient.country != null)
            return false;
        return phone != null ? phone.equals(recipient.phone) : recipient.phone == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
