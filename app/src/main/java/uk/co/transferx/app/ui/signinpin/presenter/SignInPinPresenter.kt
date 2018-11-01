package uk.co.transferx.app.ui.signinpin.presenter

import android.content.SharedPreferences
import android.widget.Toast

import javax.inject.Inject

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.data.crypto.CryptoManager
import uk.co.transferx.app.util.errors.ErrorPinException
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager

import uk.co.transferx.app.util.Constants.CREDENTIAL
import uk.co.transferx.app.util.Constants.LOGGED_IN_STATUS
import uk.co.transferx.app.util.Constants.PIN_REQUIRED
import uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT

/**
 * Created by sergey on 19/03/2018.
 */

class SignInPinPresenter @Inject
constructor(private val cryptoManager: CryptoManager, sharedPreferences: SharedPreferences)
    : BasePresenter<SignInPinPresenter.SignInPinUI>(sharedPreferences) {
    private var compositeDisposable: CompositeDisposable? = null

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    fun resetPassword() {
        // TBDD
        ui.goToWelcomeScreen()

        /*
        sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, false).apply();
        sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, true).apply();
        recipientRepository.clearRecipients();
        if (ui != null) {
            ui.goToWelcomeScreen();
        }
        */
    }

    private fun getObservableWithCrypto(credential: String, pin: String): Single<String> {
        return Single.fromCallable { cryptoManager.getDecryptCredential(credential, pin) }
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

    }
}
