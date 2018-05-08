package uk.co.transferx.app.settings.profile.personaldetails.presenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import uk.co.transferx.app.BasePresenter
import uk.co.transferx.app.UI
import uk.co.transferx.app.pojo.Profile
import uk.co.transferx.app.repository.ProfileRepository
import javax.inject.Inject

class PersonalDetailsPresenterOne @Inject constructor(private val profileRepository: ProfileRepository) : BasePresenter<PersonalDetailsPresenterOne.PersonalDetailsOneUI>() {

    private var compositeDisposable: CompositeDisposable? = null

    override fun attachUI(ui: PersonalDetailsOneUI?) {
        super.attachUI(ui)
        compositeDisposable = CompositeDisposable()
        val disposable: Disposable = profileRepository.getUserProfile()
                .subscribeBy(
                        onNext = { ui?.setData(it) },
                        onError = { ui?.setError() }
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