package uk.co.transferx.app.profile.changepassword

import android.content.SharedPreferences
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.transferx.app.data.crypto.CryptoManager
import uk.co.transferx.app.ui.settings.profile.changepin.presenter.ChangePinPresenter
import uk.co.transferx.app.util.Constants
import uk.co.transferx.app.util.schedulers.ImmediateSchedulerProvider

/**
 * Created by Tanvir on 12/2018
 */

class ChangePinPresenterTest {
    // SUT
    private lateinit var changePinPresenter: ChangePinPresenter

    @Mock
    lateinit var cryptoManager: CryptoManager

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var view: ChangePinPresenter.ChangePinUI

    @Mock
    lateinit var prefEditor: SharedPreferences.Editor

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        changePinPresenter = ChangePinPresenter(cryptoManager, ImmediateSchedulerProvider(), sharedPreferences)
    }


    @Test
    fun testChangePin_success(){
        // Prepare success response
        Mockito.`when`(cryptoManager.getDecryptCredential(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn("Success_response")
        Mockito.`when`(sharedPreferences.getString(Constants.CREDENTIAL, null)).thenReturn("some_credential")
        Mockito.`when`(sharedPreferences.edit()).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.PIN_REQUIRED, false)).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.LOGGED_IN_STATUS, true)).thenReturn(prefEditor)

        changePinPresenter.attachUI(view)

        // Enter correct PIN
        changePinPresenter.checkOldPinAndChangePin()

        // Verify that the editor puts correct values
        Mockito.verify(prefEditor).putBoolean(Constants.PIN_REQUIRED, false)
        Mockito.verify(prefEditor).putBoolean(Constants.LOGGED_IN_STATUS, true)

        // Verify decrypted credential are retrieved
        Mockito.verify(cryptoManager).getDecryptCredential(ArgumentMatchers.any(), ArgumentMatchers.any())

        // Verify that the view acts accordingly
        Mockito.verify(view).goToPreviousScreen()
    }


    @Test
    fun testSignInPin_failure(){
        // Prepare failure response
        Mockito.`when`(cryptoManager.getDecryptCredential(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn("")
        Mockito.`when`(sharedPreferences.getString(Constants.CREDENTIAL, null)).thenReturn("some_credential")

        Mockito.`when`(sharedPreferences.edit()).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.PIN_REQUIRED, false)).thenReturn(prefEditor)
        Mockito.`when`(prefEditor.putBoolean(Constants.LOGGED_IN_STATUS, true)).thenReturn(prefEditor)

        changePinPresenter.attachUI(view)

        // Enter incorrect PIN
        changePinPresenter.checkOldPinAndChangePin()

        // Verify that the editor never acts
        Mockito.verify(prefEditor, Mockito.never()).putBoolean(Constants.PIN_REQUIRED, false)
        Mockito.verify(prefEditor, Mockito.never()).putBoolean(Constants.LOGGED_IN_STATUS, true)

        // Verify decrypted credential are retrieved
        Mockito.verify(cryptoManager).getDecryptCredential(ArgumentMatchers.any(), ArgumentMatchers.any())

        // Verify that the view acts accordingly
        Mockito.verify(view).showErrorPin()
    }

    @Test
    fun testEnterPinCorrectFormat(){
        // First bind to view
        changePinPresenter.attachUI(view)

        // Passing a valid format PIN
        changePinPresenter.setPinValue("1122")

        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(true)
    }

    @Test
    fun testEnterPinInvalidFormat(){
        // First bind to view
        changePinPresenter.attachUI(view)

        // Passing a valid format PIN
        changePinPresenter.setPinValue("1ff2")

        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)
    }


    @Test
    fun testEnterPinInsufficientInput(){
        // First bind to view
        changePinPresenter.attachUI(view)

        // Passing a valid format PIN
        changePinPresenter.setPinValue("12")

        // Verify that the view acts accordingly
        Mockito.verify(view).setButtonEnabled(false)
    }


}