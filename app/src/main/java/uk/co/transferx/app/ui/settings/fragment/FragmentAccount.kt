package uk.co.transferx.app.ui.settings.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.frag_settings.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.mainscreen.fragments.RecipientsFragment
import uk.co.transferx.app.ui.settings.notification.NotificationSettingsActivity
import uk.co.transferx.app.ui.settings.presenter.ConfirmationDialogLogoutFragment
import uk.co.transferx.app.ui.settings.presenter.SettingsAccountFragmentPresenter
import uk.co.transferx.app.ui.settings.presenter.SettingsFragmentPresenter
import uk.co.transferx.app.ui.settings.profile.ProfileActivity
import uk.co.transferx.app.ui.settings.support.SupportActivity
import uk.co.transferx.app.ui.signin.SignInActivity
import javax.inject.Inject

/**
 * Created by HP on 10/31/2018.
 */

class FragmentAccount : BaseFragment(), SettingsAccountFragmentPresenter.SettingsAccountFragmentUI  {



    @Inject
    lateinit var presenter: SettingsAccountFragmentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_settings, container, false)
    }

    override fun tagName(): String {
        return FragSettings::class.java.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuAccount.setOnClickListener { presenter.clickUploadDocumentation() }
        menuLogout.setOnClickListener { showDialogConfirmation() }
        menuNotification.setOnClickListener {  presenter.clickUploadDocumentation() }
        menuReferrals.setOnClickListener { presenter.clickUploadDocumentation() }
        menuSupport.setOnClickListener { presenter.clickUploadDocumentation() }
        menuAboutUs.setOnClickListener { presenter.clickUploadDocumentation() }

    }

    private fun showDialogConfirmation() {
        val dialogFragment = ConfirmationDialogLogoutFragment()
        val bundle = Bundle()
        bundle.putString(
                ConfirmationDialogLogoutFragment.MESSAGE,
                getString(R.string.cancel_transfer_message)
        )
        // bundle.putInt(POSITION, position)
        dialogFragment.arguments = bundle
        dialogFragment.setTargetFragment(this, RecipientsFragment.DELETE_USER)
        dialogFragment.isCancelable = false
        dialogFragment.show(fragmentManager!!, "TAG")
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachUI()

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

    override fun goToPersonalInformation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToWelcome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}