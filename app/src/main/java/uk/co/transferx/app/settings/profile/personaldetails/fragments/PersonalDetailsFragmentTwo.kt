package uk.co.transferx.app.settings.profile.personaldetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.transferx.app.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication

class PersonalDetailsFragmentTwo : BaseFragment() {
    override fun tagName(): String {
        return PersonalDetailsFragmentTwo::class.simpleName as String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.personal_details_fragment_two, container, false)
    }

}