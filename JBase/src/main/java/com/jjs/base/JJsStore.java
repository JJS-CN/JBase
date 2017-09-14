package com.jjs.base;

/**
 * 说明：
 * Created by aa on 2017/6/20.
 */

public class JJsStore {
    public static class TAG {
        public static final int RESULT_OK = -1;//activity关闭回调统一标识
    }


    public static class HTTP {
        public static final String URL = "url";
        public static String URL_release = "";//正式服务器地址
        public static String URL_debug = "";//测试服务器地址
    }

    public static class APK {
        public static final String UPDATE_ALL = "update_allCount";//总长度
        public static final String UPDATE_NOW = "update_nowCount";//已下载长度
    }

}
