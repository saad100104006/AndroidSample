package uk.co.transferx.app.ui.recipients.addrecipients.presenters

import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import javax.inject.Inject

class AddFirstRecipientSuccessPresenter @Inject constructor(): BasePresenter<AddFirstRecipientSuccessPresenter.View>() {

    fun goToAddRecipient(){
        this.ui.showAddRecipient()
    }

    fun goToAddPayment(){
        this.ui.showAddPaymentMethod()
    }

    fun goToMainScreen(){
        this.ui.showMainScreen()
    }

    interface View: UI {
        fun showAddRecipient()

        fun showAddPaymentMethod()

        fun showMainScreen()
    }
}