package uk.co.transferx.app.mainscreen.schedule

import android.os.Bundle
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication

class ReviewActivity :BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_review_layout)

    }
}