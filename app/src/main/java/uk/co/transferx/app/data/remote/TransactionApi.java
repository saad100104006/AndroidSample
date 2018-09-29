package uk.co.transferx.app.data.remote;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import uk.co.transferx.app.data.pojo.CardTokenId;
import uk.co.transferx.app.data.pojo.Rates;
import uk.co.transferx.app.data.pojo.TransactionCreate;
import uk.co.transferx.app.data.pojo.Transactions;

/**
 * Created by sergey on 07/02/2018.
 */

public interface TransactionApi {

    @GET("/v1/transactions")
    Single<Response<Transactions>> getHistory(@Header("Authorization") String token);

    @GET("/v1/transactions/rates")
    Single<Response<Rates>> getRats(@Header("Authorization") String token, @Query("from") String from, @Query("to") String to);

    @POST("/v1/transactions")
    Single<Response<CardTokenId>> createTransaction(@Header("Authorization") String token, @Body TransactionCreate transactionCreate);

    @GET("/v1/transactions?scheduled=true")
    Single<Response<Transactions>> getRecurrentHistory(@Header("Authorization") String token);



}
