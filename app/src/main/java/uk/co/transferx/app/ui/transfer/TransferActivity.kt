package uk.co.transferx.app.ui.transfer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.signup.SignUpActivity
import uk.co.transferx.app.ui.transfer.fragment.SelectRecipientFragment

class TransferActivity : BaseActivity() {
    private val fragmentArray = SparseArray<BaseFragment>(4)
    private var currentFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_transfer)

        setupFragments()

        val initialFragment = fragmentArray.get(currentFragment)
        supportFragmentManager.beginTransaction().replace(R.id.container, initialFragment, fragmentArray
                .get(currentFragment).tag).commit()
    }

    override fun onDestroy() {
        currentFragment = 0
        fragmentArray.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {

        when (currentFragment) {
            0 -> finish()
            1 -> showNextOrPreviousFragment(0, null)
            2 -> showNextOrPreviousFragment(1, null)
            3 -> showNextOrPreviousFragment(2, null)
            4 -> showNextOrPreviousFragment(3, null)
            else -> super.onBackPressed()
        }
    }


    fun showNextOrPreviousFragment(nextView: Int, bundle: Bundle?) {
        val fragment = fragmentArray.get(nextView)
        if (bundle != null) fragment.arguments = bundle

        replaceFragment(fragment, currentFragment - nextView, R.id.container)
        currentFragment = nextView
    }

    private fun setupFragments(){
         fragmentArray.put(0, SelectRecipientFragment())
    }
}
