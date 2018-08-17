package uk.co.transferx.app.mainscreen.schedule.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.api.TransactionApi
import uk.co.transferx.app.pojo.Rate
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.repository.ExchangeRateRepository
import uk.co.transferx.app.tokenmanager.TokenManager
import java.math.BigDecimal
import java.net.HttpURLConnection
import javax.inject.Inject

class ReviewPresenter @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val tokenManager: TokenManager,
    private val transactionApi: TransactionApi
) :
    BasePresenter<ReviewPresenter.ReviewUI>() {


    private lateinit var transaction: TransactionCreate
    private var disposable: Disposable? = null


    fun setTransaction(transaction: TransactionCreate) {
        this.transaction = transaction
    }

    fun sendTransfer() {
        disposable = tokenManager.token
            .flatMap {
                transactionApi.createTransaction(
                    it.accessToken, transaction.copy(
                        card = null,
                        baseAmount = null,
                        recipientDto = null
                    )
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code() == HttpURLConnection.HTTP_OK) {
                    ui?.goToTransferReceipt(transaction.copy(transactionNumber = it.body()?.id ))
                }
            }, { globalErrorHandler(it) })
    }


    override fun attachUI(ui: ReviewUI?) {
        super.attachUI(ui)
        this.ui?.setDataToSummary(transaction)
        disposable =
                exchangeRateRepository.getRates(transaction.currencyInput, transaction.currency)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { this.ui?.setExchangeRate(formattedRate(it?.rates?.get(0))) },
                        { globalErrorHandler(it) })
    }

    private fun formattedRate(rate: Rate?): String {
        val rateValue = BigDecimal(rate?.rate).setScale(4, BigDecimal.ROUND_HALF_UP)
        return String.format(
            "%s %s = %s %s",
            "1",
            transaction.currencyInput,
            rateValue.toPlainString(),
            transaction.currency
        )
    }

    override fun detachUI() {
        disposable?.dispose()
        super.detachUI()
    }

    interface ReviewUI : UI {

        fun goToTransferReceipt(transaction: TransactionCreate)

        fun setExchangeRate(rate: String)

        fun setDataToSummary(transaction: TransactionCreate)
    }
}