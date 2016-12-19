package name.arche.retrifitclient;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Arche on 2016/11/1.
 */

public class ProgressListenerInterceptor implements Interceptor {
    private ProgressListener mProgressListener;

    public ProgressListenerInterceptor(ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        okhttp3.MediaType mediaType = originalResponse.body().contentType();
        String content = originalResponse.body().string();
        ResponseBody originalResponseBody = ResponseBody.create(mediaType, content);
        return originalResponse.newBuilder().body(
                new ProgressResponseBody(originalResponseBody, mProgressListener))
                .build();
    }
}
