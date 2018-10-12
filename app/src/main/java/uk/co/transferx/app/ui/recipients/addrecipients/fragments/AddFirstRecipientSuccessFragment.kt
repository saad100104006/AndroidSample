package uk.co.transferx.app.ui.recipients.addrecipients.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import uk.co.transferx.app.R
import uk.co.transferx.app.ui.base.BaseFragment


class AddFirstRecipientSuccessFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_first_recipient_success, container, false)
    }

    override fun tagName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setStatusOfError(textInputEditText: EditText?, label: TextView?, color: Int) {
        super.setStatusOfError(textInputEditText, label, color)
    }

    override fun hideKeyboard(view: View?) {
        super.hideKeyboard(view)
    }

    override fun setButtonStatus(isEnabled: Boolean) {
        super.setButtonStatus(isEnabled)
    }
}
