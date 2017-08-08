package com.jjs.demo;

import com.jjs.base.a_demo.HttpResultDemo;
import com.jjs.base.http.RxSubject;

/**
 * 说明：
 * Created by aa on 2017/6/21.
 */

public abstract class RxSubDemo<T> extends RxSubject<HttpResultDemo<T>> {
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
        //流程走完，关闭

    }

    @Override
    protected void _onError(String msg) {

    }

    protected abstract void _onSuccess(T t);
}
