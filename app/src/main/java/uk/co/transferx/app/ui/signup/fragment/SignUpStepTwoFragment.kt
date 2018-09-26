package uk.co.transferx.app.ui.signup.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.signup_step_two_fragment_layout.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.signup.SignUpActivity
import uk.co.transferx.app.ui.signup.presenters.SignUpStepTwoPresenter
import uk.co.transferx.app.util.Constants.EMAIL
import uk.co.transferx.app.util.Constants.PASSWORD
import javax.inject.Inject

/**
 * Created by smilevkiy on 15.11.17.
 */

class SignUpStepTwoFragment : BaseFragment(), SignUpStepTwoPresenter.SignUpStepTwoUI {
    @Inject
    lateinit var presenter: SignUpStepTwoPresenter

    private var isErrorShown: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signup_step_two_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack.setOnClickListener {
            hideKeyboard(buttonBack)
            activity?.onBackPressed()
        }

        buttonNext = buttonNextStep
        buttonNext.setOnClickListener {
            presenter.goToNextStep()
            hideKeyboard(buttonBack)
        }

        setUpCredentialsOnRestore(savedInstanceState)
    }

    override fun goToNextView(email: String?, password: String?) {
        hideKeyboard(passwordInputText)

        val bundle = Bundle()
        bundle.putString(EMAIL, email)
        bundle.putString(PASSWORD, password)

        (activity as SignUpActivity).showNextOrPreviousFragment(1, bundle)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)

        presenter.setEmail(emailInputText.text.toString())
        presenter.setPassword(passwordInputText.text.toString())
        presenter.setRePassword(rePasswordInputText.text.toString())

        emailInputText.addTextChangedListener(emailTextWatcher)
        passwordInputText.addTextChangedListener(passwordTextWatcher)
        rePasswordInputText.addTextChangedListener(rePasswordTextWatcher)
    }

    override fun onPause() {
        presenter.detachUI()
        emailInputText.removeTextChangedListener(emailTextWatcher)
        passwordInputText.removeTextChangedListener(passwordTextWatcher)
        rePasswordInputText.removeTextChangedListener(rePasswordTextWatcher)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EMAIL, emailInputText.text.toString())
        outState.putString(PASSWORD, passwordInputText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun setStateButton(isEnabled: Boolean) {
        setButtonStatus(isEnabled)
    }

    override fun setButtonStatus(isEnabled: Boolean) {
        buttonNext.isEnabled = isEnabled
        buttonNext.background = if (isEnabled) ContextCompat.getDrawable(context!!, R.drawable.btn_style_bright)
        else ContextCompat.getDrawable(context!!, R.drawable.btn_disabled)
    }


    override fun showErrorPassword() {
        enableErrorOnPasswordInputs()
    }

    override fun goToWelcome() {
        //no op
    }

    override fun tagName(): String {
        return SignUpStepTwoFragment::class.java.simpleName
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setEmail(s.toString()) }
        }

    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setPassword(s.toString()) }
            disableErrorOnPasswordInputs()
        }

    }

    private val rePasswordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setRePassword(s.toString()) }
            disableErrorOnPasswordInputs()
        }

    }

    private fun setUpCredentialsOnRestore(savedInstanceState: Bundle?) {
        val restoredMail = savedInstanceState?.getString(EMAIL)
        val restoredPassword = savedInstanceState?.getString(PASSWORD)

        restoredMail?.let {
            restoredPassword?.let {
                emailInputText.setText(restoredMail)
                passwordInputText.setText(restoredPassword)
                rePasswordInputText.setText(restoredPassword)

                presenter.setEmail(restoredMail)
                presenter.setPassword(restoredPassword)
                presenter.setRePassword(restoredPassword)
            }
        }
    }

    private fun enableErrorOnPasswordInputs() {
        passwordInputText.setBackgroundResource(R.drawable.input_field_error)
        rePasswordInputText.setBackgroundResource(R.drawable.input_field_error)

        isErrorShown = true
    }

    private fun disableErrorOnPasswordInputs() {
        if (isErrorShown) {
            passwordInputText.setBackgroundResource(R.drawable.input_field)
            rePasswordInputText.setBackgroundResource(R.drawable.input_field)
            isErrorShown = false
        }
    }
}
