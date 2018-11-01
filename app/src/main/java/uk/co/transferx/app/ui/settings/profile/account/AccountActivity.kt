package uk.co.transferx.app.ui.settings.profile.account

import android.os.Bundle
import kotlinx.android.synthetic.main.account_fragment_layout.*

import javax.inject.Inject

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.ui.settings.profile.account.presenter.SettingsAccountFragmentPresenter

/**
 * Created by HP on 11/1/2018.
 */


class AccountActivity : BaseActivity(), SettingsAccountFragmentPresenter.SettingsAccountFragmentUI  {


    @Inject
    lateinit var presenter: SettingsAccountFragmentPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.account_fragment_layout)

        personal_details.setOnClickListener { presenter.clickUploadDocumentation() }
        wallet.setOnClickListener { presenter.clickUploadDocumentation() }
        change_password.setOnClickListener {  presenter.clickUploadDocumentation() }
        change_pin.setOnClickListener { presenter.clickUploadDocumentation() }
        upload_docs.setOnClickListener { presenter.clickUploadDocumentation() }

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

 /*   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }*/

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
