package uk.co.transferx.app.ui.signup.fragment

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
import kotlinx.android.synthetic.main.sign_up_step_three_fragment_layout.*
import org.jetbrains.anko.startActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.landing.LandingActivity
import uk.co.transferx.app.ui.mainscreen.MainActivity
import uk.co.transferx.app.ui.signup.SignUpActivity
import uk.co.transferx.app.ui.signup.presenters.SignUpStepThreePresenter
import uk.co.transferx.app.util.Constants.*
import javax.inject.Inject

/**
 * Created by smilevkiy on 15.11.17.
 */

class SignUpStepThreeFragment : BaseFragment(), SignUpStepThreePresenter.SignUpStepThreeUI {
    @Inject
    lateinit var presenter: SignUpStepThreePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        // Get credentials from previous steps
        presenter.setCredential(arguments!!.getString(U_NAME), arguments!!.getString(EMAIL),
                arguments!!.getString(PASSWORD), arguments!!.getString(PHONE_NUMBER), arguments!!.getString(COUNTRY))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_up_step_three_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack.setOnClickListener {
            hideKeyboard(buttonBack)
            activity?.onBackPressed()
        }

        buttonNext = buttonSignUp
        buttonNext.setOnClickListener {
            hideKeyboard(it)
            presenter.signUpUser()
        }

        firstPinEntry.setAnimationEnable(true)
        secondPinEntry.setAnimationEnable(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
        firstPinEntry.addTextChangedListener(firstPinWatcher)
        secondPinEntry.addTextChangedListener(secondPinWatcher)
    }

    override fun onPause() {
        presenter.detachUI()
        firstPinEntry.removeTextChangedListener(firstPinWatcher)
        secondPinEntry.removeTextChangedListener(secondPinWatcher)
        firstPinEntry.setText(EMPTY)
        secondPinEntry.setText(EMPTY)
        super.onPause()
    }

    override fun showErrorPin() {
        firstPinEntry.setText(EMPTY)
        secondPinEntry.setText(EMPTY)
        setLinesColor(R.color.red)
        firstPinEntry.requestFocus()

        val snackbar = Snackbar.make(view!!, getString(R.string.no_match_pin), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }

    private fun resetErrorPin() {
        setLinesColor(R.color.black)
    }

    override fun goToMainScreen() {
        MainActivity.startMainActivity(activity)
        activity?.finish()
    }

    override fun goToConfirmationScreen() {
        (activity as SignUpActivity).showNextOrPreviousFragment(3, null)
    }

    override fun showErrorFromBackend() {
    }

    override fun setButtonStatus(isEnabled: Boolean) {
        buttonNext.isEnabled = isEnabled
        buttonNext.background = if (isEnabled) ContextCompat.getDrawable(context!!, R.drawable.btn_style_bright)
        else ContextCompat.getDrawable(context!!, R.drawable.btn_disabled)
    }

    override fun goToWelcome() {
        context?.startActivity<LandingActivity>()
    }

    override fun setButtonEnabled(isEnabled: Boolean) {
        setButtonStatus(isEnabled)
    }

    override fun tagName(): String {
        return SignUpStepThreeFragment::class.java.simpleName
    }

    private fun setLinesColor(@ColorRes color: Int) {
        firstPinEntry.setLineColor(ContextCompat.getColor(context!!, color))
        secondPinEntry.setLineColor(ContextCompat.getColor(context!!, color))
    }

    private val firstPinWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setFirstPin(s.toString()) }
            resetErrorPin()
        }

    }

    private val secondPinWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setSecondPin(s.toString()) }
            resetErrorPin()
        }

    }

}
