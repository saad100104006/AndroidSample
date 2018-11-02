package uk.co.transferx.app.ui.signinpin.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.chaos.view.PinView
import kotlinx.android.synthetic.main.pin_content.*
import kotlinx.android.synthetic.main.signin_fragment_layout.*
import kotlinx.android.synthetic.main.signin_pin_fragment.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.homescreen.MainActivity
import uk.co.transferx.app.ui.signinpin.presenter.SignInPinPresenter
import uk.co.transferx.app.util.Constants
import uk.co.transferx.app.util.Constants.CLEAN_START
import javax.inject.Inject

/**
 * Created by sergey on 19.11.17.
 * Refactored and redesigned by Catalin Ghita on 11/2018
 */

class SignInPinFragment : BaseFragment(), SignInPinPresenter.SignInPinUI {
    @Inject
    lateinit var presenter: SignInPinPresenter

    private var pinView: PinView? = null

    private var cleanStart: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        arguments?.let { cleanStart = arguments!!.getInt(CLEAN_START) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.signin_pin_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinView = loginPinView
        pinView?.setAnimationEnable(true)

        // Provide reference to base class action button
        buttonNext = signInPinButton
        signInPinButton.isEnabled = false
        signInPinButton.setOnClickListener {
            presenter.checkPinAndLogIn(pinView!!.text?.toString()!!)
            hideKeyboard(pinView)
        }
        textForgotPin.setOnClickListener { presenter.resetPassword() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
        pinView?.addTextChangedListener(textWatcher)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachUI()

        pinView?.removeTextChangedListener(textWatcher)
    }

    override fun goToPreviousScreen() {
        if (cleanStart == 1) startActivity(context?.intentFor<MainActivity>()?.newTask()?.clearTask())
        activity?.finish()

    }

    override fun goToWelcomeScreen() {
        // TBDD
        Toast.makeText(activity, "This action has to be discussed and debated", Toast.LENGTH_LONG).show()
    }

    override fun showErrorPin() {
        pinView?.setText(Constants.EMPTY)
        setLinesColor(R.color.red)
        pinView?.requestFocus()

        val snackbar = Snackbar.make(view!!, getString(R.string.no_match_pin), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }


    override fun showError(message: String) {
        val snackbar = Snackbar.make(view!!, getString(R.string.generic_error), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }

    override fun goToWelcome() {
        //no op
    }

    override fun tagName(): String {
        return SignInPinFragment::class.java.simpleName
    }

    override fun setButtonEnabled(isEnabled: Boolean) {
        setButtonStatus(isEnabled)
    }

    override fun setButtonStatus(isEnabled: Boolean) {
        buttonNext.isEnabled = isEnabled
        buttonNext.background = if (isEnabled) ContextCompat.getDrawable(context!!, R.drawable.btn_style_bright)
        else ContextCompat.getDrawable(context!!, R.drawable.btn_disabled)
    }


    private fun resetErrorPin() {
        setLinesColor(R.color.black)
    }

    private fun setLinesColor(@ColorRes color: Int) {
        pinView?.setLineColor(ContextCompat.getColor(pinView!!.context, color))
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            resetErrorPin()
            s.let { presenter.setPinValue(s.toString()) }
        }

        override fun afterTextChanged(s: Editable) {

        }
    }
}
