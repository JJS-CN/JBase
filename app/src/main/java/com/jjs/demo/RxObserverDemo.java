package com.jjs.demo;

import com.jjs.base.http.BaseObserver;

/**
 * 说明：统一处理返回和异常
 * 继承时设定基础类，具体内容由泛型定义，添加abs标签用于每个请求独立处理
 * Created by aa on 2017/9/20.
 */

public abstract   class RxObserverDemo<T extends BaseEntity> extends BaseObserver<T> {

}
