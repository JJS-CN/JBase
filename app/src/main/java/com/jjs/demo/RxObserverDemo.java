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
        //HttpResultDemo需要根据服务器返回方式自定义，
        //然后根据服务器返回成功与否标识，执行不同方法
        if (data.isSuccess()) {
            _onSuccess(data.getBody());
        } else {
            _onError(data.getMsg());
        }
    }

    @Override
    protected void _onComplete() {

    }

    /**
     * 如果需要进行失败时重复请求，直接实现此方法，从新调用请求方法
     */
    @Override
    protected void _onError(String msg) {
        //失败调用,判断是否展示失败toast
        if (showToast())
            ToastUtils.showShort(msg);
    }

    //请求成功后调用
    protected abstract void _onSuccess(T t);

    /**
     * 复写 showToast ，设定返回boolean可控制是否展示失败toast信息
     */

}
