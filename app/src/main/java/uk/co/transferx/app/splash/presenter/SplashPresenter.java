package uk.co.transferx.app.splash.presenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.firebase.SubscriptionManager;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 19.11.17.
 */

public class SplashPresenter extends BasePresenter<SplashPresenter.SplashUI> {


    private static final long DELAY = 800;

    private Disposable dis;

    private final SignUpApi signUpApi;
    private final TokenManager tokenManager;


    @Inject
    public SplashPresenter(final SignUpApi signUpApi, final TokenManager tokenManager, final SubscriptionManager subscriptionManager) {
        this.signUpApi = signUpApi;
        this.tokenManager = tokenManager;
        this.tokenManager.clearToken();
        this.tokenManager.clearInitToken();
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
                    if (res.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        tokenManager.setInitialToken(res.body().string());
                        ui.goToWelcomeScreen();
                        return;
                    }
                    if (ui != null) {
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

    }
}
