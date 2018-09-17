package uk.co.transferx.app.ui.settings.profile.personaldetails.presenter

import android.content.SharedPreferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.data.pojo.Profile
import uk.co.transferx.app.data.repository.ProfileRepository
import javax.inject.Inject

class PersonalDetailsPresenterTwo @Inject constructor(
    private val profileRepository: ProfileRepository,
    sharedPreferences: SharedPreferences
) :
    BasePresenter<PersonalDetailsPresenterTwo.PersonalDetailsUITwo>(sharedPreferences) {


    private var compositeDisposable: CompositeDisposable? = null

    override fun attachUI(ui: PersonalDetailsUITwo?) {
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
        super.detachUI()
        compositeDisposable?.dispose()
    }

    interface PersonalDetailsUITwo : UI {

        fun setData(profile: Profile)

        fun setError()
    }
}