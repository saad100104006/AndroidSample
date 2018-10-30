package uk.co.transferx.app.homescreen.activity

import android.content.SharedPreferences
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.remote.TransactionApi
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.homescreen.presenters.FragActivityPresenter
import uk.co.transferx.app.util.schedulers.ImmediateSchedulerProvider
import uk.co.transferx.app.data.pojo.*


class FragActivityPresenterTest {
    // SUT
    lateinit var activityPresenter: FragActivityPresenter

    @Mock
    lateinit var recipientRepository: RecipientRepository

    @Mock
    lateinit var transactionApi: TransactionApi

    @Mock
    lateinit var tokenManager: TokenManager

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var view: FragActivityPresenter.ActivityFragmentUI

    // Mock Data
    private val listRecipients = listOf<RecipientDto>(RecipientDto("4", "4", "4","4","4","4"))
    private val listTransactions = arrayListOf<Transaction>(
            Transaction("s","s","s","s","s","s","s","s","s",
                    false, "s","s","s", Meta(CardInfo("s","s","s","s"),
                    RecipientInfo("s","s","s","s","s"))))
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        activityPresenter = FragActivityPresenter(recipientRepository, transactionApi, tokenManager,
                ImmediateSchedulerProvider(), sharedPreferences)
    }

    @Test
    fun testAttachViewCachedData() {
        // Verify no API calls are made at this stage
        Mockito.verify(recipientRepository, never()).recipients
        Mockito.verify(tokenManager, never()).token
        Mockito.verify(transactionApi, never()).getHistory(any())

        // Set cached transactions
        activityPresenter.setTransactionsCacheList(listTransactions)

        // Attach UI
        activityPresenter.attachUI(view)

        // Make sure
        Mockito.verify(view).showAllTransactions(listTransactions)

    }

    @Test
    fun testAttachViewHistory_SuccessResponse() {
        // Let's prepare successful mocks
        prepareHistorySuccessfulMockData()

        // Set no cached transactions
        activityPresenter.setTransactionsCacheList(ArrayList<Transaction>())

        // Attach UI
        activityPresenter.attachUI(view)

        // Verify recipients repository is queried
        Mockito.verify(recipientRepository).recipients

        // First, token should retrieved
        Mockito.verify(tokenManager).token

        // As this is a cache-less start-up, history transactions should be queried
        Mockito.verify(transactionApi).getHistory(any())

        // As we prepared a successful response, the UI should update accordingly
        Mockito.verify(view).showAllTransactions(listTransactions)
    }

    @Test
    fun testAttachViewHistory_EmptyResponse() {
        // Let's prepare successful mocks
        prepareHistoryEmptyMockData()

        // Set no cached transactions
        activityPresenter.setTransactionsCacheList(ArrayList<Transaction>())

        // Attach UI
        activityPresenter.attachUI(view)

        // Verify recipients repository is queried
        Mockito.verify(recipientRepository).recipients

        // First, token should retrieved
        Mockito.verify(tokenManager).token

        // As this is a cache-less start-up, history transactions should be queried
        Mockito.verify(transactionApi).getHistory(any())

        // As we prepared an empty response, the UI should update accordingly
        Mockito.verify(view).hideAllTransactions()
    }

    @Test
    fun testAttachViewHistory_FailureResponse() {
        // Let's prepare failure response mocks
        prepareHistoryFailureMockData()

        // Set no cached transactions
        activityPresenter.setTransactionsCacheList(ArrayList<Transaction>())

        // Attach UI
        activityPresenter.attachUI(view)

        // Verify recipients repository is queried
        Mockito.verify(recipientRepository).recipients

        // First, token should retrieved
        Mockito.verify(tokenManager).token

        // As this is a cache-less start-up, history transactions should be queried
        Mockito.verify(transactionApi).getHistory(any())

        // As we prepared a failure response, the UI should update accordingly
        Mockito.verify(view).showError()
    }

    @Test
    fun testRecurrentCall_SuccessResponse() {
        // Let's prepare successful mocks
        prepareRecurrentSuccessfulMockData()

        // Set no cached transactions
        activityPresenter.setTransactionsCacheList(ArrayList<Transaction>())

        // Attach UI
        activityPresenter.attachUI(view)

        // Other calls take place now, should be tested by now

        // Let's get recurrent transactions
        activityPresenter.loadData(true)

        // As always, token should retrieved, this time once when Presenter initializes and
        // twice when you call for recurrent history
        Mockito.verify(tokenManager, times(2)).token

        // Recurrent transactions should be queried
        Mockito.verify(transactionApi).getRecurrentHistory(any())

        // As we prepared a success response, the UI should update accordingly
        Mockito.verify(view).showRecurrentTransactions(listTransactions)
    }


    @Test
    fun testRecurrentCall_EmptyResponse() {
        // Let's prepare successful mocks
        prepareRecurrentEmptyMockData()

        // Set no cached transactions
        activityPresenter.setTransactionsCacheList(ArrayList<Transaction>())

        // Attach UI
        activityPresenter.attachUI(view)

        // Other calls take place now, should be tested by now

        // Let's get recurrent transactions
        activityPresenter.loadData(true)

        // As always, token should retrieved, this time once when Presenter initializes and
        // twice when you call for recurrent history
        Mockito.verify(tokenManager, times(2)).token

        // Recurrent transactions should be queried
        Mockito.verify(transactionApi).getRecurrentHistory(any())

        // As we prepared an empty response, the UI should update accordingly
        Mockito.verify(view).hideRecurrentTransactions()
    }

    @Test
    fun testRecurrentCall_FailureResponse() {
        // Let's prepare failure mocks
        prepareRecurrentFailureMockData()

        // Set no cached transactions
        activityPresenter.setTransactionsCacheList(ArrayList<Transaction>())

        // Attach UI
        activityPresenter.attachUI(view)

        // Other calls take place now, should be tested by now

        // Let's get recurrent transactions
        activityPresenter.loadData(true)

        // As always, token should retrieved, this time once when Presenter initializes and
        // twice when you call for recurrent history
        Mockito.verify(tokenManager, times(2)).token

        // Recurrent transactions should be queried
        Mockito.verify(transactionApi).getRecurrentHistory(any())

        // As we prepared a failure response, the UI should update accordingly
        Mockito.verify(view).showError()
    }


    private fun prepareHistorySuccessfulMockData(){
        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(listRecipients))
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))

        val response = retrofit2.Response.success(Transactions(listTransactions))
        Mockito.`when`(transactionApi.getHistory(any())).thenReturn(Single.just(response))
    }

    private fun prepareHistoryFailureMockData(){
        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(listRecipients))
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))

        Mockito.`when`(transactionApi.getHistory(any())).thenReturn(Single.error(Throwable()))
    }

    private fun prepareRecurrentSuccessfulMockData(){
        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(listRecipients))
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))

        val response = retrofit2.Response.success(Transactions(listTransactions))
        Mockito.`when`(transactionApi.getRecurrentHistory(any())).thenReturn(Single.just(response))
    }

    private fun prepareRecurrentFailureMockData(){
        // Let's suppose that when presenter initializes, API call is made successful
        val response = retrofit2.Response.success(Transactions(listTransactions))
        Mockito.`when`(transactionApi.getHistory(any())).thenReturn(Single.just(response))

        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(listRecipients))
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))

        Mockito.`when`(transactionApi.getRecurrentHistory(any())).thenReturn(Single.error(Throwable()))
    }


    private fun prepareHistoryEmptyMockData(){
        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(listRecipients))
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))

        val response = retrofit2.Response.success(Transactions(ArrayList()))
        Mockito.`when`(transactionApi.getHistory(any())).thenReturn(Single.just(response))
    }
    private fun prepareRecurrentEmptyMockData(){
        // Let's suppose that when presenter initializes, API call is made successful
        val response = retrofit2.Response.success(Transactions(ArrayList()))
        Mockito.`when`(transactionApi.getHistory(any())).thenReturn(Single.just(response))

        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(listRecipients))
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))

        Mockito.`when`(transactionApi.getRecurrentHistory(any())).thenReturn((Single.just(response)))
    }
}