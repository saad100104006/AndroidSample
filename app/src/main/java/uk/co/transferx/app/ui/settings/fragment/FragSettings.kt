package uk.co.transferx.app.ui.settings.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity.RESULT_OK
import kotlinx.android.synthetic.main.frag_settings.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.mainscreen.fragments.RecipientsFragment.DELETE_USER
import uk.co.transferx.app.ui.settings.profile.account.PersonalInformationActivity
import uk.co.transferx.app.ui.settings.notification.NotificationSettingsActivity
import uk.co.transferx.app.ui.settings.presenter.ConfirmationDialogLogoutFragment
import uk.co.transferx.app.ui.settings.presenter.SettingsFragmentPresenter
import uk.co.transferx.app.ui.settings.profile.ProfileActivity
import uk.co.transferx.app.ui.settings.support.SupportActivity
import uk.co.transferx.app.ui.signin.SignInActivity
import javax.inject.Inject

class FragSettings : BaseFragment(), SettingsFragmentPresenter.SettingsFragmentUI  {

   @Inject
    lateinit var presenter: SettingsFragmentPresenter

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

        menuAccount.setOnClickListener { presenter.accountClicked() }
        menuLogout.setOnClickListener { showDialogConfirmation() }
        menuNotification.setOnClickListener {  presenter.clickNotification() }
        menuReferrals.setOnClickListener { presenter.clickProfile() }
        menuSupport.setOnClickListener { presenter.supportClicked() }
        menuAboutUs.setOnClickListener { presenter.supportClicked() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK){
            presenter.logOut()
        }
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
        dialogFragment.setTargetFragment(this, DELETE_USER)
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

    override fun goToAccount() {
        startActivity(Intent(context, PersonalInformationActivity::class.java))
    }

    override fun goToSupport() {
        startActivity(Intent(context, SupportActivity::class.java))
    }

    override fun goToProfile() {
        startActivity(Intent(context, ProfileActivity::class.java))
    }

    override fun goAppSettings() {
        startActivity(Intent(context, NotificationSettingsActivity::class.java))

    }

    override fun goToWelcome() {
        startActivity(Intent(context, SignInActivity::class.java))

    }


}
