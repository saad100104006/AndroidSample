package uk.co.transferx.app.settings.presenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.tokenmanager.TokenManager;

/**
 * Created by sergey on 29/12/2017.
 */

public class SettingsFragmentPresenter extends BasePresenter<SettingsFragmentPresenter.SettingsFragmentUI> {

    private final SignInOutApi signInOutApi;
    private Disposable disposable;
    private final TokenManager tokenManager;

    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Inject
    public SettingsFragmentPresenter(final SignInOutApi signInOutApi, final TokenManager tokenManager) {
        this.signInOutApi = signInOutApi;
        this.tokenManager = tokenManager;
    }

    public void logOut() {
        if (ui != null && !tokenManager.isTokenExist()) {
            tokenManager.clearInitToken();
            ui.goToWelcome();
            return;
        }
        disposable = signInOutApi.logOut(tokenManager.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (ui != null) {
                        tokenManager.clearToken();
                        tokenManager.clearInitToken();
                        ui.goToWelcome();
                    }
                });


    }

    public interface SettingsFragmentUI extends UI {

        void goToWelcome();
    }
}
