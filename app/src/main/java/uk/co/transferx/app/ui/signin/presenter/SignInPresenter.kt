package uk.co.transferx.app.ui.signin.presenter

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import uk.co.transferx.app.data.pojo.TokenEntity
import uk.co.transferx.app.data.pojo.UserSignIn
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.data.remote.SignUpApi
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
 private val tokenRepository: TokenRepository)
    : BasePresenter<SignInContract.View>(sharedPreferences), SignInContract.Presenter {

    private var disposable: Disposable? = Disposables.disposed()

    private var email: String? = null

    private var password: String? = null

    private val isInputDataValid: Boolean
        get() = Util.validateEmail(email) && Util.validatePassword(password)

    override fun detachUI() {
        super.detachUI()
        disposable?.dispose()
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
        disposable = signUpApi.initialToken
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code()) {
                        HttpsURLConnection.HTTP_OK -> tokenManager.saveToken(it.body())
                        else -> this.ui?.showConnectionError()
                    }
                }, { throwable -> this.handleError(throwable) })
    }

    private fun signIn(email: String?, password: String?) {
        val request = UserSignIn.Builder()
        disposable = signUpApi.initialToken
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

                            if (shouldGoToConfirmation()) {
                                this.ui?.goToConfirmation()
                            } else this.ui?.goToMainScreen()
                        }
                        HttpsURLConnection.HTTP_NOT_FOUND -> this.ui?.showUserNotFound()
                        HttpsURLConnection.HTTP_BAD_REQUEST -> this.ui?.showWrongPassword()
                        else -> this.ui?.showConnectionError()
                    }
                }, { throwable -> this.handleError(throwable) })

    }

    private fun handleError(th: Throwable) {
            this.ui?.showConnectionError()
    }

    private fun shouldGoToConfirmation(): Boolean {
        return sharedPreferences.getBoolean(CARD_REQUIRED, false)
                || sharedPreferences.getBoolean(RECIPIENT_REQUIRED, false)
    }

}
