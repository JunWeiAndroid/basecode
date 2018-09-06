package com.gemo.common.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gemo.common.BuildConfig;
import com.gemo.common.CommonConfig;
import com.gemo.common.util.StringUtil;
import com.google.gson.Gson;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by WJW
 * HttpManager:
 */

public class HttpManager {

    private final int TIME_OUT = 10;

    private static final HttpManager HTTP_MANAGER = new HttpManager();

    private Retrofit mRetrofit;

    private final Logger LOG = Logger.getLogger(HttpManager.class.getName());

    private HttpManager() {
        mRetrofit = getRetrofit();
    }

    public static HttpManager getInstance() {
        return HTTP_MANAGER;
    }

    private Retrofit getRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(CommonConfig.baseUrl)
                .client(getClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder.build();
    }

    public OkHttpClient getClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .proxy(Proxy.NO_PROXY)
                .addInterceptor(chain -> {
                            Request.Builder requestBuilder
                                    = chain.request().newBuilder();
                            String url = chain.request().url().toString();
                            if (url.contains("/app/user/")) {
                                HeaderData headerData = CommonConfig.getHeaderData();
                                if (headerData != null) {
                                    String token = headerData.getToken();
                                    if (!TextUtils.isEmpty(token)) {
                                        requestBuilder.addHeader("token", token);
                                    }
                                }
                            }
                            requestBuilder.addHeader("platform", "Android")
                                    .addHeader("timestamp", String.valueOf(System.currentTimeMillis()));
                            return chain.proceed(requestBuilder.build());
                        }
                );

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new InterceptorLogInfo())
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    public <T> T getHttpService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public static Gson buildGson() {
        /*Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .create();*/
        return StringUtil.GSON;
    }

    private class InterceptorLogInfo implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(@NonNull String message) {
            com.gemo.common.util.Logger.i(message);
        }
    }

}
