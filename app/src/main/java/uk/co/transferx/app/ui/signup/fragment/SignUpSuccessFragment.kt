package uk.co.transferx.app.ui.signup.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_sign_up_success.*
import org.jetbrains.anko.intentFor

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.mainscreen.MainActivity
import uk.co.transferx.app.ui.recipients.addrecipients.AddRecipientsActivity
import uk.co.transferx.app.ui.signup.presenters.SignUpSuccessPresenter
import java.time.Duration
import javax.inject.Inject

class SignUpSuccessFragment : BaseFragment(), SignUpSuccessPresenter.SignUpSuccessView {
    @Inject
    lateinit var presenter: SignUpSuccessPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddPayment.setOnClickListener{ presenter.goToAddPayment() }

        buttonAddRecipient.setOnClickListener{ presenter.goToAddRecipient() }

        buttonGoToMain.setOnClickListener{ presenter.goToMainScreen() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)

    }

    override fun onPause() {
        super.onPause()
        presenter.detachUI()
    }

    override fun showAddRecipient() {
        // TODO Linking
//        context?.startActivity(context?.intentFor<AddRecipientsActivity>())
//        activity?.finish()
        Toast.makeText(activity, "Linking has not been written yet!", Toast.LENGTH_LONG).show()
    }

    override fun showAddPaymentMethod() {
        // TODO Linking
//        context?.startActivity(context?.intentFor<AddRecipientsActivity>())
//        activity?.finish()
        Toast.makeText(activity, "Linking has not been written yet!", Toast.LENGTH_LONG).show()
    }

    override fun showMainScreen() {
        // TODO Linking
//        context?.startActivity(context?.intentFor<MainActivity>())
//        activity?.finish()
        Toast.makeText(activity, "Linking has not been written yet!", Toast.LENGTH_LONG).show()
    }

    override fun goToWelcome() {
        // No op
    }

    override fun tagName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
