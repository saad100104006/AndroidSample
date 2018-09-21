package uk.co.transferx.app.ui.recoverpass

import uk.co.transferx.app.ui.base.UI

interface RecoverPasswordContract {
    interface View: UI {
        fun unlockButton()

        fun goToSuccessScreen()

        fun lockButton()

        fun goBackToMain()

        fun error()
    }

    interface Presenter {
        fun validateEmail(email: String)

        fun sendEmail()

        fun goBack()

        fun handleError(throwable: Throwable)
    }
}