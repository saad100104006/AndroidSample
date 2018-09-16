package uk.co.transferx.app.data.repository

import io.reactivex.Single
import uk.co.transferx.app.data.remote.TransactionApi
import uk.co.transferx.app.util.errors.UnauthorizedException
import uk.co.transferx.app.data.pojo.Rates
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepository @Inject constructor(
        private val transactionApi: TransactionApi,
        private val tokenManager: TokenManager
) {
    private var rates: Rates? = null
    private var lastCheck: Long = -1
    private  val timeWait = TimeUnit.MINUTES.toMillis(30)

    fun getRates(from: String?, to: String?): Single<Rates?> {
        if (rates == null || shouldCheck()) {
            return tokenManager.token
                .flatMap { transactionApi.getRats(it.accessToken, from, to) }
                .map {
                    when {
                        it.code() == HttpURLConnection.HTTP_OK -> it.body()
                        it.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> throw UnauthorizedException()
                        else -> throw Throwable("Error ${it.code()}")
                    }
                }
                .doOnSuccess {
                    rates = it
                    lastCheck = System.currentTimeMillis()
                }
        }
        return Single.just(rates)
    }

    private fun shouldCheck(): Boolean {
        return (System.currentTimeMillis() - lastCheck) > timeWait
    }
}