package uk.co.transferx.app.ui.signup
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
import uk.co.transferx.app.ui.signup.fragment.SignUpSuccessFragment
import uk.co.transferx.app.util.Constants.*

/**
 * Created by smilevkiy on 15.11.17.
 */

class SignUpActivity : BaseActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val sparseArray = SparseArray<BaseFragment>(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.signup_activity_layout)

        val credentials = intent.getIntExtra(SIGNUP_PIN_STEP, 0)
        setUpFragments()
        currentFragment = credentials
        val fragment = sparseArray.get(currentFragment)

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
        if (bundle != null) fragment.arguments = bundle

        replaceFragment(fragment, currentFragment - nextView, R.id.container)
        currentFragment = nextView
    }

    override fun onBackPressed() {
        if (sharedPreferences.getBoolean(PIN_SHOULD_BE_INPUT, false)) return

        when (currentFragment) {
            0 -> {
                finish() }
            1 -> showNextOrPreviousFragment(0, null)
            2 -> showNextOrPreviousFragment(1, null)
            else -> super.onBackPressed()
        }
    }

    private fun setUpFragments(){
        sparseArray.put(0, SignUpStepOneFragment())
        sparseArray.put(1, SignUpStepTwoFragment())
        sparseArray.put(2, SignUpStepThreeFragment())
        sparseArray.put(3, SignUpSuccessFragment())
    }

    companion object {
        private var currentFragment: Int = 0
    }

}
