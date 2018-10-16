package uk.co.transferx.app.ui.signup.presenters

import android.content.SharedPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import uk.co.transferx.app.data.pojo.CheckEmail
import uk.co.transferx.app.data.pojo.TokenEntity
import uk.co.transferx.app.data.pojo.UserRequest
import uk.co.transferx.app.data.remote.SignUpApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.Constants.*
import uk.co.transferx.app.util.Util
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection


/**
 * Created by sergey on 08.12.17.
 */

class SignUpStepTwoPresenter @Inject
constructor(sharedPreferences: SharedPreferences,
            private val signUpApi: SignUpApi, private val tokenManager: TokenManager)
    : BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI>(sharedPreferences) {
    private var email: String? = null
    private var password: String? = null
    private var rePassword: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var phoneNumber: String? = null
    var country: String? = null
    private var isEmailErrorShown: Boolean = false

    private var compositeDisposable: CompositeDisposable? = null

    private val isInputCorrect: Boolean
        get() = Util.validatePassword(password) && Util.validateEmail(email) && !isEmailErrorShown

    private val isPasswordInputCorrect: Boolean
        get() = password == rePassword

    override fun attachUI(ui: SignUpStepTwoUI?) {
        super.attachUI(ui)
        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()
    }

    override fun detachUI() {
        super.detachUI()
        compositeDisposable?.dispose()
    }

    fun signUpUser() {
        if (isPasswordInputCorrect) {
            val userRequest = UserRequest.Builder()
            compositeDisposable?.add(signUpApi.initialToken
                    .flatMap<Response<TokenEntity>> {
                        if (it.code() == HttpsURLConnection.HTTP_OK) {
                            return@flatMap signUpApi.registerUser(it.body()?.accessToken,
                                    userRequest.email(email).firstName(firstName).lastName(lastName).upass(password).build())
                        } else return@flatMap Single.just<Response<TokenEntity>>(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        when (it.code()) {
                            HttpsURLConnection.HTTP_OK -> {
                                // Save token
                                tokenManager.saveToken(it.body())

                                // Edit local credentials
                                sharedPreferences.edit().putBoolean(LOGGED_IN_STATUS, true).apply()
                                sharedPreferences.edit().putBoolean(PIN_REQUIRED, false).apply()
                                sharedPreferences.edit().putBoolean(PIN_SHOULD_BE_INPUT, true).apply()
                                sharedPreferences.edit().putBoolean(CARD_REQUIRED, true).apply()
                                sharedPreferences.edit().putBoolean(RECIPIENT_REQUIRED, true).apply()

                                // TBDD - Currently firstName and lastName are required for PIN encryption
                                sharedPreferences.edit().putString(FIRST_NAME, firstName).apply()
                                sharedPreferences.edit().putString(LAST_NAME, lastName).apply()

                                this.ui?.goToPinSetup()

                            }
                            HttpsURLConnection.HTTP_BAD_REQUEST -> this.ui?.showConnectionError()
                            HttpsURLConnection.HTTP_INTERNAL_ERROR -> this.ui?.showBackendError()
                            else -> this.ui?.showConnectionError()
                        }
                    }, { this.ui?.showConnectionError() })
            )


        } else ui.showErrorPassword()
    }

    fun checkIfEmailIsTaken() {
        if (Util.validateEmail(email)) {
            compositeDisposable?.add(signUpApi.initialToken
                    .flatMap<Response<ResponseBody>> {
                        return@flatMap signUpApi.checkEmail(it.body()?.accessToken, CheckEmail(email!!))
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.code() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                            this.ui.showErrorEmail()
                            this.ui.hideKeyboard()
                            isEmailErrorShown = true
                        } else isEmailErrorShown = false
                    }, { this.ui?.showConnectionError() }))
        }
    }

    fun setEmail(email: String) {
        this.email = email
        validateInputData()
    }

    fun setPassword(password: String) {
        this.password = password
        validateInputData()
    }

    fun setRePassword(rePassword: String) {
        this.rePassword = rePassword
        validateInputData()
    }

    private fun validateInputData() {
        ui?.setStateButton(isInputCorrect)
    }

    interface SignUpStepTwoUI : UI {

        fun goToPinSetup()

        fun showErrorPassword()

        fun showErrorEmail()

        fun showConnectionError()

        fun showBackendError()

        fun setStateButton(isEnabled: Boolean)

        fun hideKeyboard()
    }
}
