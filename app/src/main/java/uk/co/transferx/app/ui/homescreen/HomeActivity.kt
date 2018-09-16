package uk.co.transferx.app.ui.homescreen

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.homescreen.fragments.FragActivity
import uk.co.transferx.app.ui.homescreen.fragments.FragRecipients
import uk.co.transferx.app.ui.homescreen.fragments.FragSettings

class HomeActivity : BaseActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                callFragmentActivity()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                callFragmentRecipients()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                callFragmentSettings()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        BottomNavigationHelper.disableShiftMode(navigation)

        callFragmentActivity()
    }

    internal fun callFragmentActivity() {
        replaceFragment(FragActivity(), 0, R.id.container_home)
    }

    internal fun callFragmentRecipients() {
        replaceFragment(FragRecipients(), 0, R.id.container_home)
    }

    internal fun callFragmentSettings() {
        replaceFragment(FragSettings(), 0, R.id.container_home)
    }
}
