package com.jjs.base.http;

import com.jjs.base.BaseStore;
import com.jjs.base.base.BaseApplication;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 说明：
 * Created by jjs on 2018/7/2.
 */

public class BaseClient {

    private volatile static BaseClient sInstance;
    private Object api;
    private Retrofit mRetrofit;

    private static BaseClient getInstance() {
        if (sInstance == null) {
            synchronized (BaseClient.class) {
                if (sInstance == null) {
                    sInstance = new BaseClient();
                }
            }
        }
        return sInstance;
    }

    private OkHttpClient buildOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
       builder.addInterceptor(new FixedInterceptor());
        builder.cookieJar(new NovateCookieManger(BaseApplication.get()));
        return builder.build();
    }

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BaseStore.BaseUrl)
                    .client(buildOkHttp())
                    .addCallAdapterFactory(RxJavaFactory.getInstance())
                    .addConverterFactory(new GsonConverterFactory())
                    .build();
        }
        return mRetrofit;
    }

    public void replaceRetrofit(Retrofit retrofit) {
        mRetrofit = retrofit;
        api = null;

    }

    public void refresh() {
        mRetrofit = null;
        api = null;
    }

    /**
     * 获取对应的Service
     */
    public <T> T create(Class<T> service) {
        if (api == null) {
            api = getRetrofit().create(service);
        }
        return (T) api;
    }


}
