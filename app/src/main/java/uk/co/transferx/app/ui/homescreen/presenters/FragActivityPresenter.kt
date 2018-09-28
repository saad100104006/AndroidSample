package uk.co.transferx.app.ui.homescreen.presenters

import android.content.SharedPreferences
import com.google.gson.Gson

import java.util.ArrayList

import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.Transaction
import uk.co.transferx.app.data.pojo.Transactions
import uk.co.transferx.app.data.remote.TransactionApi
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI

class FragActivityPresenter @Inject
constructor(private val recipientRepository: RecipientRepository,
            private val transactionApi: TransactionApi,
            private val tokenManager: TokenManager,
            sharedPreferences: SharedPreferences) : BasePresenter<FragActivityPresenter.ActivityFragmentUI>(sharedPreferences) {
    private var compositeDisposable: CompositeDisposable? = null
    private var recipientDtos: List<RecipientDto>? = null
    private var transactionDtos = ArrayList<Transaction>()

    init {
        generateMockData()
    }

    private fun generateMockData() {
        val str = "{\"transactions\": [{\"id\": 1,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-16T13:54:31.000Z\",\"updated\": \"2018-02-16T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}},{\"id\": 2,\"transaction_schedule_id\": 1,\"from\": \"5a5ca3375d82df077128a941\",\"to\": \"d71bd947-c9f9-451b-ab48-09abe1165154\",\"currency\": \"UGX\",\"amount\": 5000,\"status\": \"NEW\",\"message\": \"Happy birthdate Curtis!\",\"created\": \"2018-02-18T13:54:31.000Z\",\"updated\": \"2018-02-18T13:54:34.000Z\",\"meta\": {\"cardInfo\": {\"expDate\": \"04/22\",\"number\": \"6466\",\"name\": \"Emily Carter\",\"type\": \"Visa\"},\"recipientInfo\": {\"firstname\": \"Firstname2\",\"lastname\": \"Lastname\",\"country\": \"Uganda\",\"phone\": \"+44 020 7234 34\",\"image_url\": \"example.com/wasm\"}}}]}"
        val gson = Gson()
        transactionDtos = gson.fromJson(str, Transactions::class.java).transactions
    }

    override fun attachUI(ui: ActivityFragmentUI) {
        super.attachUI(ui)
        if (transactionDtos.isEmpty()) {
            loadData()
            return
        }
        this.ui.setData(transactionDtos)

    }

    override fun detachUI() {
        super.detachUI()
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
            compositeDisposable = null
        }
    }

    fun loadData() {

        /*compositeDisposable = CompositeDisposable()
        val dis = recipientRepository.recipients
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recipients ->
                    recipientDtos = recipients
                    fetchHistory()
                }, Consumer<Throwable> { this.globalErrorHandler(it) })
        compositeDisposable.add(dis)*/

    }

    private fun fetchHistory() {
        /*if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }

        val historyDis = tokenManager.token
                .flatMap { (accessToken) -> transactionApi.getHistory(accessToken) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resp ->
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        ui.setData(resp.body()!!.transactions)
                        return@tokenManager.getToken()
                                .flatMap(token -> transactionApi.getHistory(token.getAccessToken()))
                        .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe
                    }
                    globalErrorHandler(resp.code())

                }
        compositeDisposable.add(historyDis)*/
    }

    override fun globalErrorHandler(throwable: Throwable) {
        super.globalErrorHandler(throwable)
    }

    interface ActivityFragmentUI : UI {

        fun setData(transactions: List<Transaction>)

        fun setError()

        fun goToMoneySendScreen()

    }
}
