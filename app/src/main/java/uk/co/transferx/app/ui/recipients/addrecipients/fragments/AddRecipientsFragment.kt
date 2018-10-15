package uk.co.transferx.app.ui.recipients.addrecipients.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.jakewharton.rxbinding2.widget.RxTextView

import javax.inject.Inject

import io.reactivex.disposables.CompositeDisposable
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.ui.recipients.SelectPictureDialog
import uk.co.transferx.app.ui.recipients.addrecipients.Mode
import uk.co.transferx.app.ui.recipients.addrecipients.presenters.AddRecipientsPresenter
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment
import uk.co.transferx.app.ui.customview.CustomSpinner
import uk.co.transferx.app.ui.signin.SignInActivity

import android.view.Gravity.CENTER
import kotlinx.android.synthetic.main.add_recipient_fragment_layout.*
import uk.co.transferx.app.ui.mainscreen.fragments.RecipientsFragment.DELETE_USER
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.ADDITIONAL_DATA
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.MESSAGE
import uk.co.transferx.app.ui.recipients.addrecipients.AddRecipientsActivity
import uk.co.transferx.app.util.Constants.*

/**
 * Created by sergey on 03/01/2018.
 */

class AddRecipientsFragment : BaseFragment(), AddRecipientsPresenter.AddRecipientsUI {
    @Inject
    lateinit var presenter: AddRecipientsPresenter

    private var compositeDisposable: CompositeDisposable? = null
    private var mode: Mode? = null
    private var shouldButtonDisabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        arguments?.let { if ( arguments?.getInt(MODE) != -1) {
            mode = Mode.values()[arguments!!.getInt(MODE)]
            presenter.recipient = arguments?.getParcelable(RECIPIENT)
        } else mode = Mode.NONE }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.add_recipient_fragment_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonNext = view.findViewById(R.id.addRecipientButton)

        buttonGoBack.setOnClickListener { activity?.onBackPressed() }

        countrySpinner.setOnItemSelectedListener { _, country -> presenter.setCountry(country.toString()) }
        countrySpinner.setDataWithHintItem(resources.getStringArray(R.array.countries), getString(R.string.choose_country))
        if (mode == Mode.EDIT) {
            setUpView()
            return
        }
        shouldButtonDisabled = false
        buttonNext.setOnClickListener {  presenter.saveUserToApi() }
    }

    private fun setUpView() {
        titleRecipient!!.gravity = CENTER
        setEnabled(false)
        setButtonStatus(true)
        buttonNext.setText(R.string.edit)
        transferButton.visibility = View.VISIBLE
        delete_recipient.visibility = View.VISIBLE
        buttonNext.setOnClickListener { setEditMode() }

        delete_recipient.setOnClickListener { showDialogConfirmation(presenter.recipient) }
        transferButton.setOnClickListener { presenter.sendTransfer() }
    }

    private fun setEditMode() {
        shouldButtonDisabled = false
        setEnabled(true)
        transferButton.visibility = View.GONE
        delete_recipient!!.visibility = View.GONE
        buttonNext.setText(R.string.save_changes)
        setButtonStatus(false)
        buttonNext.setOnClickListener { presenter.refreshUserData() }
    }

    private fun setEnabled(enabled: Boolean) {
        countrySpinner!!.isEnabled = enabled
        firstName!!.isEnabled = enabled
        lastName!!.isEnabled = enabled
        phoneInput!!.isEnabled = enabled
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable = CompositeDisposable()
        presenter.attachUI(this)
        compositeDisposable!!.add(RxTextView.textChanges(firstName!!)
                .subscribe { first -> presenter.setFirstName(first.toString()) })
        compositeDisposable!!.add(RxTextView.textChanges(lastName!!)
                .subscribe { last -> presenter.setLastName(last.toString()) })
        compositeDisposable!!.add(RxTextView.textChanges(phoneInput!!)
                .subscribe { phone -> presenter.setPhone(phone.toString()) })

    }

    override fun onPause() {
        if (compositeDisposable != null) compositeDisposable!!.dispose()
        presenter.detachUI()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DELETE_USER) {
            val id = data?.getStringExtra(ADDITIONAL_DATA)
            presenter.deleteRecipient(id)
        }
    }

    override fun goToConfirmationScreen(MODE: Int) {
        val fragment: BaseFragment

        when(MODE){
            FIRST_RECIPIENT_ADDED_MODE -> fragment = AddFirstRecipientSuccessFragment()
            else -> fragment = AddRecipientSuccessFragment()
        }
        (activity as AddRecipientsActivity).launchAddRecipientConfirmationFragment(fragment)
    }

    override fun userActionPerformed() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun showError() {
        // TODO replace with snackbar
        Toast.makeText(context, "Upss something went wrong", Toast.LENGTH_LONG).show()
    }

    override fun setEnabledButton(enabled: Boolean) {
        if (shouldButtonDisabled) return
        setButtonStatus(enabled)
    }

    override fun setData(recipientDto: RecipientDto) {
        titleRecipient.text = recipientDto.fullName
        firstName.setText(recipientDto.firstName)
        lastName.setText(recipientDto.lastName)
        phoneInput.setText(recipientDto.phone)
        val position = getItemPosition(recipientDto.country)
        countrySpinner.adapter.setItemSelected(position)
        countrySpinner.setSelection(position)
    }

    override fun sendTransfer(recipientDto: RecipientDto) {
        val intent = Intent()
        intent.putExtra(RECIPIENT, recipientDto)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun goToWelcome() {
        SignInActivity.startSignInActivity(activity!!)
    }

    override fun tagName(): String {
        return AddRecipientsFragment::class.java.simpleName
    }

    private fun showDialogConfirmation(recipientDto: RecipientDto) {
        val dialogFragment = ConfirmationDialogFragment()
        val bundle = Bundle()
        bundle.putString(MESSAGE, getString(R.string.delete_user_message, recipientDto.fullName))
        bundle.putString(ADDITIONAL_DATA, recipientDto.id)
        dialogFragment.arguments = bundle
        dialogFragment.setTargetFragment(this, DELETE_USER)
        dialogFragment.isCancelable = false
        dialogFragment.show(fragmentManager!!, "TAG")
    }

    private fun getItemPosition(country: String): Int {
        val countries = resources.getStringArray(R.array.countries)
        for (i in countries.indices) if (countries[i] == country) return i
        return -1
    }

}
