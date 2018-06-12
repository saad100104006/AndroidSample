package uk.co.transferx.app.transfersummary.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.api.TransactionApi
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.tokenmanager.TokenManager
import java.net.HttpURLConnection
import javax.inject.Inject

class TransferSummaryPresenter @Inject constructor(
    private val transactionApi: TransactionApi,
    private val tokenManager: TokenManager
) :
    BasePresenter<TransferSummaryPresenter.TransferSummaryUI>() {

    var transactionCreate: TransactionCreate? = null
    var disposable: Disposable? = null

    override fun detachUI() {
        super.detachUI()
        disposable?.dispose()
    }

    fun setData(transactionCreate: TransactionCreate) {
        this.transactionCreate = transactionCreate
    }

    fun sendTransfer() {
        disposable = transactionApi.createTransaction(tokenManager.token, transactionCreate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ resp ->
                if (resp.code() == HttpURLConnection.HTTP_OK)
                    ui?.goBack()
            })
    }

    interface TransferSummaryUI : UI {

        fun goBack()

    }
}