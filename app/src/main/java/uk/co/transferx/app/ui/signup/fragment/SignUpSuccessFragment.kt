package uk.co.transferx.app.ui.signup.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.signup.presenters.SignUpSuccessPresenter
import javax.inject.Inject

class SignUpSuccessFragment : Fragment(), SignUpSuccessPresenter.SignUpSuccessView {
    @Inject
    lateinit var presenter: SignUpSuccessPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_success, container, false)
    }

    override fun goToAddRecipient() {

    }

    override fun goToAddPaymentMethod() {

    }

    override fun goToWelcome() {
        // No op
    }


}
