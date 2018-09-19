package uk.co.transferx.app.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sergey on 12/04/2018.
 */

public class UserSignIn {

    @SerializedName("uname")
    @Expose
    private String uname;
    @SerializedName("upass")
    @Expose
    private String upass;

    private UserSignIn(UserSignIn.Builder builder) {
        uname = builder.uname;
        upass = builder.upass;
    }


    public static final class Builder {
        private String uname;
        private String upass;


        public Builder() {
        }


        public UserSignIn.Builder upass(String val) {
            upass = val;
            return this;
        }

        public UserSignIn.Builder uname(String val) {
            uname = val;
            return this;
        }

        public UserSignIn build() {
            return new UserSignIn(this);
        }
    }
}
