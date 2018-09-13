package uk.co.transferx.app.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object ActivityUtils {
    /**
     * The fragment is added to the container view.
     */
    fun addFragmentToActivity(supportFragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(supportFragmentManager)
        checkNotNull(fragment)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }
}