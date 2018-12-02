package uk.co.transferx.app.ui.settings.profile.changepin.presenter

import android.content.SharedPreferences
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import uk.co.transferx.app.data.crypto.CryptoManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.Constants
import uk.co.transferx.app.util.schedulers.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Created by Tanvir on 11/1/2018.
 */


class ChangePinPresenter @Inject
constructor(private val cryptoManager: CryptoManager, private val schedulerProvider: BaseSchedulerProvider,
            sharedPreferences: SharedPreferences)
    : BasePresenter<ChangePinPresenter.ChangePinUI>(sharedPreferences) {

    private var compositeDisposable: CompositeDisposable? = null
    private var currentPin: String = Constants.EMPTY
    private var newPin: String = Constants.EMPTY
    private var confirmPin: String = Constants.EMPTY
    private var pinEnteredValue: String? = null
    private val isPinFilled: Boolean
        get() = currentPin != null && newPin != null && confirmPin != null &&
                currentPin?.length == Constants.PIN_SIZE.toInt() && newPin?.length == Constants.PIN_SIZE.toInt() && confirmPin?.length == Constants.PIN_SIZE.toInt()

    override fun attachUI(ui: ChangePinUI) {
        super.attachUI(ui)
        compositeDisposable = CompositeDisposable()
    }

    override fun detachUI() {
        super.detachUI()
        if (compositeDisposable != null && compositeDisposable!!.isDisposed) {
            compositeDisposable!!.dispose()
        }
    }

    fun setCurrentPin(currentPassword: String) {
        this.currentPin = currentPassword
        validateInputs()
    }

    fun setNewPin(newPassword: String) {
        this.newPin = newPassword
        validateInputs()
    }

    fun setConfirmPin(confirmPassword: String) {
        this.confirmPin = confirmPassword
        validateInputs()
    }

    fun setPinValue(pinEnteredValue: String){
        this.pinEnteredValue = pinEnteredValue
        this.ui?.setButtonEnabled(isPinFilled)
    }

    fun clickAccount() {
        ui?.goToAccount()
    }

    private fun getObservableWithCrypto(credential: String, pin: String): Single<String> {
        return Single.just ( cryptoManager.getDecryptCredential(credential, pin) )
    }


    fun checkOldPinAndChangePin() {

        if(newPin==confirmPin) {
            val credential = sharedPreferences.getString(Constants.CREDENTIAL, null)
            if (credential == null) {
                Timber.d(javaClass.simpleName + " Credential is null")
                return
            }
            compositeDisposable!!.add(getObservableWithCrypto(credential, currentPin)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe { res ->
                        if (!res.isEmpty()) {
                            val firstName = sharedPreferences.getString(Constants.FIRST_NAME, Constants.FIRST_NAME)
                            val secondName = sharedPreferences.getString(Constants.LAST_NAME, Constants.LAST_NAME)
                            val encryptedCredential =
                                    cryptoManager.getEncryptedCredential(firstName + Constants.UNDERSCORE + secondName, newPin)
                            sharedPreferences.edit().putString(Constants.CREDENTIAL, encryptedCredential).apply()
                            sharedPreferences.edit().putBoolean(Constants.PIN_REQUIRED, false).apply()
                            sharedPreferences.edit().putBoolean(Constants.LOGGED_IN_STATUS, true).apply()
                            this.ui.goToPreviousScreen()
                            return@subscribe
                        }
                    })
        }else ui.showErrorPin()
    }

    private fun validateInputs() {
        ui?.setButtonEnabled(isPinFilled)
    }

    interface ChangePinUI : UI {

        fun goToPreviousScreen()

        fun setButtonEnabled(enabled: Boolean)

        fun goToAccount()

        fun showErrorPin()


    }
}
