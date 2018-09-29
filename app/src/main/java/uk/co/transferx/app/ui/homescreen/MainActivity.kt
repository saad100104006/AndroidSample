package uk.co.transferx.app.ui.homescreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseActivity

import android.support.design.widget.TabLayout
import kotlinx.android.synthetic.main.activity_home.*

import uk.co.transferx.app.ui.homescreen.adapter.TabAdapter

class MainActivity : BaseActivity() {


    private lateinit var tabAdapter: TabAdapter
    private val tabListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            // no op
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            tabAdapter.setCheckStatus(tab?.position ?: 0, false)
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tabAdapter.setCheckStatus(tab?.position ?: 0, true)
        }

    }

    companion object {

        fun startMainActivity(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        tabAdapter = TabAdapter(fragmentManager = supportFragmentManager, context = this)
        pager.adapter = tabAdapter
        tabs.setupWithViewPager(pager)
        for (i in 0 until tabs.tabCount) {
            val tab = tabs.getTabAt(i)
            tab?.customView = tabAdapter.getTabView(i)
        }
    }

    override fun onStart() {
        super.onStart()
        tabs.addOnTabSelectedListener(tabListener)
    }

    override fun onStop() {
        tabs.removeOnTabSelectedListener(tabListener)
        super.onStop()
    }
}
