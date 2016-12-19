package name.arche.commonandroidapplication.api;

import java.util.List;

import name.arche.commonandroidapplication.models.Repo;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Arche on 2016/11/1.
 */

public interface ApiServer {

    //示例接口
    @GET("orgs/{org}/repos")
    Observable<List<Repo>> listRepos(@Path("org") String org);
}
