package com.jjs.demo;


import com.jjs.base.BaseStore;
import com.jjs.base.base.BaseApplication;

/**
 * 说明：
 * Created by aa on 2017/6/19.
 */

public class APPDemo extends BaseApplication {
    {
        BaseStore.BaseUrl = "http://192.168.1.61:8920/";
        BaseStore.isDebug = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //applyDebug("http://116.62.41.38:8072/");
        // applyRelease("https://www.yzwptgc.com/");

    }
}
