package uk.co.transferx.app.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import uk.co.transferx.app.pojo.ChangePassword
import uk.co.transferx.app.pojo.Message
import uk.co.transferx.app.pojo.Profile

interface ProfileApi {


    @GET("/v1/auth/user")
    fun featchUserProfile(@Header("Authorization") token: String): Single<Profile>

    @PUT("/v1/auth/user/password")
    fun changePassword(@Header("Authorization") token: String, @Body changePassword: ChangePassword) : Single<Message>
}