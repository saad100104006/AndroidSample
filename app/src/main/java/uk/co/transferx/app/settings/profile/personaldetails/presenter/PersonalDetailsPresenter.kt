package uk.co.transferx.app.settings.profile.personaldetails.presenter

import android.content.SharedPreferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.pojo.Profile
import uk.co.transferx.app.repository.ProfileRepository
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