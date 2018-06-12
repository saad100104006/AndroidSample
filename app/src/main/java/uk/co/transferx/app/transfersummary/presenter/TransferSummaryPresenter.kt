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

    private var transactionCreate: TransactionCreate? = null
    private var disposable: Disposable? = null
    private var isInitialized: Boolean = false

    override fun attachUI(ui: TransferSummaryUI?) {
        super.attachUI(ui)
        if(!isInitialized) {
            this.ui?.fillUser(transactionCreate)
            isInitialized = true
            transactionCreate?.card = null
            transactionCreate?.recipientDto = null
        }
    }

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
        fun fillUser(transactionCreate: TransactionCreate?)
    }
}