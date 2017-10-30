package com.jjs.base;

/**
 * 说明：
 * Created by aa on 2017/6/20.
 */

public class BaseStore {
    public static class TAG {
        public static final int RESULT_OK = -1;//activity关闭回调统一标识
        public static final String isFirst = "isFirst";//是否第一次启动app
    }

    public static class HTTP {
        public static String URL_release = "";//正式服务器地址
        public static String URL_debug = "";//测试服务器地址
    }


}
