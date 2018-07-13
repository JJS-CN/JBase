package com.jjs.base.http;

import android.util.Log;

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
          Log.e("eeee", "1");
            Observable observable = (Observable) invoke;
           /*   LifecycleTransformer transformer = null;
            if (BaseObserver.mRxActivity != null) {
                transformer = BaseObserver.mRxActivity.bindToLifecycle();
            } else if (BaseObserver.mRxFragment != null) {
                transformer = BaseObserver.mRxFragment.bindToLifecycle();
            } else if (BaseObserver.mRxDialogFragment != null) {
                transformer = BaseObserver.mRxDialogFragment.bindToLifecycle();
            }
            if (transformer != null) {
                invoke = observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(transformer);//绑定Lifecycle,解决网络请求内存溢出问题
            } else {

            }*/
            invoke = observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        }
        return invoke;
    }
}
