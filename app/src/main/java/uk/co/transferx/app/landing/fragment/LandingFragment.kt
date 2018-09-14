package uk.co.transferx.app.landing.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_landing.*

import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.landing.LandingContract
import uk.co.transferx.app.landing.presenter.LandingPresenter
import uk.co.transferx.app.signup.SignUpActivity
import uk.co.transferx.app.welcom.WelcomeActivity
import javax.inject.Inject


class LandingFragment @Inject constructor() : Fragment(), LandingContract.View {
    @Inject
    lateinit var presenter: LandingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TransferXApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSignIn.setOnClickListener { presenter.goToLoginScreen() }

        buttonSignUp.setOnClickListener { presenter.goToSignUpScreen() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)

    }

    override fun onPause() {
        super.onPause()
        presenter.detachUI()
    }

    override fun showLoginScreen() {
        WelcomeActivity.startWelcomeActivity(activity)
    }

    override fun showSignUpScreen() {
        SignUpActivity.startSignUp(activity, null)
    }

    override fun goToWelcome() {
        // no op
    }
}
