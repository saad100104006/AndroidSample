package uk.co.transferx.app.ui.landing.fragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_landing.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.landing.LandingContract
import uk.co.transferx.app.ui.landing.presenter.LandingPresenter
import uk.co.transferx.app.ui.signin.SignInActivity
import uk.co.transferx.app.ui.signup.SignUpActivity
import org.jetbrains.anko.startActivity
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
        activity?.startActivity<SignInActivity>()
    }

    override fun showSignUpScreen() {
        activity?.startActivity<SignUpActivity>()
    }

    override fun goToWelcome() {
        // no op
    }
}
