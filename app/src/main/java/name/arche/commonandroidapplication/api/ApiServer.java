package name.arche.commonandroidapplication.api;

import name.arche.retrofitutil.api.Result;

import java.util.List;

import name.arche.commonandroidapplication.models.Repo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Arche on 2016/11/1.
 */

public interface ApiServer {

    Observable<Result<Object>> login();//回调方式示例

    Observable<Result<Object>> test(); //rxjava方式示例

    Call<Result<Object>> test1(); //同步 异步方式

    //官方示例接口
    @GET("orgs/{org}/repos")
    Observable<List<Repo>> listRepos(@Path("org") String org);
}
