package uk.co.transferx.app.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import uk.co.transferx.app.pojo.Rates;
import uk.co.transferx.app.pojo.TransactionHistory;
import uk.co.transferx.app.pojo.Transactions;

/**
 * Created by sergey on 07/02/2018.
 */

public interface TransactionApi {

    @GET("/v1/transactions")
    Observable<Response<Transactions>> getHistory(@Header("Authorization") String token);

    @GET("/v1/transactions/rates")
    Observable<Response<Rates>> getRats(@Header("Authorization") String token, @Query("from") String from, @Query("to") String to);

}
