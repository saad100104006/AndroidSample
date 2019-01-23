package uk.co.transferx.app.ui.settings.profile.changepin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_pin.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.settings.profile.changepin.fragment.ChangePinFragment

/**
 * Created by Tanvir on 12/1/2018.
 */

class ChangePinActivity : BaseActivity() {

    private var isConfirmationDisplayed: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as TransferXApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        launchFragment(ChangePinFragment())
        button_back_pin.setOnClickListener {  finish() }
    }

    override fun onBackPressed() {
        if(!isConfirmationDisplayed) {
            super.onBackPressed()
            finish()
        }
    }

    fun launchFragment(fragment: BaseFragment) {
        if(fragment is ChangePinFragment) isConfirmationDisplayed = true
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.tag)
                .commit()
    }

    companion object {

        fun startSignInActivity(activity: Activity) {
            val intent = Intent(activity, ChangePinActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            activity.finish()
        }
    }

}

