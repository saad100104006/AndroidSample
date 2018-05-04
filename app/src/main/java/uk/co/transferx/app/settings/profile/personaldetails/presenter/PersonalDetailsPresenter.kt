package uk.co.transferx.app.settings.profile.personaldetails.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.api.ProfileApi
import uk.co.transferx.app.tokenmanager.TokenManager
import javax.inject.Inject

class PersonalDetailsPresenter @Inject constructor(private val profileApi: ProfileApi,
                                                   private val tokenManager: TokenManager) : BasePresenter<PersonalDetailsPresenter.PersonalDetailsUI>() {


    override fun attachUI(ui: PersonalDetailsUI?) {
        super.attachUI(ui)
       profileApi.featchUserProfile(tokenManager.token)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe()
    }


    interface PersonalDetailsUI : UI {
    }
}