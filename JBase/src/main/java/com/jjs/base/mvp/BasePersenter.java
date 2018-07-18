package com.jjs.base.mvp;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxDialogFragment;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * 说明：使用rxjava和retrofit时，使用Lifecycle进行防内存溢出问题
 * Created by aa on 2017/6/28.
 */

public abstract class BasePersenter<V extends BaseView> {
    //view层
    public V mView;
    public RxAppCompatActivity rxActivity;
    public RxFragment rxFragment;
    public RxDialogFragment rxDialogFragment;

    public BasePersenter(RxAppCompatActivity rxActivity, V view) {
        this.rxActivity = rxActivity;
        this.mView = view;
    }

    public BasePersenter(RxFragment rxFragment, V view) {
        this.rxFragment = rxFragment;
        this.mView = view;
    }

    public BasePersenter(RxDialogFragment rxDialogFragment, V view) {
        this.rxDialogFragment = rxDialogFragment;
        this.mView = view;
    }

    public void destroy() {
        mView = null;
    }

}
