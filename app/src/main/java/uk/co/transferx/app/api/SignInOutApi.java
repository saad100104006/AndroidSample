package uk.co.transferx.app.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import uk.co.transferx.app.pojo.UserRequest;

/**
 * Created by sergey on 28/12/2017.
 */

public interface SignInOutApi {


    @POST("/v1/auth/user/login")
    Observable<Response<ResponseBody>> signIn(@Header("Authorization") String token, @Body UserRequest request);


    @POST("/v1/auth/user/logout")
    Observable<Response<ResponseBody>> logOut(@Header("Authorization") String token);


}
