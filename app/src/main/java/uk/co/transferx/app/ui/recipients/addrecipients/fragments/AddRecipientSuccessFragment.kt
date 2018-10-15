package uk.co.transferx.app.ui.recipients.addrecipients.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_add_recipient_success.*
import org.jetbrains.anko.intentFor

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.landing.LandingActivity
import uk.co.transferx.app.ui.recipients.addrecipients.presenters.AddRecipientSuccessPresenter
import javax.inject.Inject


class AddRecipientSuccessFragment : BaseFragment(), AddRecipientSuccessPresenter.View {
    @Inject
    lateinit var presenter: AddRecipientSuccessPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipient_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonMakeTransfer.setOnClickListener { presenter.goToMakeTransfer() }
        buttonGoToRecipients.setOnClickListener { presenter.goToMainScreen() }

    }

    override fun showMakeTransfer() {
       Toast.makeText(activity, "Linking not completed", Toast.LENGTH_LONG).show()
    }

    override fun showMainScreen() {
        activity?.finish()
    }

    override fun goToWelcome() {
        context?.startActivity(context?.intentFor<LandingActivity>())
        activity?.finish()
    }

    override fun tagName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
