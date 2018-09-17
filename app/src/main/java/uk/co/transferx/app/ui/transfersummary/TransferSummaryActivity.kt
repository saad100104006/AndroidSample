package uk.co.transferx.app.ui.transfersummary

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_transfer_summary.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.mainscreen.MainActivity
import uk.co.transferx.app.data.pojo.TransactionCreate
import uk.co.transferx.app.ui.transfersummary.presenter.TransferSummaryPresenter
import uk.co.transferx.app.util.Constants
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Constants.TRANSACTION
import uk.co.transferx.app.util.Util
import uk.co.transferx.app.util.Util.formattedDate
import uk.co.transferx.app.ui.signin.SignInActivity
import javax.inject.Inject

class TransferSummaryActivity : BaseActivity(), TransferSummaryPresenter.TransferSummaryUI {

    @Inject
    lateinit var presenter: TransferSummaryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_transfer_summary)
        presenter.setData(intent.getParcelableExtra(TRANSACTION))
        buttonClose.setOnClickListener { presenter.close() }

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
        startActivity(intentFor<MainActivity>().clearTop())
        finish()
    }

    override fun fillData(transactionCreate: TransactionCreate) {
        receiptNumber.text = getString(
            R.string.transfer_receipt_number,
            transactionCreate.transactionNumber ?: EMPTY
        )
        paymentMethodValue.setCompoundDrawablesWithIntrinsicBounds(
            resolveCard(transactionCreate.card?.type),
            0,
            0,
            0
        )
        paymentMethodValue.text = transactionCreate.card.toString()
        name.text = transactionCreate.recipientDto?.fullName
        message.text = transactionCreate.message
        sendToValue.text =
                String.format(
                    "%s - %s",
                    transactionCreate.baseAmount ?: EMPTY,
                    transactionCreate.currencyInput
                )
        receiveValue.text =
                String.format("%s - %s", transactionCreate.amount, transactionCreate.currency)
        repeat.text = if (transactionCreate.repeat) getFormattedRepeat(
            transactionCreate.frequency,
            transactionCreate.endTime
        ) else getString(R.string.none)
        schedule.text =
                if (transactionCreate.sendNow) getString(R.string.send_now) else formattedDate(
                    transactionCreate.startTime
                )
       resolveStatus(transactionCreate)
    }

    private fun resolveStatus(transactionCreate: TransactionCreate) {
        var statusMessage = EMPTY
        when {
            transactionCreate.status.isNullOrEmpty() -> {
                statusMessage =
                        if (transactionCreate.sendNow) getString(R.string.transfer_in_progress) else getString(
                            R.string.transfer_pending
                        )
                status.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0, R.drawable.ic_in_progress, 0
                )
            }
            transactionCreate.status == "PAYIN-FAILED" -> {

                statusMessage = getString(R.string.transfer_error)
                status.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0, R.drawable.ic_error_red, 0
                )
            }
            transactionCreate.status == "PAYIN-SUCCESS" -> {
                statusMessage = getString(R.string.transfer_completed)
                status.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0, R.drawable.ic_complete, 0
                )
            }

        }
        status.text = statusMessage
    }

    private fun getFormattedRepeat(frequency: String?, time: String?): String {
        if (time.isNullOrEmpty()) {
            return String.format("%s & %s", frequency, getString(R.string.never_end))
        }
        return String.format(
            "%s %s %s", frequency, getString(R.string.until),
            Util.formatEndDate(time)
        )
    }

    private fun resolveCard(cardType: String?): Int {
        return if (cardType == Constants.MASTERCARD) R.drawable.ic_master_card else R.drawable.ic_visa
    }

    override fun setExchangeRate(rate: String) {
        ExchangeRateValue.text = rate
    }

    override fun goToWelcome() {
        SignInActivity.startWelcomeActivity(this@TransferSummaryActivity)
    }
}