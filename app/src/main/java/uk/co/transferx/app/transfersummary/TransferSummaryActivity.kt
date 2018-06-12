package uk.co.transferx.app.transfersummary

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_transfer_summary.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.transfersummary.presenter.TransferSummaryPresenter
import uk.co.transferx.app.util.Constants.TRANSACTION
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
}