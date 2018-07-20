package uk.co.transferx.app.welcom.presenter;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.pojo.UserSignIn;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.tokenmanager.TokenRepository;
import uk.co.transferx.app.util.Util;

import static uk.co.transferx.app.util.Constants.CREDENTIAL;
import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;
import static uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT;

/**
 * Created by smilevkiy on 14.11.17.
 */

public class WelcomeFragmentPresenter extends BasePresenter<WelcomeFragmentPresenter.WelcomeUI> {

    private final SignInOutApi signInOutApi;
    private final SignUpApi signUpApi;
    private Disposable disposable;
    private final TokenManager tokenManager;
    private final SharedPreferences sharedPreferences;
    private final TokenRepository tokenRepository;
    private String email;
    private String password;

    @Inject
    public WelcomeFragmentPresenter(final SignInOutApi signInOutApi, final SignUpApi signUpApi,
                                    final TokenManager tokenManager,
                                    final SharedPreferences sharedPreferences,
                                    final TokenRepository tokenRepository) {
        this.signInOutApi = signInOutApi;
        this.tokenManager = tokenManager;
        this.signUpApi = signUpApi;
        this.sharedPreferences = sharedPreferences;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void signInClicked() {
        if (tokenRepository.getToken().getAccessToken().isEmpty() && ui != null) {
            ui.showConnectionError();
            return;
        }
        signIn(email, password);
    }

    public void validateEmail(String email) {
        if (ui != null) {
            this.email = email;
            ui.changeButtonState(isInputDataValid());
        }
    }

    public void validatePassword(String password) {
        if (ui != null) {
            this.password = password;
            ui.changeButtonState(isInputDataValid());
        }
    }

    private boolean isInputDataValid() {
        return Util.validateEmail(email) && Util.validatePassword(password);
    }

    private void signIn(String email, String password) {
        UserSignIn.Builder request = new UserSignIn.Builder();
        disposable = tokenManager.getToken()
                .flatMap(token -> signInOutApi.signIn(token.getAccessToken(), request.uname(email).upass(password).build()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        tokenManager.saveToken(resp.body());
                        if (sharedPreferences.getBoolean(PIN_SHOULD_BE_INPUT, false) ||
                                !sharedPreferences.getBoolean(LOGGED_IN_STATUS, false)) {
                            ui.goToPinView(email, password);
                            return;
                        }
                        ui.goToMainScreen();
                        return;
                    } else if (resp.code() == HttpsURLConnection.HTTP_NOT_FOUND && ui != null) {
                        ui.showUserNotFound();
                        return;
                    } else if (resp.code() == HttpsURLConnection.HTTP_UNAUTHORIZED && ui != null) {
                        ui.showWrongPassword();
                        return;
                    }
                    ui.showConnectionError();

                }, this::handleError);

    }

    public void recoverPasswordClicked() {
        ui.goRecoverPassword();
    }

    public void signUpClicked() {
        if (!tokenRepository.getToken().getAccessToken().isEmpty()) {
            sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, false).apply();
            sharedPreferences.edit().remove(CREDENTIAL).apply();
            ui.goToSignUp();
            return;
        }
        ui.showConnectionError();
    }

    public void refreshGenesisToken() {
        disposable = signUpApi.getInitialToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (ui != null) {
                        if (resp.code() == HttpsURLConnection.HTTP_OK) {
                            tokenManager.saveToken(resp.body());
                            return;
                        }
                        ui.showConnectionError();
                    }

                }, this::handleError);
    }

    private void handleError(Throwable throwable) {
        if (ui != null) {
            ui.showConnectionError();
        }
    }


    public interface WelcomeUI extends UI {

        void goToSignUp();

        void goRecoverPassword();

        void goToMainScreen();

        void showConnectionError();

        void showEmailError();

        void showPasswordError();

        void showUserNotFound();

        void showWrongPassword();

        void goToPinView(final String email, final String password);

        void changeButtonState(boolean isEnabled);


    }
}
