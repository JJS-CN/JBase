package com.jjs.base.Hook;

import android.content.ComponentName;
import android.util.Log;

/**
 * Created by felear on 2018/4/25.
 */

public class HookFactory {

    private static final String TAG = "HookFactory";

    public static void hookIActivityManager(ClassLoader classLoader, ComponentName componentName) {
        IActivityManagerHandler iActivityManagerHandler = new IActivityManagerHandler();
        BaseHook iActivityManagerHook = new BaseProxyHook(iActivityManagerHandler);

        iActivityManagerHandler.init(componentName);
        try {
            iActivityManagerHook.hook(classLoader);
        } catch (Throwable throwable) {
            Log.e(TAG, "hookIActivityManager: " + throwable);
        }
    }

}
