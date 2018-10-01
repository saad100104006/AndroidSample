package uk.co.transferx.app.ui.settings.profile.changepassword

import android.os.Bundle
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.change_password_activity_layout.*
import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.TransferXApplication
import uk.co.transferx.app.ui.signin.SignInActivity
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity(), ChangePasswordPresenter.ChangePasswordUI {

    @Inject
    lateinit var presenter: ChangePasswordPresenter
    var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TransferXApplication).appComponent.inject(this)
        setContentView(R.layout.change_password_activity_layout)
        changeButtonBack.setOnClickListener({ onBackPressed() })
        savePassword.setOnClickListener({ presenter.saveNewPassword() })
    }


    override fun onResume() {
        super.onResume()
        compositeDisposable = CompositeDisposable()
        val currentPassDis = RxTextView.textChanges(currentPassword)
                .subscribe({ presenter.setCurrentPassword(it.toString()) })
        val newPassDis = RxTextView.textChanges(newPassword)
                .subscribe({ presenter.setNewPassword(it.toString()) })
        val confirmPassDis = RxTextView.textChanges(confirmPassword)
                .subscribe({ presenter.setConfirmPassword(it.toString()) })
        compositeDisposable?.add(currentPassDis)
        compositeDisposable?.add(newPassDis)
        compositeDisposable?.add(confirmPassDis)
        presenter.attachUI(this)
    }

    override fun onPause() {
        compositeDisposable?.dispose()
        presenter.detachUI()
        super.onPause()
    }

    override fun setButtonEnabled(enabled: Boolean) {
        savePassword.isEnabled = enabled
        savePassword.setBackgroundResource(if (enabled) {
            R.drawable.oval_button_black
        } else {
            R.drawable.oval_button_gray
        })
    }

    override fun goBackToSettings() {
        finish()
    }

    override fun goToWelcome() {
        SignInActivity.startSignInActivity(this@ChangePasswordActivity)
    }
}