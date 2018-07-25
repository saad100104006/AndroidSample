package uk.co.transferx.app.transfersummary

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_transfer_summary.*
import kotlinx.android.synthetic.main.schedule_activity_layout.view.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.transfersummary.presenter.TransferSummaryPresenter
import uk.co.transferx.app.util.Constants.TRANSACTION
import uk.co.transferx.app.welcom.WelcomeActivity
import javax.inject.Inject

class TransferSummaryActivity : BaseActivity(), TransferSummaryPresenter.TransferSummaryUI {

    @Inject
    lateinit var presenter: TransferSummaryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_transfer_summary)
        presenter.setData(intent.getParcelableExtra(TRANSACTION))
        sendNow.setOnClickListener { presenter.sendTransfer() }

    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun goBack() {
        finish()
    }

    override fun fillUser(transactionCreate: TransactionCreate?) {
        SendToRecipient.text = transactionCreate?.recipientDto?.fullName
        sendInput.text =
                "${transactionCreate?.baseAmount.toString()} - ${transactionCreate?.currencyInput}"
        receiveInput.text =
                "${transactionCreate?.amount.toString()} - ${transactionCreate?.currency}"
        paymentCard.text = transactionCreate?.card.toString()
        messageInput.text = transactionCreate?.message.orEmpty()
    }

    override fun goToWelcome() {
        WelcomeActivity.startWelcomeActivity(this@TransferSummaryActivity)
    }
}