package uk.co.transferx.app.landing

import uk.co.transferx.app.UI

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