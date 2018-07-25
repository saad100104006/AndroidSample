package uk.co.transferx.app;

import android.content.SharedPreferences;

import javax.net.ssl.HttpsURLConnection;

import uk.co.transferx.app.errors.UnauthorizedException;

import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;

/**
 * Created by smilevkiy on 14.11.17.
 */

public abstract class BasePresenter<T extends UI> {

    protected SharedPreferences sharedPreferences;

    public BasePresenter() {
    }

    public BasePresenter(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    protected T ui;

    public void attachUI(T ui) {
        this.ui = ui;
    }

    public void detachUI() {
        ui = null;
    }


    protected void globalErrorHandler(int code) {
        if (code != HttpsURLConnection.HTTP_UNAUTHORIZED) {
            return;
        }
        handleErrorToken();
    }

    protected void globalErrorHandler(Throwable throwable) {
        if (throwable instanceof UnauthorizedException) {
            handleErrorToken();
        }
    }

    private void handleErrorToken() {
        if (ui != null) {
            sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
            ui.goToWelcome();
        }
    }
}
