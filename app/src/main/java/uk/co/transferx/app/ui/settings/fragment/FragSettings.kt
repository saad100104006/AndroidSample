package uk.co.transferx.app.ui.settings.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity.RESULT_OK
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment
import uk.co.transferx.app.ui.mainscreen.fragments.RecipientsFragment.DELETE_USER
import uk.co.transferx.app.ui.settings.notification.NotificationSettingsActivity
import uk.co.transferx.app.ui.settings.presenter.ConfirmationDialogLogoutFragment
import uk.co.transferx.app.ui.settings.presenter.SettingsFragmentPresenter
import uk.co.transferx.app.ui.settings.profile.ProfileActivity
import uk.co.transferx.app.ui.settings.support.SupportActivity
import uk.co.transferx.app.ui.signin.SignInActivity
import uk.co.transferx.app.util.Constants
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

        view.findViewById<View>(R.id.menuAccount).setOnClickListener { view -> presenter.clickNotification() }
        view.findViewById<View>(R.id.menuLogout).setOnClickListener {
            showDialogConfirmation()

            //view -> presenter.logOut()
            //
            }
        view.findViewById<View>(R.id.menuNotification).setOnClickListener { view -> presenter.clickNotification() }
        view.findViewById<View>(R.id.menuReferrals).setOnClickListener { view -> presenter.clickProfile() }
        view.findViewById<View>(R.id.menuSupport).setOnClickListener { view -> presenter.supportClicked() }
        view.findViewById<View>(R.id.menuAboutUs).setOnClickListener { view -> presenter.supportClicked() }



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

    override fun goToSupport() {
        startActivity(Intent(context, SupportActivity::class.java))
    }

    override fun goToProfile() {
        ProfileActivity.startProfileActivity(context)

    }

    override fun goAppSettings() {
        NotificationSettingsActivity.startNotificationSettingsActivity(context)


    }

    override fun goToWelcome() {
        SignInActivity.startSignInActivity(activity!!)

    }


}
