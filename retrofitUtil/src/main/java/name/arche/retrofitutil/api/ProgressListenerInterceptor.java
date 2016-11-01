package name.arche.retrofitutil.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Arche on 2016/11/1.
 */

public class ProgressListenerInterceptor implements Interceptor {
    private ProgressListener mProgressListener;
    public ProgressListenerInterceptor(ProgressListener progressListener){
        mProgressListener = progressListener;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder().body(
                new ProgressResponseBody(originalResponse.body(), mProgressListener))
                .build();
    }
}
