package uk.co.transferx.app.ui.settings.profile.wallet.presenter

import android.content.SharedPreferences
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import javax.inject.Inject

class UploadDocumentsPresenter @Inject constructor(
    private val tokenManager: TokenManager,
    sharedPreferences: SharedPreferences
) : BasePresenter<UploadDocumentsPresenter.UploadDocumentsUI>() {



    interface UploadDocumentsUI : UI {

    }
}