package com.jjs.base;

/**
 * 说明：
 * Created by aa on 2017/6/20.
 */

public class Store {
    public static class TAG {
        final public static int RESULT_OK = -1;//activity关闭回调统一标识
    }

    public static class LOGIN {
        final public static int SEND_SMS = 0;
        final public static int REGISTER = 1;
        final public static int LOGIN = 2;
        final public static int RESET_PWD = 3;
    }

    public static class HTTP {
        final public static String URL = "url";
    }

}
