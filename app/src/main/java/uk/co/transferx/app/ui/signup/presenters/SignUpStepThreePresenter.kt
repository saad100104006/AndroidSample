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
 */

class SignUpStepThreePresenter @Inject
constructor(private val cryptoManager: CryptoManager, sharedPreferences: SharedPreferences,
            private val signUpApi: SignUpApi, private val tokenManager: TokenManager)
    : BasePresenter<SignUpStepThreePresenter.SignUpStepThreeUI>(sharedPreferences) {
    private var uname: String? = null
    private var email: String? = null
    private var password: String? = null
    private var phoneNumber: String? = null
    private var country: String? = null

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

    fun setCredential(uname: String, email: String, password: String, phoneNumber: String, country: String) {
        this.uname = uname
        this.email = email
        this.password = password
        this.phoneNumber = phoneNumber
        this.country = country
    }

    fun setFirstPin(firstPin: String) {
        this.firstPin = firstPin
        validateInputs()
    }

    fun setSecondPin(secondPin: String) {
        this.secondPin = secondPin
        validateInputs()
    }

    fun signUpUser() {
        if (firstPin == secondPin) {
            if (sharedPreferences.getBoolean(PIN_SHOULD_BE_INPUT, false) || !sharedPreferences.getBoolean(LOGGED_IN_STATUS, false)) {
                saveTokenWithNewPin(firstPin?: return)
                return
            }
        } else ui.showErrorPin()
        /*   UserRequest.Builder request = new UserRequest.Builder();
        if (firstPin.equals(secondPin)) {
            if (sharedPreferences.getBoolean(PIN_SHOULD_BE_INPUT, false) ||
                    !sharedPreferences.getBoolean(LOGGED_IN_STATUS, false) &&
                            tokenManager.getToken() != null) {
                saveTokenWithNewPin(firstPin);
                return;
            }
            String[] firstNameAndLastName = uname.split(UNDERSCORE);
            Disposable disposable = signUpApi.registerUser(tokenManager.getInitialToken(),
                    request.firstName(firstNameAndLastName[FIRST_NAME])
                            .lastName(firstNameAndLastName[LAST_NAME])
                            .email(email)
                            .upass(password).build())
                    .doOnSuccess(resp -> {
                        if (resp.code() == HttpsURLConnection.HTTP_OK) {
                            final String encryptedCredential = cryptoManager.getEncryptedCredential(email + UNDERSCORE + password, firstPin);
                            sharedPreferences.edit().putString(CREDENTIAL, encryptedCredential).apply();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                            String token = resp.body().getToken();
                            SharedPreferences.Editor editorShared = sharedPreferences.edit();
                            editorShared.putBoolean(LOGGED_IN_STATUS, true).apply();
                            tokenManager.setToken(token);
                            tokenManager.clearInitToken();
                            ui.goToMainScreen();
                            return;
                        }
                        if (resp.code() == HttpsURLConnection.HTTP_BAD_REQUEST && ui != null) {
                            String message = resp.errorBody().string();
                            if (message != null && message.contains(EMAIL) && ui != null) {
                                ui.showErrorFromBackend();
                            }
                        }

                    }, this::handleErrorFromBackend);
            if (compositeDisposable == null) {
                compositeDisposable = new CompositeDisposable();
            }
            compositeDisposable.add(disposable);
            return;
        }
        if (ui != null) {
            ui.showErrorPin();
        }  */
    }

    private fun saveTokenWithNewPin(pin: String) {
        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()

        compositeDisposable!!.add(tokenManager.token
                .map { (accessToken) -> cryptoManager.getEncryptedCredential(accessToken, pin) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { sec ->
                    sharedPreferences.edit().putString(CREDENTIAL, sec).apply()
                    sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, false).apply()
                    sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, true).apply()
                    sharedPreferences.edit().putBoolean(PIN_REQUIRED, false).apply()

                    ui?.goToMainScreen()

                }
        )
    }

    private fun validateInputs() {
        ui?.setButtonEnabled(isPinFilled)
    }

    private fun handleErrorFromBackend(throwable: Throwable) {
        Timber.e(javaClass.name, throwable)

    }

    interface SignUpStepThreeUI : UI {

        fun goToMainScreen()

        fun showErrorPin()

        fun showErrorFromBackend()

        fun setButtonEnabled(isEnabled: Boolean)

    }

    companion object {
        private val FIRST_NAME: Short = 0
        private val LAST_NAME: Short = 1
        private val PIN_SIZE: Short = 4
    }
}
