package uk.co.transferx.app.settings.profile.personaldetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.personal_details_fragment_two.*
import uk.co.transferx.app.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.pojo.Profile
import uk.co.transferx.app.settings.profile.personaldetails.presenter.PersonalDetailsPresenterTwo
import javax.inject.Inject

class PersonalDetailsFragmentTwo : BaseFragment(), PersonalDetailsPresenterTwo.PersonalDetailsUITwo {

    @Inject
    lateinit var presenter: PersonalDetailsPresenterTwo

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

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun setData(profile: Profile) {
        addressOne.setText(profile.addressOne)
        addressTwo.setText(profile.addressTwo)
        cityInput.setText(profile.city)
        postcodeInput.setText(profile.postCode)
        countryInput.setText(profile.country)
    }

    override fun setError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}