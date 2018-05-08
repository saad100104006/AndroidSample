package uk.co.transferx.app.repository

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.api.ProfileApi
import uk.co.transferx.app.pojo.Profile
import uk.co.transferx.app.tokenmanager.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val profileApi: ProfileApi, private val tokenManager: TokenManager) {

    var profile: Profile? = null

    fun getUserProfile(): Observable<Profile> {
        if (profile == null) {
            return profileApi.featchUserProfile(tokenManager.token)
                    .doOnNext({ profile = it })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        return Observable.just(profile)
    }
}