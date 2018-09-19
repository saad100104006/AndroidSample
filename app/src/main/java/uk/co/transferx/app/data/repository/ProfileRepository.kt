package uk.co.transferx.app.data.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.data.remote.ProfileApi
import uk.co.transferx.app.data.pojo.Profile
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
        private val profileApi: ProfileApi,
        private val tokenManager: TokenManager
) {

    var profile: Profile? = null

    fun getUserProfile(): Single<Profile> {
        if (profile == null) {
            return tokenManager.token
                .flatMap { profileApi.featchUserProfile(it.accessToken) }
                .doOnSuccess { profile = it }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
        return Single.just(profile)
    }
}