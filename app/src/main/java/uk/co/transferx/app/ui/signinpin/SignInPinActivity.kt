package uk.co.transferx.app.ui.signinpin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction

import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.signinpin.fragment.SignInPinFragment

/**
 * Created by sergey on 19.11.17.
 */

class SignInPinActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signpin_activity_layout)
        val signInFragment = SignInPinFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, signInFragment, signInFragment.tag).commit()
    }


    override fun onBackPressed() {}

    companion object {

        private val TYPE_SIGNIN = "type_signin"


        fun starSignInActivity(activity: Activity) {

            val intent = Intent(activity, SignInPinActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity.startActivity(intent)
            //   activity.finish();

        }
    }
}
