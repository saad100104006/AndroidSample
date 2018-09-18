package uk.co.transferx.app.ui.signin


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction

import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.signin.fragment.SignInFragment

/**
 * Created by smilevkiy on 13.11.17.
 */

class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as TransferXApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_activity)

        launchFragment()
    }

    private fun launchFragment(){
        val signInFragment = SignInFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container, signInFragment, signInFragment.tag)
        ft.commit()
    }

    /*
        TODO - Delete this method when linking is done. Linking should be done from source, not from
        TODO - target. This way, coupling is decreased
     */
    companion object {

        fun startWelcomeActivity(activity: Activity) {
            val intent = Intent(activity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
