package uk.co.transferx.app.ui.signin.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.signin_fragment_layout.*
import uk.co.transferx.app.R
import uk.co.transferx.app.R.string.forgot_password
import uk.co.transferx.app.R.string.reset_password
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

    private var isErrorShown: Boolean = false

    private var snackbar: Snackbar? = null

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
        forgotPasswButton.setText(getMixedColorsBuilder(), TextView.BufferType.SPANNABLE)

        // enable superclass to handle disabling the button
        this.buttonNext = signInButton
        signInButton.setOnClickListener {
            presenter.signIn()
            hideKeyboard(rootLayout)
        }

        backButton.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onStart() {
        super.onStart()

        // Add listeners
        mailInputText!!.addTextChangedListener(emailTextWatcher)
        passwInputText!!.addTextChangedListener(passwTextWatcher)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachUI()

        // Remove listeners
        mailInputText!!.removeTextChangedListener(emailTextWatcher)
        passwInputText!!.removeTextChangedListener(passwTextWatcher)
    }

    override fun goToSignUp() {
        SignUpActivity.startSignUp(activity, null)
    }

    override fun goToMainScreen() {
        val activity = activity
        if (activity != null) {
            MainActivity.startMainActivity(activity)
            activity.finish()
        }
    }

    override fun goToRecoverPassword() {
        startActivity(Intent(context, RecoverPasswordActivity::class.java))
    }

    override fun showWrongPassword() {
        snackbar = Snackbar.make(rootLayout!!,
                getColoredString(getString(R.string.wrong_username_or_password)), Snackbar.LENGTH_LONG)

        adjustFieldsOnError()
    }

    override fun showUserNotFound() {
        snackbar = Snackbar.make(rootLayout!!,
                getColoredString(getString(R.string.user_not_found)), Snackbar.LENGTH_LONG)
        snackbar?.view?.setBackgroundColor(Color.RED)
        snackbar?.show()

        adjustFieldsOnError()
    }

    override fun showConnectionError() {
        snackbar = Snackbar.make(rootLayout!!, getString(R.string.connection_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.try_again)) {
                    presenter.refreshGenesisToken()
                    snackbar!!.dismiss()
                }
        snackbar?.view?.setBackgroundColor(Color.RED)
        snackbar?.show()

        adjustFieldsOnError()
    }

    /*
        Methods left for future development
     */
    override fun showEmailError() {
        setStatusOfError(mailInputText, null, R.color.red)
    }

    override fun showPasswordError() {
        setStatusOfError(passwInputText, null, R.color.red)
    }

    override fun setStatusOfError(textInputEditText: EditText?, label: TextView?, color: Int) {
        // ignore label
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

    /*
        Text Watchers
     */

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            presenter.validateEmail(s.toString())

            adjustFieldsPostError()
        }

        override fun afterTextChanged(s: Editable) {
        }
    }

    private val passwTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            presenter.validatePassword(s.toString())

            adjustFieldsPostError()
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    /*
        Internal methods
     */
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

    private fun getMixedColorsBuilder(): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        val str1 = SpannableString(resources.getString(forgot_password))
        str1.setSpan(ForegroundColorSpan(Color.BLACK), 0, str1.length, 0)
        builder.append(str1)

        val str2 = SpannableString(resources.getString(reset_password))
        str2.setSpan(ForegroundColorSpan(
                ResourcesCompat.getColor(resources, R.color.colorAccent, null)), 0, str2.length, 0)
        builder.append(str2)

        return builder
    }

    private fun adjustFieldsOnError() {
        setFieldsBackground(R.drawable.input_field_error)
        isErrorShown = true
    }

    private fun adjustFieldsPostError() {
        if (isErrorShown) {
            setFieldsBackground(R.drawable.input_field)
            isErrorShown = false
        }
    }

    private fun setFieldsBackground(resource: Int) {
        mailInputText.setBackgroundResource(resource)
        passwInputText.setBackgroundResource(resource)
    }
}
