package uk.co.transferx.app.signup.presenters;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.crypto.CryptoManager;
import uk.co.transferx.app.pojo.UserRequest;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static uk.co.transferx.app.util.Constants.CREDENTIAL;
import static uk.co.transferx.app.util.Constants.EMAIL;
import static uk.co.transferx.app.util.Constants.SPACE;

/**
 * Created by sergey on 06.12.17.
 */

public class SignUpStepThreePresenter extends BasePresenter<SignUpStepThreePresenter.SignUpStepThreeUI> {

    private final CryptoManager cryptoManager;
    private final SharedPreferences sharedPreferences;
    private final SignUpApi signUpApi;
    private final TokenManager tokenManager;
    private Disposable disposable;
    private String uname, email, password;

    @Inject
    public SignUpStepThreePresenter(final CryptoManager cryptoManager, final SharedPreferences sharedPreferences, final SignUpApi signUpApi, final TokenManager tokenManager) {
        this.cryptoManager = cryptoManager;
        this.sharedPreferences = sharedPreferences;
        this.signUpApi = signUpApi;
        this.tokenManager = tokenManager;
    }

    @Override
    public void attachUI(SignUpStepThreeUI ui) {
        super.attachUI(ui);
    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void setCredential(String uname, String email, String password) {
        this.uname = uname;
        this.email = email;
        this.password = password;
    }

    public void validatePin(final String firstPin, final String secondPin) {
        UserRequest.Builder request = new UserRequest.Builder();
        if (firstPin.equals(secondPin)) {
            disposable = signUpApi.registerUser(tokenManager.getInitialToken(), request.uname(uname).email(email).upass(password).upassConfirmation(password).build())
                    .doOnNext(respo -> {
                        if (respo.code() == HttpsURLConnection.HTTP_OK) {
                            sharedPreferences.edit().putString(CREDENTIAL, cryptoManager.getEncryptedCredential(String.format("%s%s%s", email, SPACE, password), firstPin)).apply();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                            tokenManager.setToken(resp.body().string());
                            tokenManager.clearInitToken();
                            ui.goToMainScreen();
                            return;
                        }
                        if (resp.code() == HttpsURLConnection.HTTP_BAD_REQUEST && ui != null) {
                            String message = resp.errorBody().string();
                            if (message != null && message.contains(EMAIL) && ui != null) {
                                ui.showErrorFromBackend();
                            }
                        }

                    }, this::handleErrorFromBackend);
            return;
        }
        if (ui != null) {
            ui.showErrorPin();
        }
    }

    private void handleErrorFromBackend(Throwable throwable) {

    }

    public interface SignUpStepThreeUI extends UI {

        void goToMainScreen();

        void showErrorPin();

        void showErrorFromBackend();

    }
}
