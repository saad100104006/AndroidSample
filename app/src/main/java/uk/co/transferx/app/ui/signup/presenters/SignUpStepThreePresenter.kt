package uk.co.transferx.app.ui.signup.presenters

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import uk.co.transferx.app.data.crypto.CryptoManager
import uk.co.transferx.app.data.remote.SignUpApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.Constants.*
import javax.inject.Inject

/**
 * Created by sergey on 06.12.17.
 * Refactored and redesigned by Catalin Ghita on 10/2018
 */

class SignUpStepThreePresenter @Inject
constructor(private val cryptoManager: CryptoManager, sharedPreferences: SharedPreferences)
    : BasePresenter<SignUpStepThreePresenter.SignUpStepThreeUI>(sharedPreferences) {

    private var compositeDisposable: CompositeDisposable? = null
    private var firstPin: String? = null
    private var secondPin: String? = null

    private val isPinFilled: Boolean
        get() = firstPin != null && secondPin != null &&
                firstPin?.length == PIN_SIZE.toInt() && secondPin?.length == PIN_SIZE.toInt()

    override fun detachUI() {
        super.detachUI()
        compositeDisposable?.dispose()
    }


    fun setFirstPin(firstPin: String) {
        this.firstPin = firstPin
        validateInputs()
    }

    fun setSecondPin(secondPin: String) {
        this.secondPin = secondPin
        validateInputs()
    }

    fun signUpPin() {
        if (firstPin == secondPin) {
            // Store encrypted PIN
            val firstName = sharedPreferences.getString(FIRST_NAME, FIRST_NAME)
            val secondName = sharedPreferences.getString(LAST_NAME, LAST_NAME)
            val encryptedCredential =
                    cryptoManager.getEncryptedCredential(firstName + UNDERSCORE + secondName, firstPin)
            sharedPreferences.edit().putString(CREDENTIAL, encryptedCredential).apply()

            sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, false).apply()
            sharedPreferences.edit().putBoolean(PIN_REQUIRED, false).apply()

            if(shouldGoToConfirmation()) ui?.goToConfirmationScreen()
            else ui?.goToMainScreen()
        } else ui.showErrorPin()

    }

    private fun validateInputs() {
        ui?.setButtonEnabled(isPinFilled)
    }

    private fun shouldGoToConfirmation(): Boolean {
        return sharedPreferences.getBoolean(CARD_REQUIRED, true)
                || sharedPreferences.getBoolean(RECIPIENT_REQUIRED, true)
    }

    interface SignUpStepThreeUI : UI {

        fun goToMainScreen()

        fun showErrorPin()

        fun showErrorFromBackend()

        fun setButtonEnabled(isEnabled: Boolean)

        fun goToConfirmationScreen()
    }

}
