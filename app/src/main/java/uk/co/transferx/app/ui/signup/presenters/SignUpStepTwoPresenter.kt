package uk.co.transferx.app.ui.signup.presenters

import javax.inject.Inject

import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.Util

/**
 * Created by sergey on 08.12.17.
 */

class SignUpStepTwoPresenter @Inject
constructor() : BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI>() {

    private var email: String? = null
    private var password: String? = null

    private var rePassword: String? = null

    private val isInputCorrect: Boolean
        get() = Util.validatePassword(password) && Util.validateEmail(email)

    private val isPasswordInputCorrect: Boolean
        get() = password == rePassword

    fun goToNextStep() {
            if(isPasswordInputCorrect) ui?.goToNextView(email, password)
            else ui.showErrorPassword()
    }

    fun setEmail(email: String) {
        this.email = email
        validateInputData()
    }

    fun setPassword(password: String) {
        this.password = password
        validateInputData()
    }

    fun setRePassword(rePassword: String) {
        this.rePassword = rePassword
        validateInputData()
    }

    private fun validateInputData() {
            ui?.setStateButton(isInputCorrect)
    }

    interface SignUpStepTwoUI : UI {

        fun goToNextView(email: String?, password: String?)

        fun showErrorPassword()

        fun setStateButton(isEnabled: Boolean)

    }
}
