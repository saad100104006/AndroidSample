package uk.co.transferx.app.ui.signup.presenters

import javax.inject.Inject

import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.Util

import uk.co.transferx.app.util.Constants.UNDERSCORE

/**
 * Created by sergey on 27/12/2017.
 */

class SignUpStepOnePresenter @Inject
constructor() : BasePresenter<SignUpStepOnePresenter.SignUpStepOneUI>() {
    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null
    private var phoneNumber: String? = null
    private var country: String? = null

    fun setEmail(email: String?) {
        this.email = email
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun setCountry(country: String) {
        this.country = country
    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
        isInputDataValid()
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
        isInputDataValid()
    }

    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        isInputDataValid()
    }

    fun goToNextStep() {
            val sb = firstName + UNDERSCORE + lastName
            ui?.goToNextStep(email, password, sb, phoneNumber, country)
    }

    private fun isInputDataValid() {
            ui?.setButton(Util.validateName(firstName) && Util.validateName(lastName) && Util.validatePhone(phoneNumber))
    }

    interface SignUpStepOneUI : UI {
        fun goToNextStep(email: String?, password: String?, uname: String?, phoneNumber: String?, country: String?)

        fun setButton(isEnabled: Boolean)

        fun showNameError(isError: Boolean)

        fun showLastNameError()

        fun showPhoneNumberError()
    }
}
