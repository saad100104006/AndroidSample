package uk.co.transferx.app.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sergey on 27/12/2017.
 */

public class UserRequest {


    @SerializedName("firstname")
    @Expose
    private String firstName;

    @SerializedName("lastname")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("upass")
    @Expose
    private String upass;

    private UserRequest(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.firstName;
        email = builder.email;
        upass = builder.upass;
    }


    public static final class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String upass;


        public Builder() {
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder upass(String val) {
            upass = val;
            return this;
        }

        public UserRequest build() {
            return new UserRequest(this);
        }
    }
}
