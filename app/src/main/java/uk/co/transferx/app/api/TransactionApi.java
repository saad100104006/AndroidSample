package uk.co.transferx.app.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import uk.co.transferx.app.pojo.TransactionHistory;

/**
 * Created by sergey on 07/02/2018.
 */

public interface TransactionApi {

    @GET("/v1/transactions")
    Observable<Response<List<TransactionHistory>>> getHistory(@Header("Authorization") String token);
}
