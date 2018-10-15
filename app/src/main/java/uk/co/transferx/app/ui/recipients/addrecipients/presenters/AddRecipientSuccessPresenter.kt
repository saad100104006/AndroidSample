package uk.co.transferx.app.ui.recipients.addrecipients.presenters

import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import javax.inject.Inject

class AddRecipientSuccessPresenter @Inject constructor(): BasePresenter<AddRecipientSuccessPresenter.View>() {

    fun goToMakeTransfer(){
        this.ui.showMakeTransfer()
    }

    fun goToMainScreen(){
        this.ui.showMainScreen()
    }

    interface View: UI {
        fun showMakeTransfer()

        fun showMainScreen()
    }
}