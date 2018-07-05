package com.jjs.base.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 全局异常捕获，不处理系统回收页面造成的空指针异常的补丁方案
 * 在application中调用：
 * Thread.setDefaultUncaughtExceptionHandler(new UEHandler(this, WelcomeActivity.class));
 */
public class UEHandler implements Thread.UncaughtExceptionHandler {
    //错误监听
    static OnUEListener onUEListener;

    /**
     * 将UEhandler设置为app默认异常处理方法
     * 初始化方法
     */
    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler(new UEHandler());
    }

    /**
     * 设置发送异常捕获时的监听
     */
    public static void setOnUEListener(OnUEListener ueListener) {
        onUEListener = ueListener;
    }

    //捕获到异常之后，启动到首页重走逻辑为好
    public static void openLauncher(Application app, Class aClass) {
        Intent intent = new Intent(app, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 返回报错信息。需要先保存到本地，再发送到后台；或者直接发送到后台？
     */
    public static String getCrashText(Context context, Throwable ex) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
        //获取报错的堆栈信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        //获取app的版本号
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        printWriter.close();
        String result = writer.toString();
        String time = formatter.format(new Date());
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("\nModel：" + android.os.Build.MODEL);//手机型号
        stringBuffer.append("\nBrand：" + android.os.Build.BRAND);//手机厂商
        stringBuffer.append("\nSdkVersion：" + android.os.Build.VERSION.RELEASE);//系统版本号
        stringBuffer.append("\nappVersion" + versionCode);//软件版本号
        stringBuffer.append("\nerrorStr：" + result);//报错信息
        stringBuffer.append("\ntime：" + time);//时间
        return stringBuffer.toString();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (onUEListener != null) {
            onUEListener.onUEListerner(t, e);
        }
    }


    public interface OnUEListener {
        void onUEListerner(Thread t, Throwable e);
    }
}