package uk.co.transferx.app.tutorial.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import uk.co.transferx.app.R
import uk.co.transferx.app.tutorial.fragments.TutorialFragment
import uk.co.transferx.app.util.Constants.*

/**
 * Created by sergey on 21/03/2018.
 */

class TutorialAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt(BUTTON_TEXT, R.string.skip)

        when (position) {
            FIRST_PAGE -> {
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_security)
                bundle.putInt(TITLE, R.string.security)
                bundle.putInt(DESCRIPTION, R.string.security_desc)
                bundle.putInt(BACKGROUND_COLOR, R.color.light_blue_tutorial)
            }
            SECOND_PAGE -> {
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_auto_transfers)
                bundle.putInt(TITLE, R.string.automated_transfer)
                bundle.putInt(DESCRIPTION, R.string.second_page_desc)
                bundle.putInt(BACKGROUND_COLOR, R.color.dark_blue_tutorial)
            }
            THIRD_PAGE -> {
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_recipients)
                bundle.putInt(TITLE, R.string.recipients)
                bundle.putInt(DESCRIPTION, R.string.three_page_desc)
                bundle.putInt(BACKGROUND_COLOR, R.color.black_tutorial)
            }
            FORTH_PAGE -> {
                bundle.putInt(TUTORIAL_IMG, R.drawable.ic_credit_card)
                bundle.putInt(TITLE, R.string.title_flex)
                bundle.putInt(DESCRIPTION, R.string.flexible_payment_desc)
                bundle.putInt(BACKGROUND_COLOR, R.color.purple_tutorial)
                bundle.putInt(BUTTON_TEXT, R.string.get_started)
            }
            else -> throw IllegalStateException("item is illegal$position")
        }

        val currentTutorialFragment = TutorialFragment()
        currentTutorialFragment.arguments = bundle

        return currentTutorialFragment
    }

    override fun getCount(): Int {
        return PAGES
    }

    companion object {
        private const val PAGES = 4
        private const val FIRST_PAGE = 0
        private const val SECOND_PAGE = 1
        private const val THIRD_PAGE = 2
        private const val FORTH_PAGE = 3
    }
}
