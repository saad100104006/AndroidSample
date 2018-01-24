package uk.co.transferx.app.signin.presenters;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.pojo.UserRequest;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 27/12/2017.
 */

public class SignInEmailPresenter extends BasePresenter<SignInEmailPresenter.SignInEmailUI> {

    private final SignInOutApi signInOutApi;
    private Disposable disposable;
    private final TokenManager tokenManager;


    @Override
    public void attachUI(SignInEmailUI ui) {
        super.attachUI(ui);
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Inject
    public SignInEmailPresenter(final SignInOutApi signInOutApi, final TokenManager tokenManager) {
        this.signInOutApi = signInOutApi;
        this.tokenManager = tokenManager;
    }


    public void validateInput(String email, String password) {

      if (!tokenManager.isInitialTokenExist()) {
            ui.showError();
            return;
        }
        if (!Util.validateEmail(email)) {
            ui.showValidationErrorEmail();
            return;
        }
        if (!Util.validatePassword(password)) {
            ui.showValidationErrorPassword();
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
                    }
                    if (ui != null) {
                        ui.showError();
                    }

                }, this::handleError);

    }


    private void handleError(Throwable throwable) {
        if (ui != null) {
            ui.showError();
        }
    }

    public interface SignInEmailUI extends UI {

        void goToMainScreen();

        void showError();

        void showValidationErrorEmail();

        void showValidationErrorPassword();

    }
}
