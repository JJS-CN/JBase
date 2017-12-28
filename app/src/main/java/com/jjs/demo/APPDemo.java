package com.jjs.demo;


import com.jjs.base.base.BaseApplication;
import com.jjs.base.http.RetrofitUtils;

/**
 * 说明：
 * Created by aa on 2017/6/19.
 */

public class APPDemo extends BaseApplication {
    public static String BaseUrl = "";

    @Override
    public void onCreate() {
        super.onCreate();

        applyDebug();
        applyRelease();
        String debugUrl = "http://116.62.41.38:8072/";
        String releaseUrl = "https://www.yzwptgc.com/";
        BaseUrl = getDebug() ? debugUrl : releaseUrl;
        RetrofitUtils.initInterceptor(new YzwInterceptor());
        initUtils(BaseUrl);
    }
}
