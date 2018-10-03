package uk.co.transferx.app.ui.settings.profile.personaldetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.personal_details_fragment_one.*
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.data.pojo.Profile
import uk.co.transferx.app.ui.settings.profile.personaldetails.presenter.PersonalDetailsPresenterOne
import uk.co.transferx.app.ui.signin.SignInActivity
import javax.inject.Inject

class PersonalDetailsFragmentOne : BaseFragment(), PersonalDetailsPresenterOne.PersonalDetailsOneUI {

    @Inject
    lateinit var presenter: PersonalDetailsPresenterOne

    override fun tagName(): String {
        return PersonalDetailsFragmentOne::class.simpleName as String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.personal_details_fragment_one, container, false)
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
        firstName.setText(profile.firstName)
        lastName.setText(profile.lastName)
        emailInput.setText(profile.email)
        phoneInput.setText(profile.phone)
    }

    override fun setError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToWelcome() {
//        SignInActivity.startSignInActivity(activity)
    }
}
