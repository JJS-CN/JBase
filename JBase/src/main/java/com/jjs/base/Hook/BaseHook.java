package com.jjs.base.Hook;

/**
 * Created by felear on 2018/4/25.
 */

public abstract class BaseHook {

    protected BaseClassHandler mClassHandler;
    protected Object mRealObject;
    protected boolean mEnable = true;

    public void setRealObject(Object realObject) {
        mRealObject = realObject;
    }

    public void setEnableHook(boolean enable) {
        mEnable = enable;
    }

    public void hook(ClassLoader classLoader) throws Throwable {
        mClassHandler.hook(this,classLoader);
    }

    public <T extends BaseClassHandler> BaseHook(T classHandler) {
        mClassHandler = classHandler;

    }

}
