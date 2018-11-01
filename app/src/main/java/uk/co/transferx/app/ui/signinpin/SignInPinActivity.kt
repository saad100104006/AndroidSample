package uk.co.transferx.app.ui.signinpin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentTransaction

import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.signinpin.fragment.SignInPinFragment
import uk.co.transferx.app.util.Constants.CLEAN_START

/**
 * Created by sergey on 19.11.17.
 */

class SignInPinActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signpin_activity_layout)

        val signInFragment = SignInPinFragment()
        val bundle = Bundle()
        bundle.putInt(CLEAN_START, intent.getIntExtra(CLEAN_START, -1))
        signInFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, signInFragment, signInFragment.tag).commit()
    }

    override fun onBackPressed() {}
}
