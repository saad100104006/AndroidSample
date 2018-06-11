package uk.co.transferx.app.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import uk.co.transferx.app.pojo.Card
import uk.co.transferx.app.pojo.CardTokenId
import uk.co.transferx.app.pojo.Cards

interface CardsApi {


    @POST("/v1/bank/cards")
    fun addCards(@Header("Authorization") token: String, @Body card: Card): Single<CardTokenId>

    @GET("/v1/bank/cards")
    fun getCards(@Header("Authorization") token: String): Single<Cards>

}