package uk.co.transferx.app.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.pojo.CardTokenId
import uk.co.transferx.app.pojo.Cards

interface CardsApi {


    @POST("/v1/bank/cards")
    fun addCards(@Header("Authorization") token: String, @Body card: Card): Single<CardTokenId>

    @GET("/v1/bank/cards")
    fun getCards(@Header("Authorization") token: String): Single<Cards>

    @DELETE("/v1/bank/cards/{id}")
    fun deleteCard(@Header("Authorization") token: String, @Path("id") id: String?): Single<Response<ResponseBody>>

    @PUT("/v1/bank/cards/{id}")
    fun saveEditedCard(@Header("Authorization") token: String, @Path("id") id: String?, @Body card: Card): Single<Response<ResponseBody>>

}