package uk.co.transferx.app.ui.settings.profile.changepin

import android.os.Bundle
import kotlinx.android.synthetic.main.change_pin_layout.*
import kotlinx.android.synthetic.main.personal_details_activity_layout.*
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.base.BaseActivity
import javax.inject.Inject

/**
 * Created by HP on 11/1/2018.
 */

class ChangePinActivity : BaseActivity(), ChangePinPresenter.ChangePinUI  {


    @Inject
    lateinit var presenter: ChangePinPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.change_pin_layout)

        button_back.setOnClickListener({ onBackPressed() })
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attachUI(this)
    }

    override fun onPause() {
        presenter!!.detachUI()
        super.onPause()
    }

    override fun goToWelcome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToPersonalInformation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToWallet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToChangePassword() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToChangePin() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToUploadDocumentation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
