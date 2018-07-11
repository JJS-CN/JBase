package com.jjs.base.Hook;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.jjs.base.BaseStore;
import com.jjs.base.annotation.NeedLogin;
import com.jjs.base.annotation.NeedPermission;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by felear on 2018/4/25.
 */

public class IActivityManagerHandler extends BaseClassHandler {

    private static final String TAG = "IActivityManagerHandler";
    private ComponentName mComponentName;

    public void init(ComponentName componentName) {
        mComponentName = componentName;
    }

    // 传入classloader
    @Override
    public void hook(BaseHook baseHook, ClassLoader classLoader) throws Throwable {

        if (!(baseHook instanceof BaseProxyHook)) {
            Log.e(TAG, "BaseProxyHook");
            return;
        }

        BaseProxyHook baseProxyHook = (BaseProxyHook) baseHook;
        Field gDefault = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Class<?> amClass = Class.forName("android.app.ActivityManager");
            gDefault = amClass.getDeclaredField("IActivityManagerSingleton");
        } else {
            Class<?> amClass = Class.forName("android.app.ActivityManagerNative");
            gDefault = amClass.getDeclaredField("gDefault");
        }
        gDefault.setAccessible(true);
        Object iAmSingleton = gDefault.get(null);
        Log.e(TAG, "initHook: " + iAmSingleton);

        Class<?> singleTonClass = Class.forName("android.util.Singleton");
        Field mInstance = singleTonClass.getDeclaredField("mInstance");
        mInstance.setAccessible(true);

        Object ams = mInstance.get(iAmSingleton);

        // 获取IActivityManager类
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        baseProxyHook.setRealObject(ams);
        Object mAms = Proxy.newProxyInstance(classLoader
                , new Class[]{iActivityManagerClass}
                , baseProxyHook);

        mInstance.set(iAmSingleton, mAms);

        Log.e(TAG, "initHook: " + ams);


    }


    @Override
    protected void initMethod() {
        addMethod("startActivity", new startActivityHandler());
    }

    class startActivityHandler extends BaseMethodHandler {

        @Override
        protected boolean beforeHood(Object realObject, Method method, final Object[] args) {
            // 找出intent
            if (SPUtils.getInstance().getBoolean(BaseStore.isLogin)) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        final Intent arg = (Intent) args[i];
                        // 判断是否在名单中
                        ComponentName component = arg.getComponent();
                        //通过Uri调取外部地址时获取不到包信息，所以判断为null时return
                        if (component == null)
                            return false;
                        try {
                            Class<?> tClass = Class.forName(component.getClassName());
                            NeedLogin annLogin = tClass.getAnnotation(NeedLogin.class);
                            NeedPermission annPermission = tClass.getAnnotation(NeedPermission.class);
                            if (annLogin != null) {
                                //先判断是否需要登陆
                                Intent intent = new Intent();
                                intent.putExtra("intent", arg);
                                intent.setComponent(mComponentName);
                                args[i] = intent;
                            } else if (annPermission != null) {
                                //再判断是否需要权限
                                final int finalI = i;
                                PermissionUtils.permission(annPermission.value())
                                        .callback(new PermissionUtils.SimpleCallback() {
                                            @Override
                                            public void onGranted() {
                                                Intent intent = new Intent();
                                                intent.putExtra("intent", arg);
                                                intent.setComponent(mComponentName);
                                                args[finalI] = intent;
                                            }

                                            @Override
                                            public void onDenied() {
                                            }
                                        }).request();
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return false;
        }

        @Override
        protected void afterHood(Object realObject, Method method, Object[] args) {

        }
    }
}
