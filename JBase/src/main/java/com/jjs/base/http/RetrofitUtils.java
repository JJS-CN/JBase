package com.jjs.base.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 本页：网络请求封装，提供默认和Headers头部2种实例。
 * 通过init设置初始url；通过update设置头部请求参数；通过getInstance获取单例
 * Created by jjs on 2017-03-24.
 * Email:994462623@qq.com
 */
public class RetrofitUtils {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;
    private static String BASE_URL;//默认Url
    private static Map<String, String> headersMap;//头部数据添加

    /**
     * 传入初始参数,注意baseUrl结尾和api开头不能相同
     * 比如：baseurl/apis/  +   apis/getuser
     * 上面可能会识别成baseurl/apis/getuser  这样的请求地址，所以得注意
     *
     * @param baseUrl 服务器url
     */
    public static void init(String baseUrl) {
        BASE_URL = baseUrl;
    }

    /**
     * @param headerMap 请求拦截，添加头部参数,同时重新创建实例进行覆盖
     */
    public static void updateHeaders(Map<String, String> headerMap) {
        headersMap = headerMap;
        SingletonHolder.INSTANCE_HEADERS = new RetrofitUtils(headersMap);
    }

    /**
     * 获取Retrofit实例
     */
    public static RetrofitUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取Retrofit实例,包括Header
     */
    public static RetrofitUtils getInstanceHeaders() {
        return SingletonHolder.INSTANCE_HEADERS;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    /**
     * 实际由getInstance() 进行调用创建
     */
    private RetrofitUtils(Map<String, String> headerMap) {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(this.DEFAULT_TIME_OUT, TimeUnit.SECONDS)//设置全局请求的连接超时时间，默认为15s
                .writeTimeout(this.DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)//写操作 超时时间
                .readTimeout(this.DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//设置全局请求的数据读取超时时间，默认为30s
        // 当有数据时，向okhttp中添加公共参数拦截器
        if (headerMap != null) {
            Log.e("RetrofitUtils","notNull");
            BasicParamsInterceptor interceptor = new BasicParamsInterceptor.Builder().addHeaderParamsMap(headerMap).build();
            builder.addInterceptor(interceptor);
        } else {
            Log.e("RetrofitUtils","isNull");
            BasicParamsInterceptor interceptor = new BasicParamsInterceptor.Builder().build();
            builder.addInterceptor(interceptor);
        }
        //创建okhttp实例
        OkHttpClient client = builder.build();
        //配置你的Gson，在不同环境下gson对Data的转化可能不一样，这里使用统一的格式
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        // 创建Retrofit,将gson和okhttp加入进来
        mRetrofit = new Retrofit.Builder().baseUrl(this.BASE_URL)//设置基础域名。网络请求由此拼接（注意：需要/结尾，且此处与单独有相同路径时会智能纠错：www/api/+api/xxx会纠错为www/api/xxx）
                .addConverterFactory(GsonConverterFactory.create(gson))//设置json返回消息的解析库（这里使用gson）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//此在与RxJava联用时才使用
                .client(client)//配置okhttp配置。可无（为空时，retrofit会使用默认配置）
                .build();
    }

    private static class SingletonHolder {
        private static RetrofitUtils INSTANCE = new RetrofitUtils(null);
        private static RetrofitUtils INSTANCE_HEADERS = new RetrofitUtils(headersMap);
    }


}