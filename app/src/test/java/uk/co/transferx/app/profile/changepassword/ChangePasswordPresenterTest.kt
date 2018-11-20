package uk.co.transferx.app.profile.changepassword

import android.content.SharedPreferences
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.transferx.app.data.pojo.ChangePassword
import uk.co.transferx.app.data.pojo.TokenEntity
import uk.co.transferx.app.data.remote.ProfileApi
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.settings.profile.changepassword.presenter.ChangePasswordPresenter

/**
 * Created by Tanvir on 11/2018
 */

class ChangePasswordPresenterTest {
    // SUT
    private lateinit var changePasswordPresenter: ChangePasswordPresenter
    @Mock
    lateinit var sharedPreferences: SharedPreferences
    @Mock
    lateinit var view: ChangePasswordPresenter.ChangePasswordUI
    @Mock
    lateinit var tokenManager: TokenManager
    @Mock
    lateinit var profileApi: ProfileApi

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        changePasswordPresenter = ChangePasswordPresenter(tokenManager, profileApi, sharedPreferences)
    }

    @Test
    fun testSavePassword_success(){
        // Prepare success response
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))
        Mockito.`when`(profileApi.changePassword("token", ChangePassword("","")))

        changePasswordPresenter.attachUI(view)
        // Enter correct PIN
        changePasswordPresenter.saveNewPassword()
        // Verify that the view acts accordingly
        Mockito.verify(view).goBackToSettings()

    }

    @Test
    fun testSavePassword_failure(){
        // Prepare success response
        Mockito.`when`(tokenManager.token).thenReturn(Single.just(
                TokenEntity("token", 1233, 142, "refresh_token")))
        Mockito.`when`(profileApi.changePassword("token", ChangePassword("","")))


        changePasswordPresenter.attachUI(view)
        // Enter wrong Password
        changePasswordPresenter.saveNewPassword()


    }

    @Test
    fun testCurrentPassword_correct(){

        changePasswordPresenter.attachUI(view)
        // Enter correct PIN
        changePasswordPresenter.setCurrentPassword("qwerty")
        // validate input
        changePasswordPresenter.validateInput()
        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(true)

    }


    @Test
    fun testCurrentPassword_wrong(){

        changePasswordPresenter.attachUI(view)
        // Enter correct PIN
        changePasswordPresenter.setCurrentPassword("qw")
        // validate input
        changePasswordPresenter.validateInput()
        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)

    }



    @Test
    fun testNewPassword_correct(){

        changePasswordPresenter.attachUI(view)
        // Enter correct PIN
        changePasswordPresenter.setNewPassword("123456")
        // validate input
        changePasswordPresenter.validateInput()
        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(true)

    }

    @Test
    fun testNewPassword_wrong(){

        changePasswordPresenter.attachUI(view)
        // Enter correct Password
        changePasswordPresenter.setNewPassword("12")
        // validate input
        changePasswordPresenter.validateInput()
        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)

    }


    @Test
    fun testConfirmPassword_correct(){

        changePasswordPresenter.attachUI(view)
        // Enter correct Password
        changePasswordPresenter.setConfirmPassword("123456")
        //validate input
        changePasswordPresenter.validateInput()
        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(true)
    }

    @Test
    fun testConfirmPassword_wrong(){

        changePasswordPresenter.attachUI(view)
        // Enter correct PIN
        changePasswordPresenter.setConfirmPassword("11")
        // validate input
        changePasswordPresenter.validateInput()
        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)

    }


    @Test
    fun testEnterPasswordInsufficientInput(){
        // First bind to view
        changePasswordPresenter.attachUI(view)
        // Passing a valid format Password
        changePasswordPresenter.setConfirmPassword("1")
        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)
    }



}