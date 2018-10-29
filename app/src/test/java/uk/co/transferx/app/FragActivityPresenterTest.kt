package uk.co.transferx.app

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.remote.TransactionApi
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.homescreen.presenters.FragActivityPresenter
import uk.co.transferx.app.util.schedulers.ImmediateSchedulerProvider
import android.accounts.Account
import retrofit2.Retrofit
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

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        activityPresenter = FragActivityPresenter(recipientRepository, transactionApi, tokenManager,
                ImmediateSchedulerProvider(), sharedPreferences)

        val list = listOf<RecipientDto>(RecipientDto("4", "4", "4","4","4","4"))

        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(list))
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))


        val listTransactions = arrayListOf<Transaction>(
                Transaction("s","s","s","s","s","s","s","s","s",
                false, "s","s","s", Meta(CardInfo("s","s","s","s"),
                RecipientInfo("s","s","s","s","s"))))
        val response = retrofit2.Response.success(Transactions(listTransactions))

        Mockito.`when`(transactionApi.getHistory(any())).thenReturn(Single.just(response))

        activityPresenter.attachUI(view)
    }

    @Test
    fun testAttachUI() {

        Mockito.verify(view).hideAllTransactions()

    }
}