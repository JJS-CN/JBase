package com.jjs.base.Hook;

import java.util.TreeMap;

/**
 * Created by felear on 2018/4/25.
 */

public abstract class BaseClassHandler {

    private static final String TAG = "BaseClassHandler";

    private TreeMap<String, BaseMethodHandler> mMethodMap;

    public BaseClassHandler() {
        mMethodMap = new TreeMap<>();
        initMethod();
    }

    public abstract void hook(BaseHook baseHook,ClassLoader classLoader) throws Throwable;

    protected abstract void initMethod();

    public BaseMethodHandler getMethod(String name) {
        return mMethodMap.get(name);
    }

    protected void addMethod(String name, BaseMethodHandler methodHandler) {
        mMethodMap.put(name, methodHandler);
    }

}
