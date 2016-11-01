package name.arche.retrofitutil.api;

import android.util.Log;


import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by arche on 2016/4/1.
 */
public class RetrofitClient<T> {

    private static final String TAG = "RetrofitClient";
    private T mApiService;

    public T getApiService() {
        return mApiService;
    }

    private CallBack mCallBack;

    private boolean mDebug;

    private RetrofitClient(Retrofit retrofit, Class clazz, boolean debug) {
        mApiService = (T) retrofit.create(clazz);
        mDebug = debug;
    }


    // 每个网络请求的流程都是这个样子的，这块代码是重复的
    public <T> void handleResponse(Observable<Result<T>> observable) {
        observable.subscribeOn(Schedulers.io()) // 执行在io线程，效率会高点
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Result<T>>>() {
                    @Override
                    public Observable<? extends Result<T>> call(Throwable throwable) {
                        return HttpErrorHandler.onError(throwable); // 错误处理
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mCallBack != null)
                            mCallBack.onStart();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()) //在线程中进行准备工作，即上面的onStart
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程中观察结果
                .subscribe(new Action1<Result<T>>() {
                               @Override
                               public void call(Result<T> result) {
                                   if (mDebug)
                                       Log.d(TAG, "response: code is " + result.getCode() + ",message is " + result.getMessage());
                                   if (mCallBack != null) {
                                       mCallBack.onResponse(result);
                                   }
                               }
                           }


                );
    }


    public <T> RetrofitClient callback(CallBack callBack) {
        this.mCallBack = callBack;
        return this;
    }

    /**
     * 网络请求结果回调
     */
    public static abstract class CallBack {
        public abstract void onStart();
        public abstract void onResponse(Result result);
        public void onProgressUpdate(long progress, long total, boolean done){}

    }

    public final static class Builder {

        private RetrofitClient mRetrofitClient;

        private Class mClazz; //api service 接口类
        private OkHttpClient mOkHttpClient; //OkHttpClient
        private Retrofit mRetrofit; //Retrofit Client
        private String mBaseUrl;//base url
        private CallAdapter.Factory mCallAdapterFactory;
        private Converter.Factory mConverterFactory;
        private boolean mDebug;
        private int mConnectTimeout; //连接超时时长
        private int mReadTimeout; //读取超时时长
        private boolean mRetryOnConnectionFailure; //失败是否重连
        private int mMaxIdleConnections; //连接池最大连接数
        private long mKeepAliveDuration; //连接最大存活时间

        public Builder() {
            mCallAdapterFactory = RxJavaCallAdapterFactory.create();
            mConverterFactory = GsonConverterFactory.create();
            mDebug = true;
            mConnectTimeout = 30;
            mReadTimeout = 30;
            mRetryOnConnectionFailure = false;
            mMaxIdleConnections = 10;
            mKeepAliveDuration = 300;
        }


        public Builder(OkHttpClient okHttpClient){
            this(okHttpClient,null);
        }

        public Builder(Retrofit retrofit){
            this(null,retrofit);
        }

        public Builder(OkHttpClient okHttpClient,Retrofit retrofit){
            mOkHttpClient = okHttpClient;
            mRetrofit = retrofit;
        }

        /**
         * 设置连接超时时长
         * **/
        public Builder connectTimeout(int connectTimeout){
            mConnectTimeout = connectTimeout;
            return this;
        }

        /**
         * 设置读取超时时长
         * **/
        public Builder readTimeout(int readTimeout){
            mReadTimeout = readTimeout;
            return this;
        }

        /**
         * 设置失败重连
         * **/
        public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure){
            mRetryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        /**
         * 设置连接池最大连接数
         * **/
        public Builder maxIdleConnections(int maxIdleConnections){
            mMaxIdleConnections = maxIdleConnections;
            return this;
        }


        /**
         * 设置连接最大存活时长
         * **/
        public Builder keepAliveDuration(long keepAliveDuration){
            mKeepAliveDuration = keepAliveDuration;
            return this;
        }

        /**
         * 设置baseUrl
         **/
        public Builder baseUrl(String baseUrl) {
            mBaseUrl = baseUrl;
            return this;
        }

        /**
         * 设置apiService接口类
         **/
        public Builder setApiServiceClazz(Class clazz) {
            mClazz = clazz;
            return this;
        }

        /**
         * 设置okHttpClient;超时等设置自定义
         **/
        public Builder client(OkHttpClient okHttpClient) {
            mOkHttpClient = okHttpClient;
            return this;
        }


        /**
         * 自定义retrofit
         * **/
        public Builder retrofit(Retrofit retrofit){
            mRetrofit = retrofit;
            return this;
        }

        /**
         * 设置call adpter factory
         **/
        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            mCallAdapterFactory = factory;
            return this;
        }


        /**
         * 设置converter factory
         **/
        public Builder addConverterFactory(Converter.Factory factory) {
            mConverterFactory = factory;
            return this;
        }

        /**
         * log 开关设置
         **/
        public Builder debug(boolean debug) {
            mDebug = debug;
            return this;
        }


        /**
         * build api client
         **/
        public RetrofitClient builder() throws Exception {
            if (mClazz == null || mBaseUrl == null) {
                throw new Exception("The class of api service or the base url is null !");
            }

            if (mOkHttpClient == null){
                OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

                okHttpClientBuilder.connectTimeout(mConnectTimeout, TimeUnit.SECONDS)
                        .readTimeout(mReadTimeout, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(mRetryOnConnectionFailure)
                        .connectionPool(new ConnectionPool(mMaxIdleConnections, mKeepAliveDuration, TimeUnit.SECONDS))
                        .addInterceptor(new ParamsSignInterceptor()) //参数签名
                        .addInterceptor(new LogInterceptor(mDebug)) //日志输出
                        .addInterceptor(new ProgressListenerInterceptor(new ProgressListener() {
                            @Override
                            public void onProgress(long progress, long total, boolean done) {
                                if (mRetrofitClient != null && mRetrofitClient.mCallBack != null){
                                    mRetrofitClient.mCallBack.onProgressUpdate(progress,total,done);
                                }
                            }
                        }));

                mOkHttpClient = okHttpClientBuilder.build();
            }

            if (mRetrofit == null){
                mRetrofit = new Retrofit.Builder()
                        .baseUrl(mBaseUrl)
                        .client(mOkHttpClient)
                        .addCallAdapterFactory(mCallAdapterFactory)
                        .addConverterFactory(mConverterFactory)
                        .build();
            }
            mRetrofitClient = new RetrofitClient(mRetrofit, mClazz, mDebug);
            return mRetrofitClient;
        }

    }

}
