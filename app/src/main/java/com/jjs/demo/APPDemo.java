package com.jjs.demo;


import com.jjs.base.base.BaseApplication;
import com.jjs.base.http.RetrofitUtils;

/**
 * 说明：
 * Created by aa on 2017/6/19.
 */

public class APPDemo extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitUtils.initInterceptor(new YzwInterceptor());
        applyDebug("http://116.62.41.38:8072/");
        applyRelease("https://www.yzwptgc.com/");

    }
}
