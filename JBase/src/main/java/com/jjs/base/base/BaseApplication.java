package com.jjs.base.base;

/**
 * 说明：分包方案，未测试：
 * 需要init的有：SharedUtil、L、NetworkUtil
 * Created by aa on 2017/6/19.
 */

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.jjs.base.BaseStore;
import com.jjs.base.Hook.HookFactory;
import com.jjs.base.utils.UEHandler;

import java.lang.ref.WeakReference;

public abstract class BaseApplication extends Application {
    private static Application sInstance;
    private static WeakReference<Activity> mCurrentActivityWeakRef;

    public static Application get() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(BaseStore.isDebug);
        if (BaseStore.hasCrash) {
            UEHandler.init();
        }
        if (BaseStore.mLoginActivity != null) {
            // hook 登录跳转
            ComponentName componentName = new ComponentName(getPackageName(), BaseStore.mLoginActivity.getName());
            HookFactory.hookIActivityManager(Thread.currentThread().getContextClassLoader()
                    , componentName);
        }
        //监听会被add到list集合中，走生命周期时会被遍历调用
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                mCurrentActivityWeakRef = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static Activity getActivity() {
        if (mCurrentActivityWeakRef != null) {
            return mCurrentActivityWeakRef.get();
        }
        return null;
    }
}
