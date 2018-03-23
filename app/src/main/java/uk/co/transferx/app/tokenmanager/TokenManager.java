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
        if (initialToken == null) {
            initialToken = getInitialTokenFromPreferences();
        }

        return initialToken != null;
    }

    public boolean isTokenExist() {
        if (token == null) {
            token = getTokenFromPreferences();
        }

        return token != null;
    }


    public void setToken(String token) {
        this.token = token;
       // sharedPreferences.edit().putString(TOKEN, this.token).apply();
    }

    public void setInitialToken(String initialToken) {
        this.initialToken = initialToken;
        sharedPreferences.edit().putString(INITIAL_TOKEN, this.initialToken).apply();
    }


    public String getToken() {
      //  if (token == null) {
      //      token = getTokenFromPreferences();
      //  }
        return token;
    }

    public String getInitialToken() {
        if (initialToken == null) {
            initialToken = getInitialTokenFromPreferences();
        }
        return initialToken;
    }

    private String getTokenFromPreferences() {
        return sharedPreferences.getString(TOKEN, null);
    }

    private String getInitialTokenFromPreferences() {
        return sharedPreferences.getString(INITIAL_TOKEN, null);
    }

    public void clearInitToken() {
        initialToken = null;
        sharedPreferences.edit().remove(INITIAL_TOKEN).apply();
    }

    public void clearToken() {
        token = null;
        sharedPreferences.edit().remove(TOKEN).apply();
    }
}
