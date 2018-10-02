package uk.co.transferx.app.ui.mainscreen.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import kotlinx.android.synthetic.main.activity_review_layout.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.mainscreen.MainActivityOld
import uk.co.transferx.app.ui.mainscreen.schedule.presenter.ReviewPresenter
import uk.co.transferx.app.data.pojo.TransactionCreate
import uk.co.transferx.app.ui.transfersummary.TransferSummaryActivity
import uk.co.transferx.app.util.Constants.DELETE
import uk.co.transferx.app.util.Constants.MASTERCARD
import uk.co.transferx.app.util.Constants.TRANSACTION
import uk.co.transferx.app.util.Util.formatEndDate
import uk.co.transferx.app.util.Util.formattedDate
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment
import uk.co.transferx.app.ui.welcom.WelcomeActivity
import javax.inject.Inject

class ReviewActivity : BaseActivity(), ReviewPresenter.ReviewUI {

    @Inject
    lateinit var presenter: ReviewPresenter
    private lateinit var localBroadcastReceiver: BroadcastReceiver
    private var isRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_review_layout)
        presenter.setTransaction(intent.getParcelableExtra(TRANSACTION))
        buttonBackReview.setOnClickListener { onBackPressed() }
        buttonCancel.setOnClickListener { showDialogConfirmation() }
        buttonSubmit.setOnClickListener { presenter.sendTransfer() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (isRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver)
            isRegistered = false
        }
    }

    private fun resolveCard(cardType: String?): Int {
        return if (cardType == MASTERCARD) R.drawable.ic_master_card else R.drawable.ic_visa
    }

    private fun showDialogConfirmation() {
        localBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == null) {
                    return
                }
                if (intent.action == DELETE) {
                    startActivity(intentFor<MainActivityOld>().clearTop())
                }
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            localBroadcastReceiver,
            IntentFilter(DELETE)
        )
        isRegistered = true
        val dialogFragment = ConfirmationDialogFragment()
        val bundle = Bundle()
        bundle.putString(
            ConfirmationDialogFragment.MESSAGE,
            getString(R.string.cancel_transfer_message)
        )
        dialogFragment.arguments = bundle
        dialogFragment.isCancelable = false
        dialogFragment.show(supportFragmentManager, "TAG")
    }


    override fun setDataToSummary(transaction: TransactionCreate) {
        groupSchedule.visibility = if(transaction.sendNow && !transaction.repeat) View.GONE else View.VISIBLE
        paymentMethodValue.setCompoundDrawablesWithIntrinsicBounds(
            resolveCard(transaction.card?.type),
            0,
            0,
            0
        )
        paymentMethodValue.text = transaction.card.toString()
        name.text = transaction.recipientDto?.fullName
        message.text = transaction.message
        sendToValue.text =
                String.format("%s - %s", transaction.baseAmount, transaction.currencyInput)
        receiveValue.text = String.format("%s - %s", transaction.amount, transaction.currency)
        repeat.text = if (transaction.repeat) getFormattedRepeat(
            transaction.frequency,
            transaction.endTime
        ) else getString(R.string.none)
        schedule.text =
                if (transaction.sendNow) getString(R.string.send_now) else formattedDate(transaction.startTime)

    }

    private fun getFormattedRepeat(frequency: String?, time: String?): String {
        if (time.isNullOrEmpty()) {
            return String.format("%s & %s", frequency, getString(R.string.never_end))
        }
        return String.format("%s %s %s", frequency, getString(R.string.until), formatEndDate(time))
    }

    override fun setExchangeRate(rate: String) {
        ExchangeRateValue.text = rate
    }

    override fun goToTransferReceipt(transaction: TransactionCreate) {
        startActivity<TransferSummaryActivity>(TRANSACTION to transaction)
        finishAffinity()
    }

    override fun goToWelcome() {
        WelcomeActivity.startWelcomeActivity(this@ReviewActivity)
    }
}