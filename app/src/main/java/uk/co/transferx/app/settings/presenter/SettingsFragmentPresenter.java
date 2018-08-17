package uk.co.transferx.app.settings.presenter;

import android.content.SharedPreferences;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.transferx.app.BasePresenter;
import uk.co.transferx.app.UI;
import uk.co.transferx.app.api.SignInOutApi;
import uk.co.transferx.app.tokenmanager.TokenManager;

import static uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS;

/**
 * Created by sergey on 29/12/2017.
 */

public class SettingsFragmentPresenter extends BasePresenter<SettingsFragmentPresenter.SettingsFragmentUI> {

    private final SignInOutApi signInOutApi;
    private Disposable disposable;
    private final TokenManager tokenManager;
    private final SharedPreferences sharedPreferences;


    @Override
    public void detachUI() {
        super.detachUI();
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Inject
    public SettingsFragmentPresenter(final SignInOutApi signInOutApi, final TokenManager tokenManager, final SharedPreferences sharedPreferences) {
        this.signInOutApi = signInOutApi;
        this.tokenManager = tokenManager;
        this.sharedPreferences = sharedPreferences;
    }

    public void logOut() {
     /*   if (ui != null && !tokenManager.isTokenExist()) {
            tokenManager.clearInitToken();
            sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
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
                        sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
                        ui.goToWelcome();
                    }
                });
*/

    }

    public void clickNotification() {
        if (ui != null) {
            ui.goAppSettings();
        }
    }

    public void supportClicked(){
        if (ui != null) {
            ui.goToSupport();
        }
    }

    public void clickProfile() {
        if (ui != null) {
            ui.goToProfile();
        }
    }

    public interface SettingsFragmentUI extends UI {

        void goToWelcome();

        void goAppSettings();

        void goToProfile();

        void goToSupport();
    }
}
