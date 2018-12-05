package uk.co.transferx.app.ui.settings.profile

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.account_fragment_layout.*

import javax.inject.Inject
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.settings.profile.account.PersonalInformationActivity
import uk.co.transferx.app.ui.settings.profile.presenter.SettingsAccountFragmentPresenter
import uk.co.transferx.app.ui.settings.profile.changepassword.ChangePasswordActivity
import uk.co.transferx.app.ui.settings.profile.changepin.ChangePinActivity
import uk.co.transferx.app.ui.settings.profile.changepin.ChangePinActivity_MembersInjector
import uk.co.transferx.app.ui.settings.profile.personaldetails.PersonalDetailsActivity
import uk.co.transferx.app.ui.settings.profile.wallet.WalletActivity
import uk.co.transferx.app.ui.signin.SignInActivity

/**
 * Created by Tanvir on 11/1/2018.
 */


class AccountActivity : BaseActivity(), SettingsAccountFragmentPresenter.SettingsAccountFragmentUI {



    @Inject
    lateinit var presenter: SettingsAccountFragmentPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.account_fragment_layout)


        buttonsBack.setOnClickListener({ presenter.clickBack() })
        personalDetails.setOnClickListener { presenter.clickPersonalInformation() }
        wallet.setOnClickListener { presenter.clickWallet() }
        changePassword.setOnClickListener { presenter.clickChangePassword() }
        changePin.setOnClickListener { presenter.clickChangePin() }
        uploadDocs.setOnClickListener { presenter.clickUploadDocumentation() }


    }


    override fun goToBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }


    override fun goToPersonalInformation() {
        startActivity(Intent(this, PersonalDetailsActivity::class.java))
    }


    override fun goToWallet() {

        startActivity(Intent(this, WalletActivity::class.java))

    }

    override fun goToChangePassword() {

        startActivity(Intent(this, ChangePasswordActivity::class.java))

    }

    override fun goToChangePin() {
        startActivity(Intent(this, ChangePinActivity::class.java))
    }

    override fun goToUploadDocumentation() {
        startActivity(Intent(this, UploadDocumentsActivity::class.java))
    }


    override fun goToWelcome() {
        SignInActivity.startSignInActivity(this@AccountActivity)
    }

}
