package uk.co.transferx.app.ui.recoverpass.fragment

import android.os.Bundle
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
import uk.co.transferx.app.ui.recoverpass.presenter.RecoverPasswordPresenter

/**
 * Created by sergey on 23.11.17.
 */

class RecoverPasswordFragment : BaseFragment(), RecoverPasswordPresenter.RecoverPasswordUI {
    @Inject
    lateinit var presenter: RecoverPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recover_password_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recoverMailInputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // setStatusOfError(textInputEditText, label, R.color.black);
                // firstError.setVisibility(View.GONE);
                presenter.validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        sendEmail?.setOnClickListener { presenter.sendEmail() }
        buttonBack.setOnClickListener { presenter.goBack() }
    }

    override fun unlockButton() {
        sendEmail?.isEnabled = true
        sendEmail?.background = ContextCompat.getDrawable(context!!, R.drawable.btn_style_bright)
        //  setStatusOfError(textInputEditText, label, R.color.red);
        //  firstError.setText(R.string.email_error);
        //  firstError.setVisibility(View.VISIBLE);
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun successGoBack() {
        hideKeyboard(recoverMailInputText)
        (activity as RecoverPasswordActivity).goSuccess()
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun lockButton() {
        sendEmail?.isEnabled = false
        sendEmail?.background = ContextCompat.getDrawable(context!!, R.drawable.btn_disabled)
    }

    override fun goBackToMain() {
        activity?.finish()
    }

    override fun error() {
        Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
    }

    override fun goToWelcome() {
        // no op
    }

    override fun tagName(): String {
        return RecoverPasswordFragment::class.java.simpleName
    }
}
