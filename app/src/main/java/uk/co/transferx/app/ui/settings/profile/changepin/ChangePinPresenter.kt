package uk.co.transferx.app.ui.settings.profile.changepin

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.data.crypto.CryptoManager
import uk.co.transferx.app.data.pojo.ChangePassword
import uk.co.transferx.app.data.pojo.ChangePin
import uk.co.transferx.app.data.remote.ProfileApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.ui.signinpin.presenter.SignInPinPresenter
import uk.co.transferx.app.util.Constants
import uk.co.transferx.app.util.Util
import uk.co.transferx.app.util.schedulers.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Created by HP on 11/1/2018.
 */


class ChangePinPresenter @Inject
constructor(private val tokenManager: TokenManager,
            private val profileApi: ProfileApi,
            sharedPreferences: SharedPreferences)
    : BasePresenter<ChangePinPresenter.ChangePinUI>(sharedPreferences) {

    private var disposable: Disposable? = null
    private var currentPin: String = Constants.EMPTY
    private var newPin: String = Constants.EMPTY
    private var confirmPin: String = Constants.EMPTY

    private var pinEnteredValue: String? = null

    private val isPinFilled: Boolean
        get() = pinEnteredValue != null && pinEnteredValue!!.matches("^[0-9]*$".toRegex()) &&
                pinEnteredValue?.length == Constants.PIN_SIZE.toInt()


//    private var disposable: Disposable? = null


    override fun detachUI() {
        super.detachUI()
        if (disposable != null && disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    fun setCurrentPin(currentPassword: String) {
        currentPin = currentPassword
        ui?.setButtonEnabled(validateInput())
    }

    fun setNewPin(newPassword: String) {
        newPin = newPassword
        ui?.setButtonEnabled(validateInput())
    }

    fun setConfirmPin(confirmPassword: String) {
        confirmPin = confirmPassword
        ui?.setButtonEnabled(validateInput())
    }



    fun validateInput(): Boolean {
        return Util.validatePassword(currentPin) && Util.validatePassword(newPin) &&
                Util.validatePassword(confirmPin) &&
                currentPin != newPin && newPin == confirmPin
    }

    fun saveNewPin() {
        disposable = tokenManager.token
                .flatMap {
                    profileApi.changePin(
                            it.accessToken,
                            changePin= ChangePin(currentPin, newPin)
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ui?.goToAccount() }, { globalErrorHandler(it) })
    }


    fun setPinValue(pinEnteredValue: String){
        this.pinEnteredValue = pinEnteredValue
        this.ui?.setButtonEnabled(isPinFilled)
    }



    fun clickAccount() {
        ui?.goToAccount()
    }



    interface ChangePinUI : UI {

        fun setButtonEnabled(enabled: Boolean)

        fun goToAccount()

        fun showErrorPin()


    }
}
