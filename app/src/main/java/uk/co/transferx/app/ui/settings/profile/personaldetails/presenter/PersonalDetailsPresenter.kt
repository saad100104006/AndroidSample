package uk.co.transferx.app.ui.settings.profile.personaldetails.presenter

import android.content.SharedPreferences
import io.reactivex.disposables.CompositeDisposable
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.repository.ProfileRepository
import javax.inject.Inject

class PersonalDetailsPresenter @Inject constructor(
    private val profileRepository: ProfileRepository,
    sharedPreferences: SharedPreferences
) :
    BasePresenter<PersonalDetailsPresenter.PersonalDetailsUI>(sharedPreferences) {

    private var compositeDisposable: CompositeDisposable? = null


    override fun detachUI() {
        compositeDisposable?.dispose()
        super.detachUI()
    }

    interface PersonalDetailsUI : UI {


    }
}