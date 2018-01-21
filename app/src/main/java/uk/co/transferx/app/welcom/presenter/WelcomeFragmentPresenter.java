package uk.co.transferx.app.welcom.presenter;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by smilevkiy on 14.11.17.
 */

public class WelcomeFragmentPresenter extends BasePresenter<WelcomeFragmentPresenter.WelcomeUI> {

    private final SignUpApi signUpApi;
    private Disposable disposable;
    private final TokenManager tokenManager;

    @Inject
    public WelcomeFragmentPresenter(final SignUpApi signUpApi, final TokenManager tokenManager) {
        this.signUpApi = signUpApi;
        this.tokenManager = tokenManager;
    }


    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    public void signInClicked() {
//        if (tokenManager.isInitialTokenExist()) { TODO: should uncomment when server will be reachable
        ui.goToSignIn();
//            return;
//        }
//        ui.noTokenError();
    }

    public void signUpClicked() {
        if (tokenManager.isInitialTokenExist()) {
            ui.goToSignUp();
            return;
        }
        ui.noTokenError();
    }

    public void refreshToken() {
        disposable = signUpApi.getInitialToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        tokenManager.setInitialToken(resp.body().string());
                        return;
                    }
                    ui.noTokenError();
                }, throwable -> ui.noTokenError());
    }

    private void handleError(Throwable throwable) {

    }


    public interface WelcomeUI extends UI {

        void goToSignUp();

        void goToSignIn();

        void noTokenError();

    }
}
