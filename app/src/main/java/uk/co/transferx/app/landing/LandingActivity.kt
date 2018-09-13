package uk.co.transferx.app.landing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication

class LandingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)

        setContentView(R.layout.activity_landing)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
