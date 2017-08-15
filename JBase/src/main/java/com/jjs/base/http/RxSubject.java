package com.jjs.base.http;

import android.util.Log;

import com.jjs.base.widget.LoadingDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;

/**
 * 本页：数据处理，需要在实际项目中创建继承此项。进行自己的判断是否请求工作（比如token校验）
 * Created by jjs on 2017-03-25.
 * Email:994462623@qq.com
 */

public abstract class RxSubject<T> extends Subject<T> {
    @Override
    public boolean hasObservers() {
        return false;
    }

    @Override
    public boolean hasThrowable() {
        return false;
    }

    @Override
    public boolean hasComplete() {
        return false;
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
    }


    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T result) {
        Log.i(this.getClass().getName(), "拿到数据:" + result.toString());
        _onNext(result);
    }


    @Override
    public void onError(Throwable e) {
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
        Log.i(this.getClass().getName(), "onError:----" + errMsg);
        _onError(errMsg);
    }

    @Override
    public void onComplete() {
        Log.i(this.getClass().getName(), "结束，关闭加载动画");
        LoadingDialog.hide();
        _onComplete();
    }

    protected abstract void _onNext(T data);

    protected abstract void _onComplete();

    protected abstract void _onError(String msg);

    public boolean showToast() {
        return true;
    }

}
