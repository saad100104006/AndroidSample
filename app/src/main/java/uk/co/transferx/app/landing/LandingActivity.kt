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

        // bind fragment only if it is not already attached
        var fragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (fragment == null) {
            fragment = injectedFragment;
            ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.frameLayout)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {

        fun startLandingActivity(activity: Activity) {
            val intent = Intent(activity, LandingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            activity.finish()
        }
    }

}
