package uk.co.transferx.app.ui.signup.presenters

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import javax.inject.Inject

import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.Util

/**
 * Created by sergey on 08.12.17.
 */

class SignUpStepTwoPresenter @Inject
constructor() : BasePresenter<SignUpStepTwoPresenter.SignUpStepTwoUI>() {
    private var email: String? = null
    private var password: String? = null
    private var rePassword: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var phoneNumber: String? = null
    var country: String? = null

    private var compositeDisposable: CompositeDisposable? = null

    private val isInputCorrect: Boolean
        get() = Util.validatePassword(password) && Util.validateEmail(email)

    private val isPasswordInputCorrect: Boolean
        get() = password == rePassword

    override fun attachUI(ui: SignUpStepTwoUI?) {
        super.attachUI(ui)
        if (compositeDisposable == null) compositeDisposable =  CompositeDisposable()
    }

    override fun detachUI() {
        super.detachUI()
        compositeDisposable?.dispose()
    }

    fun signUpUser() {
            if(isPasswordInputCorrect) {
                // reg logic
                /// TODO

                ui?.goToPinSetup()
            }
            else ui.showErrorPassword()
    }

    fun checkIfEmailIsTaken(){

    }

    fun setEmail(email: String) {
        this.email = email
        validateInputData()
    }

    fun setPassword(password: String) {
        this.password = password
        validateInputData()
    }

    fun setRePassword(rePassword: String) {
        this.rePassword = rePassword
        validateInputData()
    }

    private fun validateInputData() {
            ui?.setStateButton(isInputCorrect)
    }

    interface SignUpStepTwoUI : UI {

        fun goToPinSetup()

        fun showErrorPassword()

        fun setStateButton(isEnabled: Boolean)

    }
}
