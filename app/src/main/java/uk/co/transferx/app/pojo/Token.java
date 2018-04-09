package uk.co.transferx.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sergey on 01/04/2018.
 */

public class Token {

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        return token != null ? token.equals(token1.token) : token1.token == null;
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }
}
