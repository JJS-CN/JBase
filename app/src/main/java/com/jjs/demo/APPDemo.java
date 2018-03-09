package com.jjs.demo;


import android.os.Looper;

import com.blankj.utilcode.util.LogUtils;
import com.jjs.base.base.BaseApplication;
import com.jjs.base.http.RetrofitUtils;
import com.jjs.base.http.RxBus;

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
        // applyRelease("https://www.yzwptgc.com/");
        new Thread(new Runnable() {
            @Override
            public void run() {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.e("send");
                        RxBus.send(0, null, null);
                        RxBus.send(0, "1", null);
                    }
                });
                new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.e("send");
                        RxBus.send(0, null, null);
                        RxBus.send(0, "1", null);
                    }
                },5000);
            }
        }).start();
    }
}
