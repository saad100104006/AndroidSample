package uk.co.transferx.app.ui.splash.presenter;

import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.ui.base.BasePresenter;
import uk.co.transferx.app.ui.base.UI;
import uk.co.transferx.app.data.remote.SignUpApi;
import uk.co.transferx.app.data.firebase.SubscriptionManager;
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager;
import uk.co.transferx.app.data.repository.tokenmanager.TokenRepository;

import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;

/**
 * Created by sergey on 19.11.17.
 */

public class SplashPresenter extends BasePresenter<SplashPresenter.SplashUI> {


    private static final long DELAY = 800;

    private Disposable dis;

    private final SignUpApi signUpApi;
    private final TokenManager tokenManager;
    private final SharedPreferences sharedPreferences;
    private final TokenRepository tokenRepository;


    @Inject
    public SplashPresenter(final SignUpApi signUpApi, final TokenManager tokenManager,
                           final SubscriptionManager subscriptionManager,
                           final SharedPreferences sharedPreferences,
                           final TokenRepository tokenRepository) {
        this.signUpApi = signUpApi;
        this.tokenManager = tokenManager;
        this.sharedPreferences = sharedPreferences;
        this.tokenRepository = tokenRepository;
        subscriptionManager.initSubscribtions();
    }


    @Override
    public void attachUI(SplashUI ui) {
        super.attachUI(ui);
        dis = signUpApi.getInitialToken()
                .delay(DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    boolean loggedInStatus = sharedPreferences.getBoolean(LOGGED_IN_STATUS, false);
                    if (res.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        if (tokenManager.shouldSaveGenesis()) {
                            tokenManager.saveToken(res.body());
                        }
                        if (loggedInStatus) {
                            ui.goToPinScreen();
                            return;
                        }
                        ui.goToWelcomeScreen();
                        return;
                    }
                    if (ui != null) {
                        if (loggedInStatus) {
                            ui.goToPinScreen();
                            return;
                        }
                        ui.goToWelcomeScreen();
                    }
                }, this::handleError);
    }


    private void handleError(Throwable throwable) {
        ui.goToWelcomeScreen();
    }


    @Override
    public void detachUI() {
        if (dis != null && !dis.isDisposed()) {
            dis.dispose();
        }
        super.detachUI();
    }

    public interface SplashUI extends UI {

        void goToWelcomeScreen();

        void goToPinScreen();

    }
}
