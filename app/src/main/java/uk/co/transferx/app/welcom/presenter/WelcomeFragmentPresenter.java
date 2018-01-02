package uk.co.transferx.app.welcom.presenter;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignUpApi;

/**
 * Created by smilevkiy on 14.11.17.
 */

public class WelcomeFragmentPresenter extends BasePresenter<WelcomeFragmentPresenter.WelcomeUI> {

    private boolean isChecked;
    private boolean isTokenPresent;
    private final SignUpApi signUpApi;
    private Disposable disposable;

    @Inject
    public WelcomeFragmentPresenter(final SignUpApi signUpApi) {
        this.signUpApi = signUpApi;
    }

    @Override
    public void attachUI(WelcomeUI ui) {
        super.attachUI(ui);
        if (!isChecked) {
            ui.checkToken();
            isChecked = true;
        }

    }

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void setTokenStatus(boolean isPresent) {
        this.isTokenPresent = isPresent;
    }

    public void signInClicked() {
        if (isTokenPresent) {
            ui.goToSignIn();
            return;
        }
        ui.noTokenError();
    }

    public void signUpClicked() {
        if (isTokenPresent) {
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
                        ui.upDateToken(resp.body().string());
                        setTokenStatus(true);
                        return;
                    }
                    ui.noTokenError();
                }, throwable -> ui.noTokenError());
    }

    private void handleError(Throwable throwable) {

    }


    public interface WelcomeUI extends UI {
        void checkToken();

        void goToSignUp();

        void goToSignIn();

        void noTokenError();

        void upDateToken(String token);

    }
}
