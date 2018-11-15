package uk.co.transferx.app.ui.transfer.presenters

import uk.co.transferx.app.data.pojo.Recipient
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
        val list = ArrayList<Recipient>()
        val recipient1 =   Recipient.Builder()
                .firstname("Anna")
                .lastname("Frank")
                .country("XX")
                .phone("39348")
                .build()
        val recipient2 =   Recipient.Builder()
                .firstname("Britney")
                .lastname("Nest")
                .country("XX")
                .phone("39348")
                .build()
        val recipient3 =   Recipient.Builder()
                .firstname("John")
                .lastname("Wick")
                .country("XX")
                .phone("39348")
                .build()
        val recipient4 =   Recipient.Builder()
                .firstname("Welney")
                .lastname("Pardy")
                .country("XX")
                .phone("39348")
                .build()

        list.add(recipient1)
        list.add(recipient2)
        list.add(recipient3)
        list.add(recipient4)

        this.ui.showRecipientList(list)
    }

    interface SelectRecipientView: UI {
        fun goToNextStep()

        fun showRecipientList(recipients: List<Recipient>)
    }
}