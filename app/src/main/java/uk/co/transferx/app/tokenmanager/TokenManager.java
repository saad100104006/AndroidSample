package uk.co.transferx.app.tokenmanager;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by sergey on 08/01/2018.
 */

public class TokenManager {

    private final static String INITIAL_TOKEN = "initial_token";

    private final static String TOKEN = "token";

    private String token;

    private String initialToken;


    private final SharedPreferences sharedPreferences;

    @Inject
    public TokenManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

    }

    public boolean isInitialTokenExist() {
        return initialToken != null;
    }

    public boolean isTokenExist() {
        return token != null;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public void setInitialToken(String initialToken) {
        this.initialToken = initialToken;
    }


    public String getToken() {
        return token;
    }

    public String getInitialToken() {
        return initialToken;
    }

    public void clerInitToken() {
        initialToken = null;
    }

    public void clearToken() {
        token = null;
    }
}
