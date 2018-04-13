package uk.co.transferx.app.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import uk.co.transferx.app.pojo.Token;
import uk.co.transferx.app.pojo.UserRequest;
import uk.co.transferx.app.pojo.UserSignIn;

/**
 * Created by sergey on 28/12/2017.
 */

public interface SignInOutApi {


    @POST("/v1/auth/user/login")
    Observable<Response<Token>> signIn(@Header("Authorization") String token, @Body UserSignIn request);


    @POST("/v1/auth/user/logout")
    Observable<Response<ResponseBody>> logOut(@Header("Authorization") String token);


}
