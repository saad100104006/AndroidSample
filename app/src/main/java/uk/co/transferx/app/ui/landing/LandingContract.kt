package uk.co.transferx.app.ui.landing
import uk.co.transferx.app.ui.base.UI

interface LandingContract {
    interface View: UI {
        fun showLoginScreen()

        fun showSignUpScreen()
    }

    interface Presenter {
        fun goToLoginScreen()

        fun goToSignUpScreen()
    }
}