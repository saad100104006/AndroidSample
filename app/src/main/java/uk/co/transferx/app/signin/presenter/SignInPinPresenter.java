package uk.co.transferx.app.signin.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.crypto.CryptoManager;
import uk.co.transferx.app.errors.ErrorPinException;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;
import static uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT;
import static uk.co.transferx.app.util.Constants.TOKEN;

/**
 * Created by sergey on 19/03/2018.
 */

public class SignInPinPresenter extends BasePresenter<SignInPinPresenter.SignInPinUI> {

    private final CryptoManager cryptoManager;
    private final SignInOutApi signInOutApi;
    private final SharedPreferences sharedPreferences;
    private final TokenManager tokenManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public SignInPinPresenter(final CryptoManager cryptoManager,
                              final SignInOutApi signInOutApi,
                              final SharedPreferences sharedPreferences,
                              final TokenManager tokenManager) {
        this.cryptoManager = cryptoManager;
        this.signInOutApi = signInOutApi;
        this.sharedPreferences = sharedPreferences;
        this.tokenManager = tokenManager;
    }

    @Override
    public void attachUI(SignInPinUI ui) {
        super.attachUI(ui);
    }

    @Override
    public void detachUI() {
        super.detachUI();
        compositeDisposable.dispose();
    }

    private Observable<String> getObservableWithCrypto(String credential, String pin) {
        return Observable.just(cryptoManager.getDecryptCredential(credential, pin));
    }

    public void checkPingAndLogIn(final String pin) {
        final String credential = sharedPreferences.getString(TOKEN, null);
        if (credential != null) {
            getObservableWithCrypto(credential, pin)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (resp.isEmpty()) {
                            throw new ErrorPinException();
                        }
                        if (ui != null) {
                            tokenManager.setToken(resp);
                            ui.goToMainScree();
                        }
                    }, this::handleError);
        }
    }

    public void resetPassword() {
        sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
        sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, true).apply();
        if (ui != null) {
            ui.goToWelcomeScreen();
        }
    }

    private void handleError(Throwable throwable) {
        if (ui != null) {
            if (throwable instanceof ErrorPinException) {
                ui.showErrorPin();
            }
            ui.showError();
        }
    }

    public interface SignInPinUI extends UI {

        void goToMainScree();

        void goToWelcomeScreen();

        void showError();

        void showErrorPin();
    }
}
