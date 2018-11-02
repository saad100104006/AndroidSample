package uk.co.transferx.app.ui.homescreen.presenters

import android.content.SharedPreferences
import android.support.annotation.VisibleForTesting
import io.reactivex.disposables.CompositeDisposable
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.Transaction
import uk.co.transferx.app.data.pojo.TransactionCreate
import uk.co.transferx.app.data.remote.TransactionApi
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.schedulers.BaseSchedulerProvider
import java.util.*
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class FragActivityPresenter @Inject
constructor(val recipientRepository: RecipientRepository,
            val transactionApi: TransactionApi,
            val tokenManager: TokenManager,
            val schedulerProvider: BaseSchedulerProvider,
            sharedPreferences: SharedPreferences) : BasePresenter<FragActivityPresenter.ActivityFragmentUI>(sharedPreferences) {
    private var compositeDisposable: CompositeDisposable? = null
    private var recipientDtos: List<RecipientDto>? = null
    private var transactionDtos = ArrayList<Transaction>()
    private var transactionRecurrentDtos = ArrayList<Transaction>()

    override fun attachUI(ui: ActivityFragmentUI) {
        super.attachUI(ui)
        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()

        if (transactionDtos.isEmpty()) {
            loadData(false)
            return
        } else this.ui.showAllTransactions(transactionDtos)
    }

    override fun detachUI() {
        super.detachUI()
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
            compositeDisposable = null
        }
    }

    fun goToSelectRecipient() {
        this.ui.goToSelectRecipient()
    }

    fun loadData(isRecurrent: Boolean) {
        compositeDisposable?.clear()

        isLoading.value = true

        // Recipients are loaded background
        loadRecipients()

        // Load transactions history
        if (isRecurrent) fetchRecurrentHistory()
        else fetchAllHistory()

    }

    private fun loadRecipients() {
        val dis = recipientRepository
                .recipients
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipients ->
                    recipientDtos = recipients
                }, { throwable ->
                    globalErrorHandler(throwable)
                })

        compositeDisposable!!.add(dis)
    }

    private fun fetchRecurrentHistory() {
        val historyDis = tokenManager.token
                .flatMap { (accessToken) -> transactionApi.getRecurrentHistory(accessToken) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ resp ->
                    isLoading.value = false
                    if (resp.code() == HttpsURLConnection.HTTP_OK) {
                        transactionRecurrentDtos.clear()
                        transactionRecurrentDtos.addAll(resp.body()?.transactions
                                ?: ArrayList<Transaction>())
                        if (transactionRecurrentDtos.isEmpty()) {
                            this.ui.hideRecurrentTransactions()
                        } else {
                            this.ui.showRecurrentTransactions(transactionRecurrentDtos)
                        }
                    }
                }, { globalErrorHandler(it) })
        compositeDisposable!!.add(historyDis)
    }

    private fun fetchAllHistory() {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }

        val historyDis = tokenManager.token
                .flatMap { (accessToken) -> transactionApi.getHistory(accessToken) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    isLoading.value = false
                    if (it.code() == HttpsURLConnection.HTTP_OK) {
                        transactionDtos.clear()
                        transactionDtos.addAll(it.body()?.transactions ?: ArrayList<Transaction>())

                        if (transactionDtos.isEmpty()) this.ui?.hideAllTransactions()
                        else this.ui?.showAllTransactions(transactionDtos)
                    }
                }, { globalErrorHandler(it) })
        compositeDisposable!!.add(historyDis)
    }

    override fun globalErrorHandler(throwable: Throwable) {
        super.globalErrorHandler(throwable)
        isLoading.value = false
        this.ui?.showError()
    }

    @VisibleForTesting
    fun setTransactionsCacheList(list: ArrayList<Transaction>){
        transactionDtos = list
    }

    interface ActivityFragmentUI : UI {
        fun showAllTransactions(transactions: List<Transaction>)

        fun showRecurrentTransactions(transactions: List<Transaction>)

        fun showError()

        fun goToReceiptScreen(transaction: TransactionCreate)

        fun goToSelectRecipient()

        fun hideAllTransactions()

        fun hideRecurrentTransactions()
    }
}
