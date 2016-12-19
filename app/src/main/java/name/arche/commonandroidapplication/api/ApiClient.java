package name.arche.commonandroidapplication.api;

import name.arche.retrifitclient.RetrofitClient;


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

}
