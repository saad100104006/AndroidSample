package uk.co.transferx.app.landing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.landing.fragment.LandingFragment
import uk.co.transferx.app.util.ActivityUtils
import javax.inject.Inject


class LandingActivity : BaseActivity() {
    @Inject
    lateinit var injectedFragment: LandingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)

        setContentView(R.layout.activity_landing)
    }

    override fun onResume() {
        super.onResume()

        bindFragment()
    }

    private fun bindFragment() {
        // bind fragment only if it is not already attached
        var fragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (fragment == null) {
            fragment = injectedFragment;
            ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.frameLayout)
        }
    }
}
