package uk.co.transferx.app.ui.signinpin.presenter

import android.content.SharedPreferences

import javax.inject.Inject

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.crypto.CryptoManager
import uk.co.transferx.app.util.Constants.*
import uk.co.transferx.app.util.errors.ErrorPinException

import uk.co.transferx.app.util.schedulers.BaseSchedulerProvider

/**
 * Created by sergey on 19/03/2018.
 * Refactored and redesigned by Catalin Ghita on 11/2018
 */

class SignInPinPresenter @Inject
constructor(private val cryptoManager: CryptoManager, sharedPreferences: SharedPreferences,
            private val schedulerProvider: BaseSchedulerProvider)
    : BasePresenter<SignInPinPresenter.SignInPinUI>(sharedPreferences) {
    private var compositeDisposable: CompositeDisposable? = null

    private var pinEnteredValue: String? = null

    private val isPinFilled: Boolean
        get() = pinEnteredValue != null &&
                pinEnteredValue?.length == PIN_SIZE.toInt()

    override fun attachUI(ui: SignInPinUI) {
        super.attachUI(ui)
        compositeDisposable = CompositeDisposable()
    }

    override fun detachUI() {
        super.detachUI()
        compositeDisposable!!.dispose()
    }

    fun checkPinAndLogIn(pin: String) {
        val credential = sharedPreferences.getString(CREDENTIAL, null)
        if (credential == null) {
            Timber.d(javaClass.simpleName + " Credential is null")
            return
        }
        compositeDisposable!!.add(getObservableWithCrypto(credential, pin)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe { res ->
                    if (!res.isEmpty()) {
                        sharedPreferences.edit().putBoolean(PIN_REQUIRED, false).apply()
                        sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, true).apply()
                        this.ui.goToPreviousScreen()
                        return@subscribe
                    }
                    handleError(ErrorPinException())
                })
    }

    fun setPinValue(pinEnteredValue: String){
        this.pinEnteredValue = pinEnteredValue
        this.ui?.setButtonEnabled(isPinFilled)
    }


    fun resetPassword() {
        // TBDD
        this.ui?.goToWelcomeScreen()

        /*
        sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
        sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, true).apply();
        recipientRepository.clearRecipients();
            ui.goToWelcomeScreen();

        */
    }

    private fun getObservableWithCrypto(credential: String, pin: String): Single<String> {
        return Single.just ( cryptoManager.getDecryptCredential(credential, pin) )
    }


    private fun handleError(throwable: Throwable) {
            if (throwable is ErrorPinException) {
                this.ui?.showErrorPin()
                return
            }
            this.ui?.showError(throwable.message!!)
    }

    interface SignInPinUI : UI {
        fun goToPreviousScreen()

        fun goToWelcomeScreen()

        fun showError(message: String)

        fun showErrorPin()

        fun setButtonEnabled(isEnabled: Boolean)
    }
}
