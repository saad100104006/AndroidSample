package uk.co.transferx.app.signin.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.crypto.CryptoManager;
import uk.co.transferx.app.errors.ErrorPinException;
import uk.co.transferx.app.pojo.UserSignIn;
import uk.co.transferx.app.recipientsrepository.RecipientRepository;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static uk.co.transferx.app.util.Constants.CREDENTIAL;
import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;
import static uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT;
import static uk.co.transferx.app.util.Constants.UNDERSCORE;

/**
 * Created by sergey on 19/03/2018.
 */

public class SignInPinPresenter extends BasePresenter<SignInPinPresenter.SignInPinUI> {

    private final CryptoManager cryptoManager;
    private final SignInOutApi signInOutApi;
    private final SharedPreferences sharedPreferences;
    private final TokenManager tokenManager;
    private final RecipientRepository recipientRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public SignInPinPresenter(final CryptoManager cryptoManager,
                              final SignInOutApi signInOutApi,
                              final SharedPreferences sharedPreferences,
                              final TokenManager tokenManager,
                              final RecipientRepository recipientRepository) {
        this.cryptoManager = cryptoManager;
        this.signInOutApi = signInOutApi;
        this.sharedPreferences = sharedPreferences;
        this.tokenManager = tokenManager;
        this.recipientRepository = recipientRepository;
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

    private Single<String> getObservableWithCrypto(final String credential, final String pin) {
        return Single.fromCallable(() -> cryptoManager.getDecryptCredential(credential, pin));
    }


    public void checkPingAndLogIn(final String pin) {

        final String credential = sharedPreferences.getString(CREDENTIAL, null);
        if (credential == null) {
            Timber.d(getClass().getSimpleName()+" Credential is null");
            return;
        }
        compositeDisposable.add(getObservableWithCrypto(credential, pin)
                .flatMap(cred -> {
                    if (cred.isEmpty()) {
                        return Single.error(new ErrorPinException());
                    }
                    final String[] emailAndPass = cred.split(UNDERSCORE);
                    final String email = emailAndPass[0];
                    final String password = emailAndPass[1];
                    UserSignIn.Builder request = new UserSignIn.Builder();
                    return signInOutApi.signIn(tokenManager.getInitialToken(), request.uname(email).upass(password).build());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (ui != null && resp.code() == HttpsURLConnection.HTTP_OK) {
                        tokenManager.setToken(resp.body().getToken());
                        ui.goToMainScree();
                    }
                }, this::handleError));

    }

    public void resetPassword() {
        sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
        sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, true).apply();
        recipientRepository.clearRecipients();
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
