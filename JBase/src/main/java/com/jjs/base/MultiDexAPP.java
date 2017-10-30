package com.jjs.base;

/**
 * 说明：分包方案，未测试：
 * 需要init的有：SharedUtil、L、NetworkUtil
 * Created by aa on 2017/6/19.
 */

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.jjs.base.activity.LoadResActivity;
import com.jjs.base.http.RetrofitUtils;
import com.jjs.base.utils.UEHandler;

import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public abstract class MultiDexAPP extends Application {
    private static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";
    private static boolean isDebug = false;

    public void onCreate() {
        super.onCreate();
        if (quickStart()) {
            return;
        }
    }

    /**
     * 使用此方法来控制debug模式，请务必调用
     */
    public void initDebug(boolean debug, String url_debug, String url_release) {
        isDebug = debug;
        BaseStore.HTTP.URL_debug = url_debug;
        BaseStore.HTTP.URL_release = url_release;
        if (debug) {
            UEHandler.openDebug();
        }
        initUtils();
    }

    public boolean getDebug() {
        return isDebug;
    }

    /**
     * 进行工具类的一些初始化操作。
     */
    private void initUtils() {
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(isDebug);
        RetrofitUtils.init(isDebug ? BaseStore.HTTP.URL_debug : BaseStore.HTTP.URL_release);
    }

    /**
     * 主要！！！这个方法会在oncreate之前调用，在此方法中进行dex分包加载
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d("loadDex", "App attachBaseContext ");
        if (!quickStart() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//>=5.0的系统默认对dex进行oat优化
            if (needWait(base)) {
                waitForDexopt(base);
            }
            MultiDex.install(this);
        } else {
            return;
        }
    }


    public boolean quickStart() {
        if (getCurProcessName(this).contains(":mini")) {
            Log.d("loadDex", ":mini start!");
            return true;
        }
        return false;
    }

    //neead wait for dexopt ?
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        Log.d("loadDex", "dex2-sha1 " + flag);
        SharedPreferences sp = context.getSharedPreferences(getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");
        return !flag.equals(saveValue);
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MultiDextAPP", e.getLocalizedMessage());
        }
        return new PackageInfo();
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }


    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.zongwu", LoadResActivity.class.getName());
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        base.startActivity(intent);
        long startWait = System.currentTimeMillis();
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                Log.d("loadDex", "wait ms :" + nowWait);
                if (nowWait >= waitTime) {
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
