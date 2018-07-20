package uk.co.transferx.app.recoverpass.presenter;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.tokenmanager.TokenManager;
import uk.co.transferx.app.util.Util;

/**
 * Created by sergey on 15/03/2018.
 */

public class RecoverPasswordPresenter extends BasePresenter<RecoverPasswordPresenter.RecoverPasswordUI> {

    private final TokenManager tokenManager;
    private String validEmail;
    private final SignInOutApi signInOutApi;
    private Disposable disposable;

    @Inject
    public RecoverPasswordPresenter(final TokenManager tokenManager, final SignInOutApi signInOutApi) {
        this.tokenManager = tokenManager;
        this.signInOutApi = signInOutApi;
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void validateEmail(String email) {
        if (!Util.validateEmail(email)) {
            ui.lockButton();
            return;
        }
        validEmail = email;
        ui.unlockButton();
    }

    public void sendEmail() {
        disposable = tokenManager.getToken()
                .flatMap(token -> signInOutApi.forgotEmail(token.getAccessToken(), validEmail))
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (ui != null && resp.code() == HttpsURLConnection.HTTP_OK) {
                        ui.goBackToMain();
                        return;
                    }
                    handleError(new Throwable(resp.message()));
                }, this::handleError);
    }

    public void goBack() {
        ui.goBackToMain();
    }

    public void handleError(Throwable throwable){
        if(ui != null){
            ui.error();
        }
    }

    public interface RecoverPasswordUI extends UI {

        void unlockButton();

        void successGoBack();

        void lockButton();

        void goBackToMain();

        void error();

    }
}
