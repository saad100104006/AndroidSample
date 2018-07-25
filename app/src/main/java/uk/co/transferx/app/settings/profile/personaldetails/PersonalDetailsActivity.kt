package uk.co.transferx.app.settings.profile.personaldetails

import android.os.Bundle
import kotlinx.android.synthetic.main.personal_details_activity_layout.*
import uk.co.transferx.app.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.pojo.Profile
import uk.co.transferx.app.settings.profile.personaldetails.adapter.PersonalDetailsAdapter
import uk.co.transferx.app.settings.profile.personaldetails.presenter.PersonalDetailsPresenter
import uk.co.transferx.app.welcom.WelcomeActivity
import javax.inject.Inject

class PersonalDetailsActivity : BaseActivity(), PersonalDetailsPresenter.PersonalDetailsUI {

    @Inject
    lateinit var presenter: PersonalDetailsPresenter
    lateinit var personalDetailsAdapter: PersonalDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.personal_details_activity_layout)
        ButtonBack.setOnClickListener({ onBackPressed() })
        personalDetailsAdapter = PersonalDetailsAdapter(supportFragmentManager)
        ViewPager.setPagingEnabled(true)
        ViewPager.adapter = personalDetailsAdapter
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun goToWelcome() {
        WelcomeActivity.startWelcomeActivity(this@PersonalDetailsActivity)
    }
}