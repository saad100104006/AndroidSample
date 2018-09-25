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
import android.os.PersistableBundle
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

        tvActivity.setOnClickListener{
            tvActivity.setTextColor(ContextCompat.getColor(this, R.color.amber))
            setTextViewDrawableColor(tvActivity, R.color.amber)
            viewActivityBar.setBackgroundColor(ContextCompat.getColor(this, R.color.amber))

            tvRecipients.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvRecipients, R.color.grey_text)
            viewRecipientsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvSettings, R.color.grey_text)
            viewSettingsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            callFragmentActivity()
        }

        tvRecipients.setOnClickListener {
            tvActivity.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvActivity, R.color.grey_text)
            viewActivityBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvRecipients.setTextColor(ContextCompat.getColor(this, R.color.amber))
            setTextViewDrawableColor(tvRecipients, R.color.amber)
            viewRecipientsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.amber))

            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvSettings, R.color.grey_text)
            viewSettingsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            callFragmentRecipients()
        }

        tvSettings.setOnClickListener{
            tvActivity.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvActivity, R.color.grey_text)
            viewActivityBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvRecipients.setTextColor(ContextCompat.getColor(this, R.color.grey_text))
            setTextViewDrawableColor(tvRecipients, R.color.grey_text)
            viewRecipientsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.amber))
            setTextViewDrawableColor(tvSettings, R.color.amber)
            viewSettingsBar.setBackgroundColor(ContextCompat.getColor(this, R.color.amber))

            callFragmentSettings()
        }

        callFragmentActivity()


        /*// TabLayout Part..
        setupViewPager(pager);
        tabs.setupWithViewPager(pager);
        setupTabIcons(tabs);*/
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

    internal fun callFragmentActivity() {
        replaceFragment(FragActivity(), 0, R.id.containerHome)
    }

    internal fun callFragmentRecipients() {
        replaceFragment(FragRecipients(), 0, R.id.containerHome)
    }

    internal fun callFragmentSettings() {
        replaceFragment(FragSettings(), 0, R.id.containerHome)
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
