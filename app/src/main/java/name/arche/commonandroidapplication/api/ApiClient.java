package name.arche.commonandroidapplication.api;

import name.arche.retrofitutil.api.RetrofitClient;

import java.util.List;

import name.arche.commonandroidapplication.models.Repo;
import retrofit2.Call;
import rx.Observable;


/**
 * Created by Arche on 2016/11/1.
 */

public class ApiClient {

    private static ApiClient mApiClient;
    private static RetrofitClient mRetrofitClient;
    private static ApiServer mApiServer;

    static {
        try {
            mRetrofitClient = new RetrofitClient.Builder()
                    .baseUrl(URLs.API_URL)
                    .setApiServiceClazz(ApiServer.class)
                    .builder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mApiServer = (ApiServer) mRetrofitClient.getApiService();
        mApiClient = new ApiClient();
    }

    public static ApiClient getInstance(){
        return mApiClient;
    }

    public void login(RetrofitClient.CallBack callBack) { //以call back方式
        mRetrofitClient.callback(callBack).handleResponse(mApiServer.login());
    }

    public Observable test(){ //以rxjava方式
        return  mApiServer.test();
    }

    public Call test1(){ //以同步、异步方式
        return mApiServer.test1();
    }

    public Observable<List<Repo>> listRepos(String org){
        return mApiServer.listRepos(org);
    }

    public void listReposByCallback(String org, RetrofitClient.CallBack callBack){
        mRetrofitClient.callback(callBack).handleResponse(mApiServer.listRepos(org));
    }

}
