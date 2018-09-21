package uk.co.transferx.app.ui.recoverpass.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import uk.co.transferx.app.data.pojo.ForgotEmail
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.recoverpass.RecoverPasswordContract
import uk.co.transferx.app.util.Util
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

/**
 * Created by sergey on 15/03/2018.
 */

class RecoverPasswordPresenter @Inject constructor
(private val tokenManager: TokenManager, private val signInOutApi: SignInOutApi)
    : BasePresenter<RecoverPasswordContract.View>(), RecoverPasswordContract.Presenter {
    private var validEmail: String? = null
    private var disposable: Disposable? = Disposables.disposed()

    override fun detachUI() {
        super.detachUI()
        disposable?.dispose()
    }

    override
    fun validateEmail(email: String) {
        if (!Util.validateEmail(email)) {
            this.ui?.lockButton()
        } else {
            validEmail = email
            ui.unlockButton()
        }
    }

    override
    fun sendEmail() {
        disposable = tokenManager.token
                .flatMap<Response<ResponseBody>> { (accessToken) -> signInOutApi.forgotEmail(accessToken, ForgotEmail(validEmail!!)) }
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code() == HttpsURLConnection.HTTP_OK) this.ui?.goToSuccessScreen()
                    else handleError(Throwable(it.message()))
                }, { this.handleError(it) })
    }

    override
    fun goBack() {
        this.ui?.goBackToMain()
    }

    override
    fun handleError(throwable: Throwable) {
        this.ui?.error()
    }

}
