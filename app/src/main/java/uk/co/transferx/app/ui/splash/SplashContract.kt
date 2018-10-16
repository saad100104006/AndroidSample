package uk.co.transferx.app.ui.splash

import uk.co.transferx.app.ui.base.UI

interface SplashContract {
    interface View : UI {
        fun goToLandingScreen()

        fun goToTutorialScreen()

        fun goToPinScreen()

        fun goToSetPinScreen()
    }
}