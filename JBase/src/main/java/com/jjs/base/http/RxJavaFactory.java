package com.jjs.base.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 说明：处理call方法
 * Created by jjs on 2018/7/2.
 */

public class RxJavaFactory extends CallAdapter.Factory {
    private static final RxJavaFactory ourInstance = new RxJavaFactory();
    private final RxJava2CallAdapterFactory mFactory;

    public static RxJavaFactory getInstance() {
        return ourInstance;
    }

    private RxJavaFactory() {

        mFactory = RxJava2CallAdapterFactory.create();

    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter<?, ?> callAdapter = mFactory.get(returnType, annotations, retrofit);
        return (CallAdapter<?, ?>) Proxy.newProxyInstance(CallAdapter.class.getClassLoader()
                , new Class[]{CallAdapter.class}, new RxJavaAdapter(callAdapter));
    }


}
