package uk.co.transferx.app.ui.homescreen.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.homescreen.fragments.FragActivity
import uk.co.transferx.app.ui.homescreen.fragments.FragRecipients
import uk.co.transferx.app.ui.homescreen.fragments.FragSettings

class TabAdapter(fragmentManager: FragmentManager, val context: Context) :
    FragmentPagerAdapter(fragmentManager) {

    private val tabTitles = context.resources.getStringArray(R.array.tabs)
    private val tabAdapterItems = SparseArray<View>(3)
    private val fragments = SparseArray<Fragment>(3)

    init {
        fragments.append(0, FragActivity())
        fragments.append(1, FragRecipients())
        fragments.append(2, FragSettings())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    fun getTabView(position: Int): View {
        val tab = LayoutInflater.from(context).inflate(R.layout.tab_layout, null)
        val title = tab.findViewById<TextView>(R.id.tabTitle)
        val img = tab.findViewById<ImageView>(R.id.icon)
        img.setImageDrawable(ContextCompat.getDrawable(context, getIcon(position)))
        title.text = tabTitles[position]
        tabAdapterItems.put(position, tab)
        if (position == 0) {
            setCheckStatus(position, true)
        }
        return tab
    }

    fun setCheckStatus(position: Int, status: Boolean) {
        val view = tabAdapterItems.get(position)
        val title = view.findViewById<TextView>(R.id.tabTitle)
        val img = view.findViewById<ImageView>(R.id.icon)
        img.setColorFilter(
            if (status) ContextCompat.getColor(
                context,
                R.color.amber
            ) else ContextCompat.getColor(context, R.color.color_unselected)
        )
        title.setTextColor(
            if (status) ContextCompat.getColor(
                context,
                R.color.amber
            ) else ContextCompat.getColor(context, R.color.color_unselected)
        )
    }

    private fun getIcon(position: Int): Int {
        return when (position) {
            0 -> R.drawable.home_cash
            1 -> R.drawable.home_recipients
            2 -> R.drawable.home_settings
            else -> throw IllegalStateException("Illegal state is $position")
        }
    }
}