package uk.co.transferx.app.ui.settings.profile.account

import android.os.Bundle

import javax.inject.Inject

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.settings.profile.account.presenter.SettingsAccountFragmentPresenter

/**
 * Created by Tanvir on 11/1/2018.
 */


class PersonalInformationActivity : BaseActivity(), SettingsAccountFragmentPresenter.SettingsAccountFragmentUI  {


    @Inject
    lateinit var presenter: SettingsAccountFragmentPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.personal_layout)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attachUI(this)
    }

    override fun onPause() {
        presenter!!.detachUI()
        super.onPause()
    }

    override fun goToWelcome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToPersonalInformation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToWallet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToChangePassword() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToChangePin() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToUploadDocumentation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
