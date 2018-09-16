package uk.co.transferx.app.ui.settings.profile.changepassword

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.remote.ProfileApi
import uk.co.transferx.app.data.pojo.ChangePassword
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Util
import javax.inject.Inject

class ChangePasswordPresenter
@Inject constructor(
        private val tokenManager: TokenManager,
        private val profileApi: ProfileApi,
        sharedPreferences: SharedPreferences
) :
    BasePresenter<ChangePasswordPresenter.ChangePasswordUI>(sharedPreferences) {

    private var currentPass: String = EMPTY
    private var newPass: String = EMPTY
    private var confirmPass: String = EMPTY
    private var disposable: Disposable? = null

    override fun detachUI() {
        disposable?.dispose()
        super.detachUI()
    }

    fun setCurrentPassword(currentPassword: String) {
        currentPass = currentPassword
        ui?.setButtonEnabled(validateInput())
    }

    fun setNewPassword(newPassword: String) {
        newPass = newPassword
        ui?.setButtonEnabled(validateInput())
    }

    fun setConfirmPassword(confirmPassword: String) {
        confirmPass = confirmPassword
        ui?.setButtonEnabled(validateInput())
    }

    private fun validateInput(): Boolean {
        return Util.validatePassword(currentPass) && Util.validatePassword(newPass) &&
                Util.validatePassword(confirmPass) &&
                currentPass != newPass && newPass == confirmPass
    }

    fun saveNewPassword() {
        disposable = tokenManager.token
            .flatMap {
                profileApi.changePassword(
                    it.accessToken,
                    changePassword = ChangePassword(currentPass, newPass)
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ ui?.goBackToSettings() }, { globalErrorHandler(it) })
    }


    interface ChangePasswordUI : UI {
        fun setButtonEnabled(enabled: Boolean)
        fun goBackToSettings()
    }
}