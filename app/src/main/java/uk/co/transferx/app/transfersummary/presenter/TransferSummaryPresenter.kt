package uk.co.transferx.app.transfersummary.presenter

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.pojo.Rate
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.repository.ExchangeRateRepository
import java.math.BigDecimal
import javax.inject.Inject

class TransferSummaryPresenter @Inject constructor(
    sharedPreferences: SharedPreferences,
    private val exchangeRateRepository: ExchangeRateRepository
) :
    BasePresenter<TransferSummaryPresenter.TransferSummaryUI>(sharedPreferences) {

    private lateinit var transactionCreate: TransactionCreate
    private var disposable: Disposable = Disposables.disposed()

    fun setData(transactionCreate: TransactionCreate) {
        this.transactionCreate = transactionCreate
    }

    fun close() {
        ui?.goBack()
    }

    override fun attachUI(ui: TransferSummaryUI?) {
        super.attachUI(ui)
        this.ui?.fillData(transactionCreate)
        disposable = exchangeRateRepository.getRates(
            transactionCreate.currencyInput,
            transactionCreate.currency
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { this.ui?.setExchangeRate(formattedRate(it?.rates?.get(0))) },
                { globalErrorHandler(it) })
    }

    override fun detachUI() {
        disposable.dispose()
        super.detachUI()
    }

    private fun formattedRate(rate: Rate?): String {
        val rateValue = BigDecimal(rate?.rate).setScale(4, BigDecimal.ROUND_HALF_UP)
        return String.format(
            "%s %s = %s %s",
            "1",
            transactionCreate.currencyInput,
            rateValue.toPlainString(),
            transactionCreate.currency
        )
    }

    interface TransferSummaryUI : UI {
        fun goBack()
        fun fillData(transactionCreate: TransactionCreate)
        fun setExchangeRate(rate: String)
    }
}