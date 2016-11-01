package name.arche.retrofitutil.api;

import com.sensetime.apiencryption.EncryptionClient;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by Arche on 2016/11/1.
 */

// 参数加密
public  class ParamsSignInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder();
        long timeStamp = System.currentTimeMillis();

        if (original.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oldFormBody = (FormBody) original.body();
            newFormBody.add("timestamp", timeStamp + "");

            for (int i = 0; i < oldFormBody.size(); i++) {
                newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                treeMap.put(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
            }

            treeMap.put("timestamp", timeStamp + "");
            newFormBody.addEncoded("sign", EncryptionClient.getSign(treeMap));
            requestBuilder.method(original.method(), newFormBody.build());

            Request request = requestBuilder.build();
            return chain.proceed(request);

        } else if (original.method().equals("GET")){
            HttpUrl url = original.url();
            for (int i=0;i<url.querySize();i++){
                treeMap.put(url.queryParameterName(i),url.queryParameterValue(i));
            }
            treeMap.put("timestamp", timeStamp + "");
            String sign = EncryptionClient.getSign(treeMap);

            HttpUrl.Builder builder =  url.newBuilder();
            builder.addQueryParameter("timestamp",timeStamp +"");
            builder.addQueryParameter("sign",sign);
            Request.Builder request = requestBuilder.url(builder.build());
            return chain.proceed(request.build());

        } else {

            okhttp3.Response response = chain.proceed(original);
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }
}
