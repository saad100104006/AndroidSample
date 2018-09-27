package uk.co.transferx.app.ui.homescreen

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.TextView
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.homescreen.fragments.FragActivity
import uk.co.transferx.app.ui.homescreen.fragments.FragRecipients
import uk.co.transferx.app.ui.homescreen.fragments.FragSettings
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import kotlinx.android.synthetic.main.activity_home.*
import uk.co.transferx.app.ui.customview.NonSwipeableViewPager
import android.view.LayoutInflater
import android.support.v4.app.FragmentPagerAdapter
import android.view.Gravity

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // TabLayout Part..
        setupViewPager(pager);
        tabs.setupWithViewPager(pager);
        setupTabIcons(tabs);

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //do stuff here

                pager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    val tabOne = tab.customView as TextView
                    setTextViewDrawableColor(tabOne, R.color.amber)
                    tabOne.setTextColor(ContextCompat.getColor(baseContext, R.color.amber))
                    tabs.getTabAt(0)?.setCustomView(tabOne)
                } else if (tab.getPosition() == 1) {
                    val tabTwo = tab.customView as TextView
                    setTextViewDrawableColor(tabTwo, R.color.amber)
                    tabTwo.setTextColor(ContextCompat.getColor(baseContext, R.color.amber))
                    tabs.getTabAt(1)?.setCustomView(tabTwo)
                } else if (tab.getPosition() == 2) {
                    val tabThree = tab.customView as TextView
                    setTextViewDrawableColor(tabThree, R.color.amber)
                    tabThree.setTextColor(ContextCompat.getColor(baseContext, R.color.amber))
                    tabs.getTabAt(2)?.setCustomView(tabThree)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                pager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    val tabOne = tab.customView as TextView
                    setTextViewDrawableColor(tabOne, R.color.grey_text)
                    tabOne.setTextColor(ContextCompat.getColor(baseContext, R.color.grey_text))
                    tabs.getTabAt(0)?.setCustomView(tabOne)
                } else if (tab.getPosition() == 1) {
                    val tabTwo = tab.customView as TextView
                    setTextViewDrawableColor(tabTwo, R.color.grey_text)
                    tabTwo.setTextColor(ContextCompat.getColor(baseContext, R.color.grey_text))
                    tabs.getTabAt(1)?.setCustomView(tabTwo)
                } else if (tab.getPosition() == 2) {
                    val tabThree = tab.customView as TextView
                    setTextViewDrawableColor(tabThree, R.color.grey_text)
                    tabThree.setTextColor(ContextCompat.getColor(baseContext, R.color.grey_text))
                    tabs.getTabAt(2)?.setCustomView(tabThree)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setupViewPager(pager: NonSwipeableViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(FragActivity(), getString(R.string.title_activity))
        adapter.addFrag(FragRecipients(), getString(R.string.title_recipients))
        adapter.addFrag(FragSettings(), getString(R.string.title_settings))
        pager.setAdapter(adapter)
    }

    private fun setupTabIcons(tabs: TabLayout) {

        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_cash, 0, 0)
        tabOne.text = getString(R.string.title_activity)
        tabOne.gravity = Gravity.CENTER_HORIZONTAL
        tabOne.setTextColor(ContextCompat.getColor(baseContext, R.color.amber))
        tabOne.compoundDrawablePadding = 10
        tabs.getTabAt(0)?.setCustomView(tabOne)

        val tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_recipients, 0, 0)
        tabTwo.text = getString(R.string.title_recipients)
        tabTwo.gravity = Gravity.CENTER_HORIZONTAL
        tabTwo.compoundDrawablePadding = 10
        tabs.getTabAt(1)?.setCustomView(tabTwo)

        val tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_settings, 0, 0)
        tabThree.text = getString(R.string.title_settings)
        tabThree.gravity = Gravity.CENTER_HORIZONTAL
        tabThree.compoundDrawablePadding = 10
        tabs.getTabAt(2)?.setCustomView(tabThree)
    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = arrayListOf<Fragment>()
        private val mFragmentTitleList = arrayListOf<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList.get(position)
        }
    }
}
