package uk.co.transferx.app.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import uk.co.transferx.app.pojo.AddedRecipientResponce;
import uk.co.transferx.app.pojo.Recipient;

/**
 * Created by sergey on 03/01/2018.
 */

public interface RecipientsApi {


    @GET("/v1/recipients")
    Observable<Response<List<Recipient>>> getRecipients(@Header("Authorization") String token);


    @POST("/v1/recipients")
    Observable<Response<AddedRecipientResponce>> addRecipient(@Header("Authorization") String token, @Body Recipient request);


}
