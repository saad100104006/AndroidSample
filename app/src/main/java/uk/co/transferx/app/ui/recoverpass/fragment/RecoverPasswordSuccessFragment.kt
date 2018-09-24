package uk.co.transferx.app.ui.recoverpass.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recover_password_success_fragment_layout.*

import uk.co.transferx.app.ui.base.BaseFragment
import uk.co.transferx.app.R

/**
 * Created by sergey on 19/03/2018.
 */

class RecoverPasswordSuccessFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recover_password_success_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSignIn.setOnClickListener { activity?.finish() }
    }

    override fun tagName(): String {
        return RecoverPasswordSuccessFragment::class.java.simpleName
    }
}
