package name.arche.commonandroidapplication.api;

import java.util.List;

import name.arche.commonandroidapplication.models.Repo;
import name.arche.commonandroidapplication.models.Result;
import name.arche.commonandroidapplication.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by arche on 2016/4/1.
 */
public interface ApiService {
    /**========================示例接口========================
    @GET("users/{user}/repos")
    Result<List<Repo>> listRepos(@Path("user") String user);

     @Headers({
     "Accept: application/vnd.github.v3.full+json",
     "User-Agent: Retrofit-Sample-App"
     })
     @GET("users/{username}")
     Result<User> getUser(@Path("username") String username);

    */

//    @GET("users/{username}")
//    Result<User> getUser(@Path("username") String username);

    @GET("/face/user")
    Observable<Result<User>> getUserInfo(@Query("company_id")String company_id,@Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("/face/users")
    Call<Result<List<User>>> getUsers(@Field("company_id")String company_id);


    @GET("orgs/{org}/repos")
    Observable<List<Repo>> listRepos(@Path("org") String org);
}
