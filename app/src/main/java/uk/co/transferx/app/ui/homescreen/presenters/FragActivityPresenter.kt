package uk.co.transferx.app.ui.homescreen.presenters

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers

import java.util.ArrayList

import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.TokenEntity
import uk.co.transferx.app.data.pojo.Transaction
import uk.co.transferx.app.data.pojo.TransactionCreate
import uk.co.transferx.app.data.pojo.Transactions
import uk.co.transferx.app.data.remote.TransactionApi
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import javax.net.ssl.HttpsURLConnection

class FragActivityPresenter @Inject
constructor(val recipientRepository: RecipientRepository,
            val transactionApi: TransactionApi,
            val tokenManager: TokenManager,
            sharedPreferences: SharedPreferences) : BasePresenter<FragActivityPresenter.ActivityFragmentUI>(sharedPreferences) {
    private var compositeDisposable: CompositeDisposable? = null
    private var recipientDtos: List<RecipientDto>? = null
    private var transactionDtos = ArrayList<Transaction>()
    private var transactionRecurrentDtos = ArrayList<Transaction>()

    init {
        generateMockData()
    }

    private fun generateMockData(): List<Transaction> {
        val str = "{\"transactions\": [{\"id\": 1,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-16T13:54:31.000Z\",\"updated\": \"2018-02-16T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}}]}"
        val gson = Gson()
        return gson.fromJson(str, Transactions::class.java).transactions
    }

    override fun attachUI(ui: ActivityFragmentUI) {
        super.attachUI(ui)
        if(compositeDisposable==null) {
            compositeDisposable = CompositeDisposable()
        }

        if (transactionDtos.isEmpty()) {
            this.ui.hideAllTransactions()
            loadData(false)
            return
        } else {
            this.ui.showAllTransactions(transactionDtos)
        }

    }

    override fun detachUI() {
        super.detachUI()
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
            compositeDisposable = null
        }
    }

    fun loadData(isRecurrent: Boolean) {
        compositeDisposable?.clear()

        isLoading.value = true

        val dis = recipientRepository.recipients
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recipients ->
                    isLoading.value = false
                    recipientDtos = recipients
                    if (isRecurrent)
                        fetchRecurrentHistory()
                    else
                        fetchAllHistory()

                }, { throwable ->
                    isLoading.value = false
                    globalErrorHandler(throwable)
                })

        compositeDisposable!!.add(dis)

    }

    private fun fetchRecurrentHistory() {
        isLoading.value = true

        val historyDis = tokenManager.token
                .flatMap { (accessToken) -> transactionApi.getRecurrentHistory(accessToken) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resp ->

                    isLoading.value = false
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        transactionRecurrentDtos.clear()
                        transactionRecurrentDtos.addAll(resp.body()?.transactions
                                ?: ArrayList<Transaction>())
                        if (transactionRecurrentDtos.isEmpty()) {
                            ui.hideRecurrentTransactions()
                        } else {
                            ui.showRecurrentTransactions(transactionRecurrentDtos)
                        }
                    }

                    globalErrorHandler(resp.code())

                }
        compositeDisposable!!.add(historyDis)
    }

    private fun fetchAllHistory() {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }

        val historyDis = tokenManager.token
                .flatMap { (accessToken) -> transactionApi.getHistory(accessToken) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resp ->
                    isLoading.value = false
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        transactionDtos.clear()
                        transactionDtos.addAll(resp.body()?.transactions ?: generateMockData())
                        if (transactionDtos.isEmpty()) {
                            ui.hideAllTransactions()
                        } else {
                            ui.showAllTransactions(transactionDtos)
                        }
                    }
                    globalErrorHandler(resp.code())

                }
        compositeDisposable!!.add(historyDis)
    }

    override fun globalErrorHandler(throwable: Throwable) {
        super.globalErrorHandler(throwable)
    }

    interface ActivityFragmentUI : UI {
        fun showAllTransactions(transactions: List<Transaction>)

        fun showRecurrentTransactions(transactions: List<Transaction>)

        fun setError()

        fun goToRecieptScreen(transaction: TransactionCreate)

        fun hideAllTransactions()

        fun hideRecurrentTransactions()


    }
}
