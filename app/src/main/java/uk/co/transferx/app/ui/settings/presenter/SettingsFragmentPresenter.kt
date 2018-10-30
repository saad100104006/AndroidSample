package uk.co.transferx.app.ui.settings.presenter

import android.content.SharedPreferences

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager

import uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS

/**
 * Created by sergey on 29/12/2017.
 */

class SettingsFragmentPresenter @Inject
constructor(private val signInOutApi: SignInOutApi, private val tokenManager: TokenManager, private val sharedPreference: SharedPreferences) : BasePresenter<SettingsFragmentPresenter.SettingsFragmentUI>() {
    private var disposable: Disposable? = null


    override fun detachUI() {
        super.detachUI()
        if (disposable != null && disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    fun logOut() {
        /*   if (ui != null && !tokenManager.isTokenExist()) {
            tokenManager.clearInitToken();
            sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
            ui.goToWelcome();
            return;
        } */
        disposable = tokenManager.token
                .flatMap<Response<ResponseBody>> { (accessToken) -> signInOutApi.logOut(accessToken) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { res ->
                    if (ui != null) {
                        tokenManager.clearToken()
                     //   sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply()
                        ui.goToWelcome()
                    }
                }
    }

    fun clickNotification() {
            ui?.goAppSettings()
    }

    fun supportClicked() {
        ui?.goToSupport()
    }

    fun clickProfile() {
            ui?.goToProfile()
    }

    interface SettingsFragmentUI : UI {

        override fun goToWelcome()

        fun goAppSettings()

        fun goToProfile()

        fun goToSupport()
    }
}
