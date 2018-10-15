package uk.co.transferx.app.ui.recipients.addrecipients.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_add_first_recipient_success.*
import org.jetbrains.anko.intentFor
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.landing.LandingActivity
import uk.co.transferx.app.ui.recipients.addrecipients.AddRecipientsActivity
import uk.co.transferx.app.ui.recipients.addrecipients.presenters.AddFirstRecipientSuccessPresenter
import uk.co.transferx.app.ui.settings.profile.wallet.AddCardActivity
import uk.co.transferx.app.ui.settings.profile.wallet.CardMode
import uk.co.transferx.app.util.Constants
import javax.inject.Inject


class AddFirstRecipientSuccessFragment : BaseFragment(), AddFirstRecipientSuccessPresenter.View {
    @Inject
    lateinit var presenter: AddFirstRecipientSuccessPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_first_recipient_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddRecipient.setOnClickListener{ presenter.goToAddRecipient() }
        buttonGoToMain.setOnClickListener{ presenter.goToMainScreen() }
        buttonAddPayment.setOnClickListener{ presenter.goToAddPayment() }
    }

    override fun showAddRecipient() {
        (activity as AddRecipientsActivity).launchAddEditRecipientsFragment()
    }

    override fun showAddPaymentMethod() {
        context?.startActivity(context?.intentFor<AddCardActivity>(Constants.MODE to CardMode.ADD.ordinal))
        activity?.finish()
    }

    override fun showMainScreen() {
        activity?.finish()
    }

    override fun goToWelcome() {
        context?.startActivity(context?.intentFor<LandingActivity>())
        activity?.finish()
    }

    override fun tagName(): String {
        return AddFirstRecipientSuccessFragment::class.simpleName as String
    }

    override fun setStatusOfError(textInputEditText: EditText?, label: TextView?, color: Int) {
        super.setStatusOfError(textInputEditText, label, color)
    }

    override fun hideKeyboard(view: View?) {
        super.hideKeyboard(view)
    }

    override fun setButtonStatus(isEnabled: Boolean) {
        super.setButtonStatus(isEnabled)
    }
}
