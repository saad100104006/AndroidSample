package uk.co.transferx.app.ui.signin.presenter

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import uk.co.transferx.app.data.pojo.TokenEntity
import uk.co.transferx.app.data.pojo.UserSignIn
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.data.remote.SignUpApi
import uk.co.transferx.app.data.repository.ProfileRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.data.repository.tokenmanager.TokenRepository
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.signin.SignInContract
import uk.co.transferx.app.util.Constants.*
import uk.co.transferx.app.util.Util
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

/**
 * Created by smilevkiy on 14.11.17.
 */

class SignInPresenter @Inject constructor
(private val signInOutApi: SignInOutApi, private val signUpApi: SignUpApi,
 private val tokenManager: TokenManager, sharedPreferences: SharedPreferences,
 private val profileRepository: ProfileRepository,
 private val tokenRepository: TokenRepository)
    : BasePresenter<SignInContract.View>(sharedPreferences), SignInContract.Presenter {

    private var compositeDisposable: CompositeDisposable? = null

    private var email: String? = null

    private var password: String? = null

    private val isInputDataValid: Boolean
        get() = Util.validateEmail(email) && Util.validatePassword(password)

    override fun attachUI(ui: SignInContract.View?) {
        super.attachUI(ui)
        compositeDisposable = CompositeDisposable()
    }

    override fun detachUI() {
        super.detachUI()
        compositeDisposable?.dispose()
    }

    override fun signIn() {
        signIn(email, password)
    }

    override fun validateEmail(email: String) {
        this.email = email
        this.ui?.changeButtonState(isInputDataValid)
    }

    override fun validatePassword(password: String) {
        this.password = password
        this.ui?.changeButtonState(isInputDataValid)
    }

    override fun goToRecoverPassword() {
        this.ui?.goToRecoverPassword()
    }

    override fun goToSignUp() {
            this.ui?.goToSignUp()
    }

    override fun refreshGenesisToken() {
        val disposable = signUpApi.initialToken
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code()) {
                        HttpsURLConnection.HTTP_OK -> tokenManager.saveToken(it.body())
                        else -> this.ui?.showConnectionError()
                    }
                }, { throwable -> this.handleError(throwable) })
        compositeDisposable?.add(disposable)
    }

    private fun signIn(email: String?, password: String?) {
        val request = UserSignIn.Builder()
        val disposable = signUpApi.initialToken
                .flatMap<Response<TokenEntity>> {
                    if (it.code() == HttpsURLConnection.HTTP_OK) {
                        return@flatMap signInOutApi.signIn(it.body()?.accessToken, request.uname(email).upass(password).build())
                    }
                    return@flatMap Single.just<Response<TokenEntity>>(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code()) {
                        HttpsURLConnection.HTTP_OK -> {
                            tokenManager.saveToken(it.body())
                            sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, true).apply()
                            sharedPreferences.edit().putBoolean(PIN_REQUIRED, false).apply()

                            // If user has no stored credentials, it means the cache/data of the app
                            // has been deleted. In this case, we need to retrieve credentials
                            // and setup PIN again
                            if(sharedPreferences.getString(CREDENTIAL, null) == null){
                                val disposable: Disposable = profileRepository.getUserProfile()
                                        .subscribeBy(
                                                onSuccess = {
                                                    // Set credentials
                                                    sharedPreferences.edit().putString(FIRST_NAME, it.firstName).apply()
                                                    sharedPreferences.edit().putString(LAST_NAME, it.lastName).apply()

                                                    // Send user to set PIN screen
                                                    this.ui?.goToSetPinScreen()
                                                     },
                                                onError = { globalErrorHandler(it) }
                                        )
                                compositeDisposable?.add(disposable)
                            } else if (shouldGoToConfirmation()) {
                                this.ui?.goToConfirmation()
                            } else this.ui?.goToMainScreen()
                        }
                        HttpsURLConnection.HTTP_NOT_FOUND -> this.ui?.showUserNotFound()
                        HttpsURLConnection.HTTP_BAD_REQUEST -> this.ui?.showWrongPassword()
                        else -> this.ui?.showConnectionError()
                    }
                }, { throwable -> this.handleError(throwable) })
        compositeDisposable?.add(disposable)

    }

    private fun handleError(th: Throwable) {
            this.ui?.showConnectionError()
    }

    private fun shouldGoToConfirmation(): Boolean {
        return sharedPreferences.getBoolean(CARD_REQUIRED, false)
                || sharedPreferences.getBoolean(RECIPIENT_REQUIRED, false)
    }

}
