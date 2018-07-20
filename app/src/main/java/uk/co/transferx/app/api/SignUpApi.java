package uk.co.transferx.app.api;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import uk.co.transferx.app.pojo.TokenEntity;
import uk.co.transferx.app.pojo.UserRequest;

/**
 * Created by sergey on 27/12/2017.
 */

public interface SignUpApi {

    @GET("/v1/auth/client/genesis")
    Single<Response<TokenEntity>> getInitialToken();


    @POST("/v1/auth/user")
    Single<Response<TokenEntity>> registerUser(@Header("Authorization") String token, @Body UserRequest request);


    @POST("/v1/auth/client/refresh")
    Single<Response<TokenEntity>> refreshToken(@Header("Authorization") String token);

}
