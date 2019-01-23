package uk.co.transferx.app.ui.settings.profile.changepin.fragment

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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_change_pin.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.ui.settings.profile.changepin.presenter.ChangePinPresenter
import uk.co.transferx.app.util.Constants
import javax.inject.Inject

/**
 * Created by Tanvir on 2/12/18.
 */

class ChangePinFragment : BaseFragment(), ChangePinPresenter.ChangePinUI {

    @Inject
    lateinit var presenter: ChangePinPresenter
    var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TransferXApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_pin, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonNext =savePin
        buttonNext.setOnClickListener {
            hideKeyboard(it)
            presenter.checkOldPinAndChangePin()
        }

        firstPinsEntry?.setAnimationEnable(true)
        secondPinsEntry?.setAnimationEnable(true)
        confirmPinsEntry?.setAnimationEnable(true)

    }

    override fun onResume() {
        super.onResume()
        compositeDisposable = CompositeDisposable()
        presenter.attachUI(this)

        firstPinsEntry.addTextChangedListener(firstPinWatcher)
        secondPinsEntry.addTextChangedListener(secondPinWatcher)
        confirmPinsEntry.addTextChangedListener(thirdPinWatcher)
    }

    override fun onPause() {
        presenter.detachUI()
        firstPinsEntry.removeTextChangedListener(firstPinWatcher)
        secondPinsEntry.removeTextChangedListener(secondPinWatcher)
        confirmPinsEntry.removeTextChangedListener(thirdPinWatcher)
        firstPinsEntry.setText(Constants.EMPTY)
        secondPinsEntry.setText(Constants.EMPTY)
        confirmPinsEntry.setText(Constants.EMPTY)
        super.onPause()
    }

    private fun resetErrorPin() {
        setLinesColor(R.color.black)
    }

    private fun setLinesColor(@ColorRes color: Int) {
        firstPinsEntry.setLineColor(ContextCompat.getColor(context!!, color))
        secondPinsEntry.setLineColor(ContextCompat.getColor(context!!, color))
        confirmPinsEntry.setLineColor(ContextCompat.getColor(context!!, color))
    }

    override fun showErrorPin() {
        firstPinsEntry.setText(Constants.EMPTY)
        secondPinsEntry.setText(Constants.EMPTY)
        confirmPinsEntry.setText(Constants.EMPTY)
        setLinesColor(R.color.red)
        firstPinsEntry.requestFocus()
        val snackbar = Snackbar.make(view!!, getString(R.string.no_match_pin), Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.RED)
        snackbar.show()
    }


    override fun setButtonStatus(isEnabled: Boolean) {
        buttonNext.isEnabled = isEnabled
        buttonNext.background = if (isEnabled) ContextCompat.getDrawable(context!!, R.drawable.btn_style_bright)
        else ContextCompat.getDrawable(context!!, R.drawable.btn_disabled)
    }

    private val firstPinWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setCurrentPin(s.toString()) }
            resetErrorPin()
        }
    }


    private val secondPinWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setNewPin(s.toString()) }
            resetErrorPin()
        }
    }

    private val thirdPinWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { presenter.setConfirmPin(s.toString()) }
            resetErrorPin()
        }
    }

    override fun setButtonEnabled(enabled: Boolean) {
        setButtonStatus(enabled)
    }
    override fun goToAccount() {
    }
    override fun goToWelcome() {
    }
    override fun tagName(): String {
        return ChangePinFragment::class.java.simpleName
    }
    override fun goToPreviousScreen() {
        activity?.finish()

    }
}
