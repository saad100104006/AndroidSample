package uk.co.transferx.app.ui.settings.profile

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_upload_documents_layout.*
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.settings.profile.wallet.presenter.UploadDocumentsPresenter
import uk.co.transferx.app.ui.signin.SignInActivity
import javax.inject.Inject

class UploadDocumentsActivity : BaseActivity(), UploadDocumentsPresenter.UploadDocumentsUI {

    @Inject
    lateinit var presenter: UploadDocumentsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.activity_upload_documents_layout)
        buttonBackUpload.setOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachUI(this)
    }

    override fun onPause() {
        presenter.detachUI()
        super.onPause()
    }

    override fun goToWelcome() {
        SignInActivity.startWelcomeActivity(this@UploadDocumentsActivity)
    }
}