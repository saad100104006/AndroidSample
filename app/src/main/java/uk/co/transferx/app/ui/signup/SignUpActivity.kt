package uk.co.transferx.app.ui.signup

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.SparseArray

import javax.inject.Inject

import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.signup.fragment.SignUpStepOneFragment
import uk.co.transferx.app.ui.signup.fragment.SignUpStepThreeFragment
import uk.co.transferx.app.ui.signup.fragment.SignUpStepTwoFragment
import uk.co.transferx.app.ui.welcom.WelcomeActivity

import uk.co.transferx.app.util.Constants.CREDENTIAL
import uk.co.transferx.app.util.Constants.EMAIL
import uk.co.transferx.app.util.Constants.PASSWORD
import uk.co.transferx.app.util.Constants.PIN_SHOULD_BE_INPUT

/**
 * Created by smilevkiy on 15.11.17.
 */

class SignUpActivity : BaseActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val sparseArray = SparseArray<BaseFragment>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)

        val credentials = intent.getBundleExtra(CREDENTIAL)
        setContentView(R.layout.signup_activity_layout)

        setUpFragments()

        val fragment = sparseArray.get(currentFragment)
        if (credentials != null) fragment.arguments = credentials

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, sparseArray
                .get(currentFragment).tag).commit()
    }

    override fun onDestroy() {
        currentFragment = 0
        sparseArray.clear()
        super.onDestroy()
    }

    fun showNextOrPreviousFragment(nextView: Int, bundle: Bundle?) {
        val fragment = sparseArray.get(nextView)
        if (bundle != null) {
            fragment.arguments = bundle
        }
        replaceFragment(fragment, currentFragment - nextView, R.id.container)
        currentFragment = nextView
    }

    override fun onBackPressed() {
        if (sharedPreferences.getBoolean(PIN_SHOULD_BE_INPUT, false)) return

        when (currentFragment) {
            0 -> {
                startActivity(Intent(this@SignUpActivity, WelcomeActivity::class.java))
                finish()
            }
            1 -> showNextOrPreviousFragment(0, null)
            2 -> showNextOrPreviousFragment(1, null)
            else -> super.onBackPressed()
        }
    }

    private fun setUpFragments(){
//        sparseArray.put(0, SignUpStepOneFragment())
//        sparseArray.put(1, SignUpStepTwoFragment())
        sparseArray.put(1, SignUpStepOneFragment())
        sparseArray.put(0, SignUpStepTwoFragment())
        sparseArray.put(2, SignUpStepThreeFragment())
    }

    companion object {
        private var currentFragment: Int = 0

        // Linking methods
        // SHOULD NOT BE USED in order to externalize dependencies
        // TODO When linking is done, these methods should be refined/deleted
        fun startSignUp(activity: Activity, bundle: Bundle?) {
            val intent = Intent(activity, SignUpActivity::class.java)
            if (bundle != null) {
                intent.putExtra(CREDENTIAL, bundle)
            }
            activity.startActivity(intent)
            activity.finish()
        }

        fun startSignUp(activity: Activity, fragmentNumber: Int, email: String, password: String) {
            val bundle = Bundle()
            bundle.putString(EMAIL, email)
            bundle.putString(PASSWORD, password)
            bundle.putBoolean(PIN_SHOULD_BE_INPUT, true)
            currentFragment = fragmentNumber
            startSignUp(activity, bundle)
        }
    }

}
