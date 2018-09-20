package uk.co.transferx.app.ui.splash

import android.content.Intent
import android.os.Bundle
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity

import javax.inject.Inject

import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.landing.LandingActivity
import uk.co.transferx.app.ui.signin.SignInActivity
import uk.co.transferx.app.ui.splash.presenter.SplashPresenter
import uk.co.transferx.app.ui.tutorial.TutorialActivity
import uk.co.transferx.app.ui.welcom.WelcomeActivity

/**
 * Created by sergey on 19.11.17.
 */

class SplashActivity : BaseActivity(), SplashContract.View {
    @Inject
    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }


    override fun onPause() {
        super.onPause()
        presenter.detachUI()
    }

    override fun goToLandingScreen() {
        startActivity(intentFor<LandingActivity>().newTask().clearTask())
    }

    override fun goToPinScreen() {
        // TODO - CREATE LINK  TO SignInPinActivity WHEN LINKING IS PERFORMED
    }

    override fun goToTutorialScreen() {
        startActivity(intentFor<TutorialActivity>().newTask().clearTask())
    }

    // Method required by base UI. In further development, should be refined or deleted
    override fun goToWelcome() {
        //no op
    }
}
