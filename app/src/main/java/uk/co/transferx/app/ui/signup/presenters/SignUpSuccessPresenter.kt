package uk.co.transferx.app.ui.signup.presenters

import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import javax.inject.Inject

class SignUpSuccessPresenter @Inject constructor(): BasePresenter<SignUpSuccessPresenter.SignUpSuccessView>() {

    fun goToAddRecipient(){
        this.ui.showAddRecipient()
    }

    fun goToAddPayment(){
        this.ui.showAddPaymentMethod()
    }

    fun goToMainScreen(){
        this.ui.showMainScreen()
    }

    interface SignUpSuccessView: UI {
        fun showAddRecipient()

        fun showAddPaymentMethod()

        fun showMainScreen()
    }
}