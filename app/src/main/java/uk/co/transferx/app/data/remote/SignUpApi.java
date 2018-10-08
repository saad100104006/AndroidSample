package uk.co.transferx.app.data.remote;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import uk.co.transferx.app.data.pojo.TokenEntity;
import uk.co.transferx.app.data.pojo.UserRequest;

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

    @POST("/v1/auth/user/exists")
    Single<Response<ResponseBody>> checkEmail(@Header("Authorization") String token, @Body String email);

}
