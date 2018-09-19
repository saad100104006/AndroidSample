package uk.co.transferx.app.ui.signin.presenters;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.data.remote.SignInOutApi;
import uk.co.transferx.app.data.pojo.UserSignIn;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;
import uk.co.transferx.app.data.repository.tokenmanager.TokenRepository;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 27/12/2017.
 */

public class SignInEmailPresenter extends BasePresenter<SignInEmailPresenter.SignInEmailUI> {

    private final SignInOutApi signInOutApi;
    private Disposable disposable;
    private final TokenManager tokenManager;
    private final TokenRepository tokenRepository;

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
    public SignInEmailPresenter(final SignInOutApi signInOutApi, final TokenManager tokenManager,
                                final TokenRepository tokenRepository) {
        this.signInOutApi = signInOutApi;
        this.tokenManager = tokenManager;
        this.tokenRepository = tokenRepository;
    }


    public void validateInput(String email, String password) {

        if (tokenRepository.getToken().getAccessToken().isEmpty()) {
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

        signIn(email, password);
    }

    private void signIn(String email, String password) {
        UserSignIn.Builder signIn = new UserSignIn.Builder();
        disposable = tokenManager.getToken()
                .flatMap(token -> signInOutApi.signIn(token.getAccessToken(), signIn.uname(email).upass(password).build()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        tokenManager.saveToken(resp.body());
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
