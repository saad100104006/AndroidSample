package uk.co.transferx.app.repository

import io.reactivex.Single
import uk.co.transferx.app.api.TransactionApi
import uk.co.transferx.app.errors.UnauthorizedException
import uk.co.transferx.app.pojo.Rates
import uk.co.transferx.app.tokenmanager.TokenManager
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

    fun getRates(from: String, to: String): Single<Rates?> {
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