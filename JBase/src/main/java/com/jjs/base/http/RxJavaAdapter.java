package com.jjs.base.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.CallAdapter;

/**
 * 说明：动态代理替换，添加一些方法
 * Created by jjs on 2018/7/2.
 */

public class RxJavaAdapter implements InvocationHandler {

    private final CallAdapter<?, ?> mCallAdapter;

    public RxJavaAdapter(CallAdapter<?, ?> callAdapter) {
        mCallAdapter = callAdapter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(mCallAdapter, args);
        if (invoke instanceof Observable) {
            Observable observable = (Observable) invoke;
            invoke = observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        }
        return invoke;
    }
}
