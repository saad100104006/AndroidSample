package uk.co.transferx.app.transfersummary

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_transfer_summary.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.mainscreen.MainActivity
import uk.co.transferx.app.pojo.TransactionCreate
import uk.co.transferx.app.transfersummary.presenter.TransferSummaryPresenter
import uk.co.transferx.app.util.Constants
import uk.co.transferx.app.util.Constants.EMPTY
import uk.co.transferx.app.util.Constants.TRANSACTION
import uk.co.transferx.app.util.Util
import uk.co.transferx.app.util.Util.formattedDate
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
                    transactionCreate.baseAmount,
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
        status.text =
                if (transactionCreate.sendNow) getString(R.string.transfer_in_progress) else getString(
                    R.string.transfer_pending
                )
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
        WelcomeActivity.startWelcomeActivity(this@TransferSummaryActivity)
    }
}