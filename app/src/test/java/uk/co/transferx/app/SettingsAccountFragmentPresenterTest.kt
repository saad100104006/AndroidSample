package uk.co.transferx.app

import android.content.SharedPreferences
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.transferx.app.data.pojo.TokenEntity
import uk.co.transferx.app.data.remote.SignInOutApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.settings.presenter.SettingsFragmentPresenter
import uk.co.transferx.app.ui.settings.profile.presenter.SettingsAccountFragmentPresenter


/**
 * Created by Saad on 30/10/18.
 */


class SettingsAccountFragmentPresenterTest {
    // SUT
    lateinit var activityPresenter: SettingsAccountFragmentPresenter



    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        activityPresenter = SettingsAccountFragmentPresenter()
    }

    @Test
    fun testPerformOnclick() {
        //testing click perform


        Mockito.verify(activityPresenter).clickBack()
        Mockito.verify(activityPresenter).clickPersonalInformation()
        Mockito.verify(activityPresenter).clickWallet()
        Mockito.verify(activityPresenter).clickChangePassword()
        Mockito.verify(activityPresenter).clickChangePin()
        Mockito.verify(activityPresenter).clickUploadDocumentation()

    }


}