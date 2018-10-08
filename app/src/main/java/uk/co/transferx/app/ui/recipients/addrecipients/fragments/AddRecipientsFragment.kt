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
import uk.co.transferx.app.ui.mainscreen.fragments.RecipientsFragment.DELETE_USER
import uk.co.transferx.app.util.Constants.MODE
import uk.co.transferx.app.util.Constants.RECIPIENT
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.ADDITIONAL_DATA
import uk.co.transferx.app.ui.customview.ConfirmationDialogFragment.MESSAGE

/**
 * Created by sergey on 03/01/2018.
 */

class AddRecipientsFragment : BaseFragment(), AddRecipientsPresenter.AddRecipientsUI {

    @Inject
    internal var presenter: AddRecipientsPresenter? = null
    private var firstName: TextInputEditText? = null
    private var lastName: TextInputEditText? = null
    private var phoneNumber: TextInputEditText? = null
    private var countrySpinner: CustomSpinner? = null
    private var titleText: TextView? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var mode: Mode? = null
    private var newTransfer: Button? = null
    private var deleteRecipient: Button? = null
    private var shouldButtonDisabled = true

    override fun tagName(): String {
        return AddRecipientsFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null && bundle.getInt(MODE) != -1) {
            mode = Mode.values()[bundle.getInt(MODE)]
            presenter!!.recipient = bundle.getParcelable(RECIPIENT)
        } else {
            mode = Mode.NONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.add_recipient_fragment_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countrySpinner = view.findViewById(R.id.countrySpinner)
        buttonNext = view.findViewById(R.id.add_recipient_button)
        titleText = view.findViewById(R.id.title_recipient)
        firstName = view.findViewById(R.id.firstName)
        lastName = view.findViewById(R.id.lastName)
        phoneNumber = view.findViewById(R.id.phoneInput)
        newTransfer = view.findViewById(R.id.transfer_button)
        deleteRecipient = view.findViewById(R.id.delete_recipient)
        view.findViewById<View>(R.id.recipient_img).setOnClickListener { v -> SelectPictureDialog.newInstance().show(fragmentManager!!, "TAG") }
        view.findViewById<View>(R.id.button_back).setOnClickListener { v -> activity!!.onBackPressed() }
        countrySpinner!!.setOnItemSelectedListener { position, `object` -> presenter!!.setCountry(`object`.toString()) }
        countrySpinner!!.setDataWithHintItem(resources.getStringArray(R.array.countries), getString(R.string.choose_country))
        if (mode == Mode.EDIT) {
            setUpView()
            return
        }
        shouldButtonDisabled = false
        buttonNext.setOnClickListener { v -> presenter!!.saveUserToApi() }
    }

    private fun setUpView() {
        titleText!!.gravity = CENTER
        setEnabled(false)
        setButtonStatus(true)
        buttonNext.setText(R.string.edit)
        newTransfer!!.visibility = View.VISIBLE
        deleteRecipient!!.visibility = View.VISIBLE
        buttonNext.setOnClickListener { v -> setEditMode() }
        deleteRecipient!!.setOnClickListener { v -> showDialogConfirmation(presenter!!.recipient) }
        newTransfer!!.setOnClickListener { v -> presenter!!.sendTransfer() }
    }

    private fun setEditMode() {
        shouldButtonDisabled = false
        setEnabled(true)
        newTransfer!!.visibility = View.GONE
        deleteRecipient!!.visibility = View.GONE
        buttonNext.setText(R.string.save_changes)
        setButtonStatus(false)
        buttonNext.setOnClickListener { v -> presenter!!.refreshUserData() }
    }

    private fun setEnabled(enabled: Boolean) {
        countrySpinner!!.isEnabled = enabled
        firstName!!.isEnabled = enabled
        lastName!!.isEnabled = enabled
        phoneNumber!!.isEnabled = enabled
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable = CompositeDisposable()
        presenter!!.attachUI(this)
        compositeDisposable!!.add(RxTextView.textChanges(firstName!!)
                .subscribe { first -> presenter!!.setFirstName(first.toString()) })
        compositeDisposable!!.add(RxTextView.textChanges(lastName!!)
                .subscribe { last -> presenter!!.setLastName(last.toString()) })
        compositeDisposable!!.add(RxTextView.textChanges(phoneNumber!!)
                .subscribe { phone -> presenter!!.setPhone(phone.toString()) })

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

    override fun onPause() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
        presenter!!.detachUI()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DELETE_USER) {
            val id = data!!.getStringExtra(ADDITIONAL_DATA)
            presenter!!.deleteRecipient(id)
        }
    }

    override fun userActionPerformed() {
        activity!!.setResult(Activity.RESULT_OK)
        activity!!.finish()
    }

    override fun showError() {
        Toast.makeText(context, "Upss something went wrong", Toast.LENGTH_LONG).show()
    }

    override fun setEnabledButton(enabled: Boolean) {
        if (shouldButtonDisabled) {
            return
        }
        setButtonStatus(enabled)
    }

    private fun getItemPosition(country: String): Int {
        val countries = resources.getStringArray(R.array.countries)
        for (i in countries.indices) {
            if (countries[i] == country) {
                return i
            }
        }
        return -1
    }

    override fun setData(recipientDto: RecipientDto) {
        titleText!!.text = recipientDto.fullName
        firstName!!.setText(recipientDto.firstName)
        lastName!!.setText(recipientDto.lastName)
        phoneNumber!!.setText(recipientDto.phone)
        val position = getItemPosition(recipientDto.country)
        countrySpinner!!.adapter.setItemSelected(position)
        countrySpinner!!.setSelection(position)
    }

    override fun sendTransfer(recipientDto: RecipientDto) {
        val intent = Intent()
        intent.putExtra(RECIPIENT, recipientDto)
        activity!!.setResult(Activity.RESULT_OK, intent)
        activity!!.finish()
    }

    override fun goToWelcome() {
        SignInActivity.startSignInActivity(activity!!)
    }
}
