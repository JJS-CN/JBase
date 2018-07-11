package com.jjs.base.Hook;

import java.lang.reflect.Method;

/**
 * Created by felear on 2018/4/25.
 */

public abstract class BaseMethodHandler {

    protected abstract boolean beforeHood(Object realObject, Method method, Object[] args);

    protected abstract void afterHood(Object realObject, Method method, Object[] args);

    public Object innerHood(Object realObject, Method method, Object[] args) throws Throwable {
        Object result = null;
        // 返回true时拦截hook
        if (!beforeHood(realObject, method, args)) {
            result = method.invoke(realObject, args);
        }
        afterHood(realObject, method, args);
        return result;
    }

}
