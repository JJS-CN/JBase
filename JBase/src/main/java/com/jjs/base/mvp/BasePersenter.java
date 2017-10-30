package com.jjs.base.mvp;

/**
 * 说明：使用rxjava和retrofit时，使用Lifecycle进行防内存溢出问题
 * Created by aa on 2017/6/28.
 */

public abstract class BasePersenter<V extends BaseView> {
    //view层
    public V mView;

    public BasePersenter(V view) {
        this.mView = view;


    }


    public void destroy() {
        mView = null;
    }

}
