package uk.co.transferx.app.transfersummary

import android.os.Bundle
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.TransferXApplication

class TransferSummaryActivity :BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
    }
}