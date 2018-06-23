package uk.co.transferx.app.signup.presenters;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.crypto.CryptoManager;
import uk.co.transferx.app.pojo.UserRequest;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static uk.co.transferx.app.util.Constants.CREDENTIAL;
import static uk.co.transferx.app.util.Constants.EMAIL;
import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;
import static uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT;
import static uk.co.transferx.app.util.Constants.SPACE;
import static uk.co.transferx.app.util.Constants.TOKEN;
import static uk.co.transferx.app.util.Constants.UNDERSCORE;

/**
 * Created by sergey on 06.12.17.
 */

public class SignUpStepThreePresenter extends BasePresenter<SignUpStepThreePresenter.SignUpStepThreeUI> {

    private final static short FIRST_NAME = 0;
    private final static short LAST_NAME = 1;
    private final static short PIN_SIZE = 4;
    private final CryptoManager cryptoManager;
    private final SharedPreferences sharedPreferences;
    private final SignUpApi signUpApi;
    private final TokenManager tokenManager;
    private String uname, email, password;
    private CompositeDisposable compositeDisposable;
    private String firstPin, secondPin;


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
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public void setCredential(String uname, String email, String password) {
        this.uname = uname;
        this.email = email;
        this.password = password;
    }

    private void saveTokenWithNewPin(String pin) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(Single.fromCallable(() -> cryptoManager.getEncryptedCredential(email + UNDERSCORE + password, pin))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sec -> {
                    sharedPreferences.edit().putString(CREDENTIAL, sec).apply();
                    sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, false).apply();
                    sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, true).apply();
                    if (ui != null) {
                        ui.goToMainScreen();
                    }
                })
        );
    }

    public void setFirstPin(String firstPin) {
        this.firstPin = firstPin;
        validateInputs();
    }

    public void setSecondPin(String secondPin) {
        this.secondPin = secondPin;
        validateInputs();
    }

    private boolean isPinFilled() {
        return firstPin != null && secondPin != null &&
                firstPin.length() == PIN_SIZE && secondPin.length() == PIN_SIZE;
    }

    private void validateInputs() {
        if (ui != null) {
            ui.setButtonEnabled(isPinFilled());
        }
    }

    public void validatePin() {
        UserRequest.Builder request = new UserRequest.Builder();
        if (firstPin.equals(secondPin)) {
            if (sharedPreferences.getBoolean(PIN_SHOULD_BE_INPUT, false) ||
                    !sharedPreferences.getBoolean(LOGGED_IN_STATUS, false) &&
                            tokenManager.getToken() != null) {
                saveTokenWithNewPin(firstPin);
                return;
            }
            String[] firstNameAndLastName = uname.split(UNDERSCORE);
            Disposable disposable = signUpApi.registerUser(tokenManager.getInitialToken(),
                    request.firstName(firstNameAndLastName[FIRST_NAME])
                            .lastName(firstNameAndLastName[LAST_NAME])
                            .email(email)
                            .upass(password).build())
                    .doOnSuccess(resp -> {
                        if (resp.code() == HttpsURLConnection.HTTP_OK) {
                           final String encCred = cryptoManager.getEncryptedCredential(email + UNDERSCORE + password, firstPin);
                           sharedPreferences.edit().putString(CREDENTIAL, encCred).apply();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                            String token = resp.body().getToken();
                            SharedPreferences.Editor editorShared = sharedPreferences.edit();
                            editorShared.putBoolean(LOGGED_IN_STATUS, true).apply();
                            tokenManager.setToken(token);
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
            if (compositeDisposable == null) {
                compositeDisposable = new CompositeDisposable();
            }
            compositeDisposable.add(disposable);
            return;
        }
        if (ui != null) {
            ui.showErrorPin();
        }
    }

    private void handleErrorFromBackend(Throwable throwable) {
        Timber.e(getClass().getName(), throwable);

    }

    public interface SignUpStepThreeUI extends UI {

        void goToMainScreen();

        void showErrorPin();

        void showErrorFromBackend();

        void setButtonEnabled(boolean isEnabled);

    }
}
