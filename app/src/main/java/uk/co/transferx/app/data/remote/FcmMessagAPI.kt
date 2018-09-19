package uk.co.transferx.app.data.remote

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import uk.co.transferx.app.data.pojo.UpdateFCMToken

interface FcmMessagAPI {

    @POST("/v1/notification/fcm")
    fun updateToken(@Header("Authorization") token: String, @Body updateFcmToken: UpdateFCMToken): Single<Response<ResponseBody>>
}