package com.jjs.demo;


import com.jjs.base.JJsStore;
import com.jjs.base.MultiDexAPP;

/**
 * 说明：
 * Created by aa on 2017/6/19.
 */

public class APPDemo extends MultiDexAPP {
    @Override
    public void onCreate() {
        this.isDebug = true;
        JJsStore.HTTP.URL_release = "http://116.62.41.38:8072/";
        JJsStore.HTTP.URL_debug = "http://apis.baidu.com/idl_baidu/clothing_classification/";
        super.onCreate();


    }
}
