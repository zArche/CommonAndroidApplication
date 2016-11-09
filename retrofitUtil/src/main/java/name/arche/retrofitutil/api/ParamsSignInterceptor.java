package name.arche.retrofitutil.api;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by Arche on 2016/11/1.
 */

// 参数加密
public class ParamsSignInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        TreeMap<String, String> treeMap = new TreeMap<String, String>();

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        treeMap.put("timestamp", timeStamp);

        if (original.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oldFormBody = (FormBody) original.body();
            newFormBody.add("timestamp",timeStamp );

            for (int i = 0; i < oldFormBody.size(); i++) {
                newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                treeMap.put(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
            }

            newFormBody.addEncoded("sign", EncryptionClient.getSign(treeMap));
            requestBuilder.method(original.method(), newFormBody.build());

            Request request = requestBuilder.build();
            return chain.proceed(request);

        } else if (original.method().equals("GET")) {
            HttpUrl url = original.url();
            for (int i = 0; i < url.querySize(); i++) {
                treeMap.put(url.queryParameterName(i), url.queryParameterValue(i));
            }
            String sign = EncryptionClient.getSign(treeMap);

            HttpUrl.Builder builder = url.newBuilder();
            builder.addQueryParameter("timestamp",timeStamp );
            builder.addQueryParameter("sign", sign);
            Request request = requestBuilder.url(builder.build()).build();
            return chain.proceed(request);

        } else if (original.body() instanceof MultipartBody) {
            MultipartBody originBody = (MultipartBody) original.body();
            List<MultipartBody.Part> parts = originBody.parts();
            Class partClazz = MultipartBody.Part.class;
            java.lang.reflect.Field headersField = null, bodyField = null;
            MultipartBody.Builder builder = new MultipartBody.Builder();
            try {
                //反射headers和body字段，在okhttp3.4.1后续版本可以直接get到
                // todo 待更新okhttp版本后换掉
                headersField = partClazz.getDeclaredField("headers");
                bodyField = partClazz.getDeclaredField("body");
                headersField.setAccessible(true);
                bodyField.setAccessible(true);

                for (MultipartBody.Part part : parts) {
                    Headers headers = (Headers) headersField.get(part);
                    RequestBody body = (RequestBody) bodyField.get(part);
                    MediaType contentType = body.contentType();
                    builder.addPart(part);
                    if ((contentType == null||contentType.toString().contains("text/plain")) && body.contentLength() > 0) {//文本--需加密字段
                        String key = readKeyFromHeaders(headers);
                        String value = readValueFromBody(body);
                        treeMap.put(key, value);
                    }
                }
                builder.addFormDataPart("timestamp", timeStamp);
                builder.addFormDataPart("sign", EncryptionClient.getSign(treeMap));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            builder.setType(MultipartBody.FORM);
            Request request = requestBuilder.method(original.method(), builder.build()).build();
            okhttp3.Response response = chain.proceed(request);
            MediaType mediaType = response.body().contentType();
            String contentT = response.body().string();
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, contentT))
                    .build();
        } else {
            okhttp3.Response response = chain.proceed(original);
            MediaType mediaType = response.body().contentType();
            String contentT = response.body().string();
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, contentT))
                    .build();
        }
    }

    private String readValueFromBody(RequestBody body) throws IOException {
        Buffer sink = new Buffer();
        body.writeTo(sink);
        sink.flush();
        int length = (int) body.contentLength();
        byte[] bytes = new byte[length];
        sink.read(bytes);
        return new String(bytes);
    }

    private String readKeyFromHeaders(Headers headers) {
        String disposition = headers.get("Content-Disposition");
        String nameStr = "name=\"";
        int start = disposition.indexOf(nameStr) + nameStr.length();
        int end = disposition.indexOf("\"", start);
        return disposition.substring(start, end);
    }
}
