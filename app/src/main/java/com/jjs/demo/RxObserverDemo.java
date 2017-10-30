package com.jjs.demo;

import com.blankj.utilcode.util.ToastUtils;
import com.jjs.base.http.BaseObserver;

/**
 * 说明：统一处理返回和异常
 * 继承时设定基础类，具体内容由泛型定义，添加abs标签用于每个请求独立处理
 * Created by aa on 2017/9/20.
 */

public abstract class RxObserverDemo<T> extends BaseObserver<HttpResultDemo<T>> {
    @Override
    protected void _onNext(HttpResultDemo<T> data) {
        if (data.isSuccess()) {
            _onSuccess(data.getBody());
        } else {
            _onError(data.getMsg());
        }
    }

    @Override
    protected void _onComplete() {

    }

    @Override
    protected void _onError(String msg) {
        //失败调用,判断是否展示失败toast
        if (showToast())
            ToastUtils.showShort(msg);
    }

    //请求成功后调用
    protected abstract void _onSuccess(T t);
}
