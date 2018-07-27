package uk.co.transferx.app.settings.profile.personaldetails.presenter

import android.content.SharedPreferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.pojo.Profile
import uk.co.transferx.app.repository.ProfileRepository
import javax.inject.Inject

class PersonalDetailsPresenterOne @Inject constructor(
    private val profileRepository: ProfileRepository,
    sharedPreferences: SharedPreferences
) :
    BasePresenter<PersonalDetailsPresenterOne.PersonalDetailsOneUI>(sharedPreferences) {

    private var compositeDisposable: CompositeDisposable? = null

    override fun attachUI(ui: PersonalDetailsOneUI?) {
        super.attachUI(ui)
        compositeDisposable = CompositeDisposable()
        val disposable: Disposable = profileRepository.getUserProfile()
            .subscribeBy(
                onSuccess = { ui?.setData(it) },
                onError = { globalErrorHandler(it) }
            )
        compositeDisposable?.add(disposable)
    }

    override fun detachUI() {
        compositeDisposable?.dispose()
        super.detachUI()
    }

    interface PersonalDetailsOneUI : UI {
        fun setData(profile: Profile)
        fun setError()
    }
}