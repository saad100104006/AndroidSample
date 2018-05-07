package uk.co.transferx.app.settings.profile.personaldetails.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import uk.co.transferx.app.BaseFragment
import uk.co.transferx.app.pojo.Profile
import uk.co.transferx.app.settings.profile.personaldetails.fragments.PersonalDetailsFragmentOne
import uk.co.transferx.app.settings.profile.personaldetails.fragments.PersonalDetailsFragmentTwo

class PersonalDetailsAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var profile: Profile? = null

    var fragments: SparseArray<BaseFragment> = SparseArray()

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size()
    }

    init {
        fragments.put(0, PersonalDetailsFragmentOne())
        fragments.put(1, PersonalDetailsFragmentTwo())
    }

}