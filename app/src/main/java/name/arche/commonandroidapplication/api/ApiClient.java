package name.arche.commonandroidapplication.api;

import name.arche.retrofitutil.api.ProgressListener;
import name.arche.retrofitutil.api.RetrofitClient;

import java.util.List;

import name.arche.commonandroidapplication.models.Repo;
import retrofit2.Call;
import rx.Observable;


/**
 * Created by Arche on 2016/11/1.
 */

public class ApiClient {

    /**
     * 支持3种方式调用：
     *  1.以call back 方式：
     *        外部直接调用ApiClient.getRetrofitClient().handleResponse(mApiServer.login(),callBack);
     *        因此可以在此类中增加一层例如login接口
     *  2.以rxjava原生返回方式：
     *        外部直接调用ApiClient.getApiServer().test().subscribeOn()....
     *  3.以官方Call方式返回
     * **/

    private static ApiClient mApiClient;
    private static RetrofitClient mRetrofitClient;
    private static ApiServer mApiServer;

    static {
        try {
            mRetrofitClient = new RetrofitClient.Builder()
                    .baseUrl(URLs.API_URL)
                    .setApiServerClazz(ApiServer.class)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mApiServer = (ApiServer) mRetrofitClient.getApiService();
        mApiClient = new ApiClient();
    }

    public static ApiClient getInstance(){
        return mApiClient;
    }

    public static RetrofitClient getRetrofitClient(){return mRetrofitClient;}

    public static ApiServer getApiServer(){return mApiServer;}


    /**
     * 以call back方式调用时，可增加一层
     * **/
    public void login(RetrofitClient.CallBack callBack) {
        mRetrofitClient.handleResponse(mApiServer.login(),callBack);
    }

    /**
     * 以rxjava原生返回时，可以增加一层，也可以在外部直接调用ApiClient.getApiServer().test().subscribeOn()....
     * **/
    public Observable test(){ //以rxjava方式
        return  mApiServer.test();
    }


    /**
     * 以官方原生Call方式返回时，可以增加一层，也可以在外部直接调用
     * **/
    public Call test1(){ //以同步、异步方式
        return mApiServer.test1();
    }


    public Observable<List<Repo>> listRepos(String org){
        return mApiServer.listRepos(org);
    }

    public void listReposByCallback(String org, RetrofitClient.CallBack callBack){
        mRetrofitClient.handleResponse(mApiServer.listRepos(org),callBack);
    }

}
