package uk.co.transferx.app.ui.signup.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.signup_step_one_fragment_layout.*
import org.w3c.dom.Text

import javax.inject.Inject

import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.R.string.email
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signup_step_one_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack!!.setOnClickListener {
            hideKeyboard(buttonBack)
            activity?.onBackPressed()
        }
        
        emailInputText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        passwordInputText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        passwordReinputText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        
        buttonNext.setOnClickListener { presenter.goToNextStep() }

        emailInputText.setText(savedInstanceState?.getString(EMAIL))
        passwordInputText.setText(savedInstanceState?.getString(PASSWORD))
        passwordReinputText.setText(savedInstanceState?.getString(REPASSWORD))
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
        presenter.setFirstName(emailInputText!!.text.toString())
        presenter.setLastName(passwordInputText!!.text.toString())
        
    }

    override fun onPause() {
        presenter.detachUI()
        emailInputText.removeTextChangedListener(emailTextWatcher)
        passwordInputText.removeTextChangedListener(passwordTextWatcher)
        passwordReinputText.removeTextChangedListener(rePasswordTextWatcher)
        super.onPause()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EMAIL, emailInputText.text.toString())
        outState.putString(PASSWORD, passwordInputText.text.toString())
        outState.putString(REPASSWORD, passwordReinputText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun goToNextStep(uname: String) {
        val bundle = Bundle()
        bundle.putString(U_NAME, uname)
        (activity as SignUpActivity).showNextOrPreviousFragment(1, bundle)
    }

    override fun setButton(isEnabled: Boolean) {
        setButtonStatus(isEnabled)
    }

    override fun showError() {
        // TODO handle error with snackbar
        Toast.makeText(context, "Something wrong with connection, please try again later", Toast.LENGTH_SHORT).show()
    }

    override fun showNameError() {
        //        setStatusOfError(emailInputText, firstLabel, R.color.red);
        //        firstError.setText(R.string.first_name_error);
        //        firstError.setVisibility(View.VISIBLE);
    }

    override fun showLastNameError() {
        //        setStatusOfError(passwordInputText, secondLabel, R.color.red);
        //        secondError.setText(R.string.last_name_error);
        //        secondError.setVisibility(View.VISIBLE);
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
            // TODO set email
            presenter.setFirstName(s.toString())
        }

    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // TODO set password
            presenter.setLastName(s.toString())
        }

    }

    private val rePasswordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // TODO set again password
            presenter.setLastName(s.toString())
        }

    }
    companion object {
        // TODO add to const util
        private val EMAIL = "email"
        private val PASSWORD = "password"
        private val REPASSWORD = "repassword"
    }
}
