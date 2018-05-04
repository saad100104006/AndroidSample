package uk.co.transferx.app.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import uk.co.transferx.app.pojo.Profile

interface ProfileApi {


    @GET("/v1/auth/user")
    fun featchUserProfile(@Header("Authorization") token: String): Single<Profile>
}