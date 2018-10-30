package uk.co.transferx.app

/**
 * Created by Saad on 30/10/18.
 */

import android.content.SharedPreferences
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.homescreen.presenters.FragActivityPresenter
import uk.co.transferx.app.data.pojo.*
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.ui.settings.presenter.SettingsFragmentPresenter
import android.R.attr.onClick
import org.mockito.Mockito.*
import org.mockito.Mockito.`when`




class SettingsFragmentPresenterTest {
    // SUT
    lateinit var activityPresenter: SettingsFragmentPresenter

    @Mock
    lateinit var signInOutApi: SignInOutApi

    @Mock
    lateinit var tokenManager: TokenManager

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var view: SettingsFragmentPresenter.SettingsFragmentUI

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        activityPresenter = SettingsFragmentPresenter(signInOutApi,  tokenManager, sharedPreferences)
    }

    @Test
    fun testPerformOnclick() {
        //testing click perform

        Mockito.verify(activityPresenter).supportClicked()
        Mockito.verify(activityPresenter).clickProfile()
        Mockito.verify(activityPresenter).clickNotification()
        Mockito.verify(activityPresenter).logOut()

    }

    @Test
    fun testAttachViewCachedData() {
        // Verify no API calls are made at this stage
        Mockito.verify(signInOutApi, never()).logOut(any())
        Mockito.verify(tokenManager, never()).token
        // Attach UI
        activityPresenter.attachUI(view)


    }

    @Test
    fun testLogout_SuccessResponse() {
        // Let's prepare successful mocks
        prepareLogoutSuccessfulMockData()

        // Attach UI
        activityPresenter.attachUI(view)

        Mockito.verify(signInOutApi).logOut(any())
        Mockito.verify(tokenManager).token

    }

    @Test
    fun testLogout_EmptyResponse() {
        // Let's prepare successful mocks
        prepareEmpty_logoutMockData()

        // Attach UI
        activityPresenter.attachUI(view)
        Mockito.verify(signInOutApi).logOut(any())
        Mockito.verify(tokenManager).token
    }

    @Test
    fun testLogout_FailureResponse() {
        // Let's prepare failure response mocks
        prepareLogoutFailureMockData()

        // Attach UI
        activityPresenter.attachUI(view)

        Mockito.verify(signInOutApi).logOut(any())
        // First, token should retrieved
        Mockito.verify(tokenManager).token

    }
    private fun prepareLogoutSuccessfulMockData(){

        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))
        Mockito.`when`(signInOutApi.logOut(any())).thenReturn(Single.just(any()))
    }

    private fun prepareLogoutFailureMockData(){
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))
        Mockito.`when`(signInOutApi.logOut(any())).thenReturn(Single.just(any()))
    }

    private fun prepareEmpty_logoutMockData(){
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))
        Mockito.`when`(signInOutApi.logOut(any())).thenReturn(Single.just(any()))
    }


}