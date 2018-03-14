package uk.co.transferx.app.welcom.presenter;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.pojo.UserRequest;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

/**
 * Created by smilevkiy on 14.11.17.
 */

public class WelcomeFragmentPresenter extends BasePresenter<WelcomeFragmentPresenter.WelcomeUI> {

    private final SignInOutApi signInOutApi;
    private final SignUpApi signUpApi;
    private Disposable disposable;
    private final TokenManager tokenManager;

    @Inject
    public WelcomeFragmentPresenter(final SignInOutApi signInOutApi, final SignUpApi signUpApi, final TokenManager tokenManager) {
        this.signInOutApi = signInOutApi;
        this.tokenManager = tokenManager;
        this.signUpApi = signUpApi;
    }


    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void validateInput(String email, String password) {
        if (!tokenManager.isInitialTokenExist()) {
            ui.showConnectionError();
            return;
        }
        if (!Util.validateEmail(email)) {
            ui.showEmailError();
            return;
        }
        if (!Util.validatePassword(password)) {
            ui.showPasswordError();
            return;
        }
        signIn(email, password, tokenManager.getInitialToken());
    }

    private void signIn(String email, String password, String token) {
        UserRequest.Builder request = new UserRequest.Builder();
        disposable = signInOutApi.signIn(token, request.uname(email).upass(password).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        tokenManager.setToken(resp.body().string());
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
        if (tokenManager.isInitialTokenExist()) {
            ui.goToSignUp();
            return;
        }
        ui.showConnectionError();
    }

    public void refreshToken() {
        disposable = signUpApi.getInitialToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        tokenManager.setInitialToken(resp.body().string());
                        return;
                    }
                    ui.showConnectionError();
                }, throwable -> ui.showConnectionError());
    }

    private void handleError(Throwable throwable) {
        ui.showConnectionError();
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

    }
}
