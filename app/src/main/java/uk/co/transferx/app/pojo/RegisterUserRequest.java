package uk.co.transferx.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sergey on 27/12/2017.
 */

public class RegisterUserRequest {


    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("upass")
    @Expose
    private String upass;
    @SerializedName("upass_confirmation")
    @Expose
    private String upassConfirmation;



    private RegisterUserRequest(Builder builder) {
        uname = builder.uname;
        email = builder.email;
        upass = builder.upass;
        upassConfirmation = builder.upassConfirmation;
    }


    public static final class Builder {
        private String uname;
        private String email;
        private String upass;
        private String upassConfirmation;

        public Builder() {
        }

        public Builder uname(String val) {
            uname = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder upass(String val) {
            upass = val;
            upassConfirmation = val;
            return this;
        }

        public RegisterUserRequest build() {
            return new RegisterUserRequest(this);
        }
    }
}
