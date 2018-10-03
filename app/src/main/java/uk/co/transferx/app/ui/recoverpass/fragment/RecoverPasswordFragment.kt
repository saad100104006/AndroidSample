package uk.co.transferx.app.ui.recoverpass.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.recover_password_fragment_layout.*

import javax.inject.Inject

import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.recoverpass.RecoverPasswordActivity
import uk.co.transferx.app.ui.recoverpass.RecoverPasswordContract
import uk.co.transferx.app.ui.recoverpass.presenter.RecoverPasswordPresenter

/**
 * Created by sergey on 23.11.17.
 */

class RecoverPasswordFragment : BaseFragment(), RecoverPasswordContract.View {
    @Inject
    lateinit var presenter: RecoverPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recover_password_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendEmail?.setOnClickListener { presenter.sendEmail() }
        buttonBack.setOnClickListener { presenter.goBack() }
    }

    override fun unlockButton() {
        sendEmail?.isEnabled = true
        sendEmail?.background = ContextCompat.getDrawable(context!!, R.drawable.btn_style_bright)
    }

    override fun onResume() {
        super.onResume()

        presenter.attachUI(this)
        recoverMailInputText.addTextChangedListener(recoverEmailTextWatcher)
    }

    override fun onPause() {
        super.onPause()

        presenter.detachUI()
        recoverMailInputText.removeTextChangedListener(recoverEmailTextWatcher)
    }

    override fun goToSuccessScreen() {
        hideKeyboard(recoverMailInputText)
        (activity as RecoverPasswordActivity).goSuccess()
    }

    override fun lockButton() {
        sendEmail?.isEnabled = false
        sendEmail?.background = ContextCompat.getDrawable(context!!, R.drawable.btn_disabled)
    }

    override fun goBackToMain() {
        activity?.finish()
    }

    override fun error() {
        Snackbar.make(rootLayout, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
    }

    override fun goToWelcome() {
        // no op
    }

    override fun tagName(): String {
        return RecoverPasswordFragment::class.java.simpleName
    }

    private val recoverEmailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            presenter.validateEmail(s.toString())
        }
    }
}
