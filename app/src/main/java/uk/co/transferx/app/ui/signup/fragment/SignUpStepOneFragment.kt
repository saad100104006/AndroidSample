package uk.co.transferx.app.ui.signup.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.signup_step_one_fragment_layout.*

import javax.inject.Inject

import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.signup.SignUpActivity
import uk.co.transferx.app.ui.signup.presenters.SignUpStepOnePresenter

import uk.co.transferx.app.util.Constants.U_NAME

/**
 * Created by smilevkiy on 15.11.17.
 */

class SignUpStepOneFragment : BaseFragment(), SignUpStepOnePresenter.SignUpStepOneUI {
    @Inject
    lateinit var presenter: SignUpStepOnePresenter

    override fun tagName(): String {
        return SignUpStepOneFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity!!.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        // Set credentials from previous step
        presenter.setEmail(arguments?.getString(EMAIL))
        presenter.setPassword(arguments?.getString(PASSWORD))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signup_step_one_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack.setOnClickListener {
            hideKeyboard(buttonBack)
            activity?.onBackPressed()
        }

        // Link base class button
        buttonNext = buttonNextOne
        buttonNext.setOnClickListener { presenter.goToNextStep() }

        countrySpinner.setData(resources.getStringArray(R.array.countries))
        countrySpinner.setSelection(0, true)
        countrySpinner.setOnItemSelectedListener { _, country -> presenter.setCountry(country.toString()) }

        // Restore if possible
        firstNameInputText.setText(savedInstanceState?.getString(FIRST_NAME))
        secondNameInputText.setText(savedInstanceState?.getString(LAST_NAME))
        phoneNumberInputText.setText(savedInstanceState?.getString(PHONE_NUMBER))
    }



    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)

        firstNameInputText.addTextChangedListener(emailTextWatcher)
        secondNameInputText.addTextChangedListener(passwordTextWatcher)
        phoneNumberInputText.addTextChangedListener(phoneNumberTextWatcher)
    }

    override fun onPause() {
        presenter.detachUI()

        // Remove listeners
        firstNameInputText.removeTextChangedListener(emailTextWatcher)
        secondNameInputText.removeTextChangedListener(passwordTextWatcher)
        phoneNumberInputText.removeTextChangedListener(phoneNumberTextWatcher)
        super.onPause()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FIRST_NAME, firstNameInputText.text.toString())
        outState.putString(LAST_NAME, secondNameInputText.text.toString())
        outState.putString(PHONE_NUMBER, phoneNumberInputText.text.toString())
    }

    override fun goToNextStep(email: String?, password: String?, uname: String?, phoneNumber: String?, country: String?) {
        val bundle = Bundle()
        bundle.putString(U_NAME, uname)
        bundle.putString(EMAIL, email)
        bundle.putString(PHONE_NUMBER, phoneNumber)
        bundle.putString(PASSWORD, password)
        bundle.putString(COUNTRY, country)

        (activity as SignUpActivity).showNextOrPreviousFragment(1, bundle)
    }

    override fun setButton(isEnabled: Boolean) {
        setButtonStatus(isEnabled)
    }

    override fun setButtonStatus(isEnabled: Boolean) {
        buttonNext.isEnabled = isEnabled
        buttonNext.background = if (isEnabled) ContextCompat.getDrawable(context!!, R.drawable.btn_style_bright)
        else ContextCompat.getDrawable(context!!, R.drawable.btn_disabled)
    }

    // Methods left for possible usage and implementation
    override fun showNameError(isError: Boolean) {
    }

    override fun showLastNameError() {
    }

    override fun showPhoneNumberError() {
    }

    override fun goToWelcome() {
        //no op
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            presenter.setFirstName(s.toString())
        }

    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            presenter.setLastName(s.toString())
        }

    }

    private val phoneNumberTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            presenter.setPhoneNumber(s.toString())
        }

    }
    companion object {
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val PHONE_NUMBER = "phone_number"
        private const val COUNTRY = "country"
        private const val PASSWORD = "password"
        private const val EMAIL = "email"
        private const val POSITION = "country_position"
    }
}
