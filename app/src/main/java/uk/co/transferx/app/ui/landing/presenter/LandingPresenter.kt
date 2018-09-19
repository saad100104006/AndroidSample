package uk.co.transferx.app.landing.presenter

import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.landing.LandingContract
import javax.inject.Inject

class LandingPresenter @Inject constructor() :
        BasePresenter<LandingContract.View>(),
        LandingContract.Presenter {
    override fun goToLoginScreen() {
        this.ui.showLoginScreen()
    }

    override fun goToSignUpScreen() {
        this.ui.showSignUpScreen()
    }
}