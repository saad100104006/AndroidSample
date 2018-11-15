package uk.co.transferx.app.ui.transfer.presenters

import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import javax.inject.Inject

class SelectRecipientPresenter @Inject
constructor(private val recipientRepository: RecipientRepository) :
        BasePresenter<SelectRecipientPresenter.SelectRecipientView>() {

    override fun attachUI(ui: SelectRecipientView?) {
        super.attachUI(ui)

        // Get recipients
    }

    interface SelectRecipientView: UI {
        fun goToNextStep()
    }
}