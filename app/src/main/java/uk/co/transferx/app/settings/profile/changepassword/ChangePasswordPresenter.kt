package uk.co.transferx.app.settings.profile.changepassword

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.api.ProfileApi
import uk.co.transferx.app.pojo.ChangePassword
import uk.co.transferx.app.tokenmanager.TokenManager
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Util
import javax.inject.Inject

class ChangePasswordPresenter
@Inject constructor(private val tokenManager: TokenManager,
                    private val profileApi: ProfileApi) :
        BasePresenter<ChangePasswordPresenter.ChangePasswordUI>() {

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
        disposable = profileApi.changePassword(tokenManager.token,
                changePassword = ChangePassword(currentPass, newPass))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ -> ui?.goBackToSettings() }, { throwable -> handleError(throwable) })
    }

    private fun handleError(throwable: Throwable) {

    }

    interface ChangePasswordUI : UI {
        fun setButtonEnabled(enabled: Boolean)

        fun goBackToSettings()
    }
}