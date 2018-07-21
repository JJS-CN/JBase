package com.jjs.base;

/**
 * 说明：
 * Created by jjs on 2018/7/10.
 */

public class BaseStore {
    //todo 判断是否登陆，hook会通过此参数值进行取值判断，所以登陆成功后务必修改此参数
    public static final String isLogin = "isLogin";
    //todo 支付时，将通过rxbus进行发送
    public static final String Pay_Result = "PayResult";

    //微信的appId，manifest中需要对应修改
    public static String wxAppId="";
    public static Class mLoginActivity;//登录页，用于登陆判断时的跳转
    public static boolean isDebug = false;//是否是debug模式，开关log打印信息
    public static boolean hasCrash = false;//是否需要全局异常捕获
    public static String BaseUrl = ""; //服务器地址
    public static String BaseImageUrl = "";//图片地址--由于retrofit规定baseUrl必须以/结尾，而图片地址拼接通常不能如意
}
