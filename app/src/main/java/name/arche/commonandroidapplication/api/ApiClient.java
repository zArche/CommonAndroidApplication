package name.arche.commonandroidapplication.api;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by arche on 2016/4/1.
 */
public class ApiClient {

    private static ApiService apiService;
    private static String mToken;

    static {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .connectionPool(new ConnectionPool(10, 300, TimeUnit.SECONDS));
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Response response = chain.proceed(chain.request());
//                        String rp = response.body().string();
//                        Log.i("url",response.request().url().toString());
//                        Log.i("response",rp);
//                        return response;
//                    }
//                });
        final OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApiService() {
        return apiService;
    }

    public static String getToken() {
        return mToken;
    }

    public static void setToken(String token) {
        mToken = token;
    }
}
