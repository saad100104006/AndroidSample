package uk.co.transferx.app.ui.recoverpass

import android.os.Bundle

import uk.co.transferx.app.ui.base.BaseActivity
import uk.co.transferx.app.R
import uk.co.transferx.app.ui.recoverpass.fragment.RecoverPasswordFragment
import uk.co.transferx.app.ui.recoverpass.fragment.RecoverPasswordSuccessFragment

/**
 * Created by sergey on 23.11.17.
 */

class RecoverPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recover_password_activity_layout)

        // Replace fragment accordingly
        replaceFragment(RecoverPasswordFragment(), 0, R.id.container)
    }

    fun goSuccess() {
        replaceFragment(RecoverPasswordSuccessFragment(), -1, R.id.container)
    }

}
