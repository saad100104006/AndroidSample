package uk.co.transferx.app.signin.signinpin

import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.MockitoAnnotations
import uk.co.transferx.app.data.crypto.CryptoManager
import uk.co.transferx.app.ui.signinpin.presenter.SignInPinPresenter
import uk.co.transferx.app.util.Constants
import uk.co.transferx.app.util.schedulers.ImmediateSchedulerProvider

/**
 * Created by Catalin Ghita on 11/2018
 */

class SignInPinPresenterTest {
    // SUT
    private lateinit var signInPinPresenter: SignInPinPresenter

    @Mock
    lateinit var cryptoManager: CryptoManager

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var view: SignInPinPresenter.SignInPinUI

    @Mock
    lateinit var prefEditor: SharedPreferences.Editor


    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        signInPinPresenter = SignInPinPresenter(cryptoManager, sharedPreferences, ImmediateSchedulerProvider())
    }

    @Test
    fun testSignInPin_success(){
        // Prepare success response
        Mockito.`when`(cryptoManager.getDecryptCredential(any(), any())).thenReturn("Success_response")
        Mockito.`when`(sharedPreferences.getString(Constants.CREDENTIAL, null)).thenReturn("some_credential")

        Mockito.`when`(sharedPreferences.edit()).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.PIN_REQUIRED, false)).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.LOGGED_IN_STATUS, true)).thenReturn(prefEditor)

        signInPinPresenter.attachUI(view)

        // Enter correct PIN
        signInPinPresenter.checkPinAndLogIn("1234")

        // Verify that the editor puts correct values
        Mockito.verify(prefEditor).putBoolean(Constants.PIN_REQUIRED, false)
        Mockito.verify(prefEditor).putBoolean(Constants.LOGGED_IN_STATUS, true)

        // Verify decrypted credential are retrieved
        Mockito.verify(cryptoManager).getDecryptCredential(any(), any())

        // Verify that the view acts accordingly
        Mockito.verify(view).goToPreviousScreen()
    }

    @Test
    fun testSignInPin_failure(){
        // Prepare failure response
        Mockito.`when`(cryptoManager.getDecryptCredential(any(), any())).thenReturn("")
        Mockito.`when`(sharedPreferences.getString(Constants.CREDENTIAL, null)).thenReturn("some_credential")

        Mockito.`when`(sharedPreferences.edit()).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.PIN_REQUIRED, false)).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.LOGGED_IN_STATUS, true)).thenReturn(prefEditor)

        signInPinPresenter.attachUI(view)

        // Enter incorrect PIN
        signInPinPresenter.checkPinAndLogIn("1233")

        // Verify that the editor never acts
        Mockito.verify(prefEditor, never()).putBoolean(Constants.PIN_REQUIRED, false)
        Mockito.verify(prefEditor, never()).putBoolean(Constants.LOGGED_IN_STATUS, true)

        // Verify decrypted credential are retrieved
        Mockito.verify(cryptoManager).getDecryptCredential(any(), any())

        // Verify that the view acts accordingly
        Mockito.verify(view).showErrorPin()
    }

    @Test
    fun testEnterPinCorrectFormat(){
        // First bind to view
        signInPinPresenter.attachUI(view)

        // Passing a valid format PIN
        signInPinPresenter.setPinValue("1122")

        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(true)
    }

    @Test
    fun testEnterPinInvalidFormat(){
        // First bind to view
        signInPinPresenter.attachUI(view)

        // Passing a valid format PIN
        signInPinPresenter.setPinValue("1ff2")

        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)
    }


    @Test
    fun testEnterPinInsufficientInput(){
        // First bind to view
        signInPinPresenter.attachUI(view)

        // Passing a valid format PIN
        signInPinPresenter.setPinValue("12")

        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)
    }


}