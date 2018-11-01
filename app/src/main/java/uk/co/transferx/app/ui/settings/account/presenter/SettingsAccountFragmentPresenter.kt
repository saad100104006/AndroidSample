package uk.co.transferx.app.ui.settings.account.presenter

import io.reactivex.disposables.Disposable
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import javax.inject.Inject

/**
 * Created by HP on 10/31/2018.
 */

class SettingsAccountFragmentPresenter @Inject

constructor() : BasePresenter<SettingsAccountFragmentPresenter.SettingsAccountFragmentUI>() {

    private var disposable: Disposable? = null


    override fun detachUI() {
        super.detachUI()
        if (disposable != null && disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }


    fun clickPersonalInformation() {
        ui?.goToPersonalInformation()
    }
    fun clickChangePassword() {
        ui?.goToChangePassword()
    }
    fun clickWallet() {
        ui?.goToWallet()
    }
    fun clickChangePin() {
        ui?.goToChangePin()
    }
    fun clickUploadDocumentation() {
        ui?.goToUploadDocumentation()
    }


    interface SettingsAccountFragmentUI : UI {

         fun goToPersonalInformation()

        fun goToWallet()

        fun goToChangePassword()

        fun goToChangePin()

        fun goToUploadDocumentation()
    }
}
