package name.arche.retrofitutil.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by Arche on 2016/11/1.
 */

public class LogInterceptor implements Interceptor {

    private boolean mDebug;
    private static final String TAG = "RetrofitClient";
    public LogInterceptor(boolean debug){
        mDebug = debug;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (mDebug)
            Log.d(TAG, "request:" + original.toString());      //输出请求前整个url

        long t1 = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(original);
        long t2 = System.currentTimeMillis();
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        if (mDebug){
            Log.d(TAG,"request cost time:" + + (t2 - t1) + "ms");
            Log.d(TAG,"response:" + content);
        }
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();

    }
}
