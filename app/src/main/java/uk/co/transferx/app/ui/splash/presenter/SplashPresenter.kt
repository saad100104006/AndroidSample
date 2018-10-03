package uk.co.transferx.app.ui.splash.presenter

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.data.firebase.SubscriptionManager
import uk.co.transferx.app.data.remote.SignUpApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.data.repository.tokenmanager.TokenRepository
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.splash.SplashContract
import uk.co.transferx.app.util.Constants.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

/**
 * Created by sergey on 19.11.17.
 */

class SplashPresenter @Inject
constructor(private val signUpApi: SignUpApi, private val tokenManager: TokenManager,
            subscriptionManager: SubscriptionManager,
            sharedPreferences: SharedPreferences,
            private val tokenRepository: TokenRepository)
    : BasePresenter<SplashContract.View>(sharedPreferences) {

    private var dis: Disposable? = Disposables.disposed()

    init {
        subscriptionManager.initSubscribtions()
    }

    override fun attachUI(ui: SplashContract.View) {
        super.attachUI(ui)
        dis = signUpApi.initialToken
                .delay(SPLASH_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val loggedInStatus = sharedPreferences.getBoolean(LOGGED_IN_STATUS, false)
                    val firstStart = sharedPreferences.getBoolean(FIRST_START_APP, true)

                    if (firstStart) {
                        this.ui?.goToTutorialScreen()

                        sharedPreferences.edit().putBoolean(FIRST_START_APP, false).apply()
                        return@subscribe
                    }

                    if (it.code() == HttpsURLConnection.HTTP_OK && tokenManager.shouldSaveGenesis()) {
                        tokenManager.saveToken(it.body())
                    }

                    if (loggedInStatus) {
                        // TODO enable link when PIN strategy is decided
//                        this.ui?.goToPinScreen()
                    }
                    this.ui?.goToLandingScreen()

                }, { this.handleError(it) })
    }

    private fun handleError(throwable: Throwable) {
        this.ui?.goToLandingScreen()
    }

    override fun detachUI() {
        dis?.dispose()
        super.detachUI()
    }

}
