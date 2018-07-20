package uk.co.transferx.app.api;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uk.co.transferx.app.pojo.TokenEntity;
import uk.co.transferx.app.pojo.UserSignIn;

/**
 * Created by sergey on 28/12/2017.
 */

public interface SignInOutApi {


    @POST("/v1/auth/user/login")
    Single<Response<TokenEntity>> signIn(@Header("Authorization") String token, @Body UserSignIn request);


    @POST("/v1/auth/user/logout")
    Single<Response<ResponseBody>> logOut(@Header("Authorization") String token);

    @POST("/v1/auth/user/forgot/{mail}")
    Single<Response<ResponseBody>> forgotEmail(@Header("Authorization") String token, @Path("mail") String mail);

    @POST("/v1/auth/client/refresh")
    Single<Response<ResponseBody>> refreshToken(@Header("Authorization") String token);


}
