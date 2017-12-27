package uk.co.transferx.app.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import uk.co.transferx.app.pojo.RegisterUserRequest;

/**
 * Created by sergey on 27/12/2017.
 */

public interface SignUpApi {

    @GET("/v1/auth/client/genesis")
    Observable<Response<ResponseBody>> getInitialToken();


    @POST("/v1/auth/user")
    Observable<Response<ResponseBody>> registerUser(@Header("Authorization") String token, @Body RegisterUserRequest request);

}
