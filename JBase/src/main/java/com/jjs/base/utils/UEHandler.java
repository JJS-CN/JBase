package com.jjs.base.utils;

import android.app.Application;
import android.content.Intent;

import com.jjs.base.activity.BaseLauncherActivity;

/**
 * 全局异常捕获，不处理系统回收页面早册造成的空指针异常的补丁方案
 * 在application中调用：
 * Thread.setDefaultUncaughtExceptionHandler(new UEHandler(this, WelcomeActivity.class));
 */
public class UEHandler implements Thread.UncaughtExceptionHandler {
    private Application app;
    private Class mClass;
    private static boolean isDebug;

    /**
     * applicaiton启动源
     * activity系统第一个页面(需要在oncrate中判断isTaskRoot，是否位于栈底，false直接finish)
     */
    public UEHandler(Application app, Class<? extends BaseLauncherActivity> aClass) {
        this.app = app;
        this.mClass = aClass;

    }

    /**
     * 将UEhandler设置为app默认异常处理方法
     */
    public static void init(Application app, Class<? extends BaseLauncherActivity> aClass) {
        if (!isDebug)
            Thread.setDefaultUncaughtExceptionHandler(new UEHandler(app, aClass));
    }

    public static void openDebug() {
        isDebug = true;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(app, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}