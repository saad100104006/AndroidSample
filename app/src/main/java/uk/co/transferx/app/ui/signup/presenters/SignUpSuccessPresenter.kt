package uk.co.transferx.app.ui.signup.presenters

import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import javax.inject.Inject

class SignUpSuccessPresenter @Inject constructor(): BasePresenter<SignUpSuccessPresenter.SignUpSuccessView>() {

    interface SignUpSuccessView: UI {
        fun goToAddRecipient()

        fun goToAddPaymentMethod()
    }
}