package uk.co.transferx.app.ui.signin

import uk.co.transferx.app.ui.base.UI

interface SignInContract {
    interface View : UI {
        fun goToSignUp()

        fun goToRecoverPassword()

        fun goToMainScreen()

        fun showConnectionError()

        fun showEmailError()

        fun showPasswordError()

        fun showUserNotFound()

        fun showWrongPassword()

        fun changeButtonState(isEnabled: Boolean)
    }

    interface Presenter {
        fun signIn()

        fun validateEmail(email: String)

        fun validatePassword(password: String)

        fun goToRecoverPassword()

        fun goToSignUp()

        fun refreshGenesisToken()
    }
}