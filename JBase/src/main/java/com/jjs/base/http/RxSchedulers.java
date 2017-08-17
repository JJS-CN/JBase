package com.jjs.base.http;


import com.jjs.base.widget.LoadingDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 本页：传入是否显示loading、内存溢出方法   (默认开启loading)
 * Created by jjs on 2017-03-24.
 * Email:994462623@qq.com
 */

public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> io_main(final LifecycleTransformer lifecycle) {
        return createObservable(true, lifecycle);
    }

    public static <T> ObservableTransformer<T, T> io_main(final boolean showLoading, final LifecycleTransformer lifecycle) {
        return createObservable(showLoading, lifecycle);
    }

    /**
     * @param showLoading 是否展示loading窗
     * @param lifecycle   解决rxjava的lifecycle内存溢出问题
     */
    private static <T> ObservableTransformer<T, T> createObservable(final boolean showLoading, final LifecycleTransformer lifecycle) {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.newThread()).doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //设定是否开启dialog窗
                        if (showLoading)
                            LoadingDialog.show();
                    }
                }).compose(lifecycle)//绑定Lifecycle,解决网络请求内存溢出问题
                        .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
      /*     return upstream=new ObservableTransformer<T,T>()
                upstream.subscribeOn(Schedulers.newThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                LoadingDialog.show();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                        .observeOn(AndroidSchedulers.mainThread());*/
}
