package uk.co.transferx.app.ui.landing.presenter
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.landing.LandingContract
import javax.inject.Inject

class LandingPresenter @Inject constructor() :
        BasePresenter<LandingContract.View>(),
        LandingContract.Presenter {
    override fun goToLoginScreen() {
        this.ui?.showLoginScreen()
    }

    override fun goToSignUpScreen() {
        this.ui?.showSignUpScreen()
    }
}