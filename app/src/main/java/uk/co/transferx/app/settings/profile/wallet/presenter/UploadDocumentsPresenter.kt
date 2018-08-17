package uk.co.transferx.app.settings.profile.wallet.presenter

import android.content.SharedPreferences
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.tokenmanager.TokenManager
import javax.inject.Inject

class UploadDocumentsPresenter @Inject constructor(
    private val tokenManager: TokenManager,
    sharedPreferences: SharedPreferences
) : BasePresenter<UploadDocumentsPresenter.UploadDocumentsUI>() {



    interface UploadDocumentsUI : UI {

    }
}