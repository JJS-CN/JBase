package com.jjs.base.base;

/**
 * 说明：分包方案，未测试：
 * 需要init的有：SharedUtil、L、NetworkUtil
 * Created by aa on 2017/6/19.
 */

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.jjs.base.http.RetrofitUtils;
import com.jjs.base.utils.UEHandler;

public abstract class BaseApplication extends Application {
    private static boolean isDebug = false;
    private boolean hasCrash = true;
    public static String BaseUrl = "";


    /**
     * 打开debug模式
     */
    public void applyDebug(String baseUrl) {
        BaseUrl=baseUrl;
       initUtils(true);
    }

    /**
     * 打开release模式
     */
    public void applyRelease(String baseUrl) {
        BaseUrl=baseUrl;
        initUtils(false);
    }

    /**
     * 传入基础Url服务器地址
     */
    private void initUtils(boolean debug) {
        isDebug=debug;
        if (hasCrash) {
            UEHandler.init(isDebug);
        }
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(isDebug);
        RetrofitUtils.init(BaseUrl);
        if (isDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }

    /**
     * 设置是否打开carsh模式；默认为true；
     * 为false时不开启。
     * 为true时根据isDebug设置开启。
     */
    public void openCrash(boolean hasCrash) {
        this.hasCrash = hasCrash;
    }

    public boolean getDebug() {
        return isDebug;
    }

}
