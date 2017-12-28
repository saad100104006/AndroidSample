package uk.co.transferx.app.signin.presenters;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInApi;
import uk.co.transferx.app.pojo.UserRequest;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 27/12/2017.
 */

public class SignInEmailPresenter extends BasePresenter<SignInEmailPresenter.SignInEmailUI> {

    private final SignInApi signInApi;
    private Disposable disposable;


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
    public SignInEmailPresenter(SignInApi signInApi) {
        this.signInApi = signInApi;
    }


    public void validateInput(String email, String password, String token) {
        if (!Util.validateEmail(email)) {
            ui.showValidationErrorEmail();
            return;
        }
        if (!Util.validatePassword(password)) {
            ui.showValidationErrorPassword();
            return;
        }

        signIn(email, password, token);
    }

    private void signIn(String email, String password, String token) {
        UserRequest.Builder request = new UserRequest.Builder();
        disposable = signInApi.signIn(token, request.email(email).upass(password).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
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
