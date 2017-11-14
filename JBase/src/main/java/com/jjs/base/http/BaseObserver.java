package com.jjs.base.http;

import com.blankj.utilcode.util.LogUtils;
import com.jjs.base.widget.LoadingDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 说明：接受消息回调
 * 注意：在onComplete和onError之间可能只调用其中一个，可能全部都调用，所以注意方法的书写
 * 这里要把dialog关闭都写上
 * Created by aa on 2017/9/20.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        LogUtils.i("onNext:" + t.toString());
        _onNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        LoadingDialog.hide();
        String errMsg = "";
        if (e instanceof SocketTimeoutException) {
            errMsg = "网络连接超时，请检查您的网络状态，稍后重试";
        } else if (e instanceof ConnectException) {
            errMsg = "网络连接异常，请检查您的网络状态";
        } else if (e instanceof UnknownHostException) {
            errMsg = "网络异常，请检查您的网络状态";
        } else {
            errMsg = e.getMessage();
        }
        LogUtils.i("onError:" + errMsg);
        _onError(errMsg);

    }

    @Override
    public void onComplete() {
        LoadingDialog.hide();
        _onComplete();
    }

    protected abstract void _onNext(T data);

    protected abstract void _onComplete();

    protected abstract void _onError(String msg);

    /**
     * 外部重写此方法，来控制是否展示错误提示；
     */
    public boolean showToast() {
        return true;
    }
}
