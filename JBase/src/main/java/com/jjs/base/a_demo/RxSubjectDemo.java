package com.jjs.base.a_demo;

import com.blankj.utilcode.util.ToastUtils;
import com.jjs.base.http.RxSubject;
import com.jjs.base.widget.LoadingDialog;

/**
 * 说明：
 * Created by aa on 2017/6/21.
 */

public abstract class RxSubjectDemo<T> extends RxSubject<HttpResultDemo<T>> {
    //继承时设定基础类，具体内容由泛型定义，添加abs标签用于每个请求独立处理


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
        //流程走完，关闭loading，由于compose时开启了loading窗
        LoadingDialog.hide();
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
