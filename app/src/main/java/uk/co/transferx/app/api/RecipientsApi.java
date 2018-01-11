package uk.co.transferx.app.api;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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


    @DELETE("/v1/recipients/{id}")
    Observable<Response<ResponseBody>> deleteRecipient(@Header("Authorization") String token, @Path("id") String id);

    @PUT("/v1/recipients/{id}")
    Observable<Response<ResponseBody>> updateRecipient(@Header("Authorization") String token, @Path("id") String id);


}
