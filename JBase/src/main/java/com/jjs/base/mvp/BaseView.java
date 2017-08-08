package com.jjs.base.mvp;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 说明：bindToLifecycle为解决内存溢出问题，必须实现供P层使用。
 * Created by aa on 2017/6/28.
 */

public interface BaseView {
     <T> LifecycleTransformer<T> bindToLifecycle();
}
