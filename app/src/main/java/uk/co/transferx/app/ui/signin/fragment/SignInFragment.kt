package uk.co.transferx.app.ui.signin.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.email_field.*
import kotlinx.android.synthetic.main.signin_fragment_layout.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.mainscreen.MainActivity
import uk.co.transferx.app.ui.recoverpass.RecoverPasswordActivity
import uk.co.transferx.app.ui.signin.SignInContract
import uk.co.transferx.app.ui.signin.presenter.SignInPresenter
import uk.co.transferx.app.ui.signup.SignUpActivity
import javax.inject.Inject

/**
 * Created by smilevkiy on 13.11.17.
 */

class SignInFragment : BaseFragment(), SignInContract.View {
    @Inject
    lateinit var presenter: SignInPresenter

    private var snackbar: Snackbar? = null

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            presenter.validateEmail(s.toString())
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    private val passwTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            presenter.validatePassword(s.toString())
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signin_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerButton.setOnClickListener { presenter.goToSignUp() }

        forgotPasswButton.setOnClickListener { presenter.goToRecoverPassword() }

        this.buttonNext = signInButton
        signInButton.setOnClickListener { presenter.signIn() }
    }

    override fun onStart() {
        super.onStart()
        mailInput!!.addTextChangedListener(emailTextWatcher)
        passwInput!!.addTextChangedListener(passwTextWatcher)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        super.onPause()

        presenter.detachUI()

        mailInput!!.removeTextChangedListener(emailTextWatcher)
        passwInput!!.removeTextChangedListener(passwTextWatcher)

    }

    override fun goToSignUp() {
        SignUpActivity.startSignUp(activity, null)
    }

    override fun goToRecoverPassword() {
        startActivity(Intent(context, RecoverPasswordActivity::class.java))
    }

    override fun showWrongPassword() {
        Snackbar.make(coordinatorLayout!!, getColoredString(getString(R.string.wrong_username_or_password)), Snackbar.LENGTH_LONG).show()
    }

    override fun goToMainScreen() {
        val activity = activity
        if (activity != null) {
            MainActivity.startMainActivity(activity)
            activity.finish()
        }
    }

    override fun showUserNotFound() {
        Snackbar.make(coordinatorLayout!!, getColoredString(getString(R.string.user_not_found)), Snackbar.LENGTH_LONG).show()
    }

    override fun showConnectionError() {
        snackbar = Snackbar.make(coordinatorLayout!!, getString(R.string.connection_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.try_again)) {
                    presenter.refreshGenesisToken()
                    snackbar!!.dismiss()
                }
        snackbar!!.show()
    }

    override fun showEmailError() {
        setStatusOfError(mailInput, mailLabel, R.color.red)
        mailError!!.setText(R.string.email_error)
        mailError!!.visibility = View.VISIBLE
    }

    override fun showPasswordError() {
        setStatusOfError(passwInput, passwLabel, R.color.red)
        passwError!!.setText(R.string.password_error)
        passwError!!.visibility = View.VISIBLE
    }

    override fun changeButtonState(isEnabled: Boolean) {
        setButtonStatus(isEnabled)
    }

    override fun tagName(): String {
        return SignInFragment::class.java.simpleName
    }

    override fun goToWelcome() {
        //no op
    }

    private fun getColoredString(message: String): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
                .append(message)
        ssb.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.red)),
                0,
                message.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ssb
    }
}
