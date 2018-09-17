package uk.co.transferx.app.ui.signin.presenter

import android.content.SharedPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import uk.co.transferx.app.data.pojo.TokenEntity
import uk.co.transferx.app.data.pojo.UserSignIn
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.data.remote.SignUpApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.data.repository.tokenmanager.TokenRepository
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.ui.signin.SignInContract
import uk.co.transferx.app.util.Constants.*
import uk.co.transferx.app.util.Util
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

/**
 * Created by smilevkiy on 14.11.17.
 */

class SignInPresenter @Inject
constructor(private val signInOutApi: SignInOutApi, private val signUpApi: SignUpApi,
            private val tokenManager: TokenManager, sharedPreferences: SharedPreferences,
            private val tokenRepository: TokenRepository)
    : BasePresenter<SignInContract.View>(sharedPreferences), SignInContract.Presenter {
    private var disposable: Disposable? = null
    private var email: String? = null
    private var password: String? = null

    private val isInputDataValid: Boolean
        get() = Util.validateEmail(email!!) && Util.validatePassword(password)

    override fun detachUI() {
        super.detachUI()
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun signIn() {
        if (tokenRepository.getToken().accessToken.isEmpty() && ui != null) {
            ui.showConnectionError()
            return
        }
        signIn(email, password)
    }

    override fun validateEmail(email: String) {
        if (ui != null) {
            this.email = email
            ui.changeButtonState(isInputDataValid)
        }
    }

    override fun validatePassword(password: String) {
        if (ui != null) {
            this.password = password
            ui.changeButtonState(isInputDataValid)
        }
    }

    override fun goToRecoverPassword() {
        ui.goToRecoverPassword()
    }

    override fun goToSignUp() {
        if (!tokenRepository.getToken().accessToken.isEmpty()) {
            sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, false).apply()
            sharedPreferences.edit().remove(CREDENTIAL).apply()
            ui.goToSignUp()
            return
        }
        ui.showConnectionError()
    }

    override fun refreshGenesisToken() {
        disposable = signUpApi.initialToken
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    if (ui != null) {
                        if (resp.code() == HttpsURLConnection.HTTP_OK) {
                            tokenManager.saveToken(resp.body())
                            return@subscribe
                        }
                        ui.showConnectionError()
                    }

                }, { this.handleError() })
    }

    private fun signIn(email: String?, password: String?) {
        val request = UserSignIn.Builder()
        disposable = signUpApi.initialToken
                .flatMap<Response<TokenEntity>> { resp ->
                    if (resp.code() == HttpsURLConnection.HTTP_OK) {
                        return@flatMap signInOutApi.signIn(resp.body()?.accessToken, request.uname(email).upass(password).build())
                    }
                    return@flatMap Single.just<Response<TokenEntity>>(resp)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        tokenManager.saveToken(resp.body())
                        sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, true).apply()

                        ui.goToMainScreen()
                        return@subscribe
                    } else if (resp.code() == HttpsURLConnection.HTTP_NOT_FOUND && ui != null) {
                        ui.showUserNotFound()
                        return@subscribe
                    } else if (resp.code() == HttpsURLConnection.HTTP_UNAUTHORIZED && ui != null) {
                        ui.showWrongPassword()
                        return@subscribe
                    }
                    ui.showConnectionError()

                }, { this.handleError() })

    }

    private fun handleError() {
        if (ui != null) {
            ui.showConnectionError()
        }
    }
}
