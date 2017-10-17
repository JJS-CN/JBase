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
 * LifecycleTransformer 将在组件停止运行时，自动取消请求，防止内存溢出
 * Created by jjs on 2017-03-24.
 * Email:994462623@qq.com
 */

public class RxSchedulers {
    //是否展示loading窗
   private boolean showLoading=true;
    //解决rxjava的lifecycle内存溢出问题
    private  LifecycleTransformer lifecycle;

    public RxSchedulers(LifecycleTransformer lifecycle){
        this.lifecycle=lifecycle;
    }
    public static RxSchedulers getInstance(LifecycleTransformer lifecycle){
        return new RxSchedulers(lifecycle);
    }

    public RxSchedulers showLoading(boolean show){
        showLoading=show;
        return this;
    }


    public  <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
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
}
