package com.jjs.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.jjs.base.R;


/**
 * 本页：dialog弹窗，在activity的onCreate和onResume调用init初始化，在onDestroy中关闭dialog
 * 使用hide有时会出现bug：dialog不可见但触摸事件仍被派发给dialog，导致activity所有触摸失效
 * 解决方案：用dissmiss替换hide。错误原理：http://blog.csdn.net/a332324956/article/details/78547642?locationNum=9&fps=1
 * Created by jjs on 2017-03-25.
 * Email:994462623@qq.com
 */

public class LoadingDialog {
    private static Dialog mDialog;

    /**
     * 初始化dialog数据，可动态设置显示view和style
     */
    public static void show(Context context) {
        if (context == null) {
            return;
        }
        if (mDialog != null && mDialog.getContext() == context) {
            mDialog.show();
            return;
        }
        dissmiss();
        mDialog = new Dialog(context, R.style.Dialog_notBG);
        mDialog.setCancelable(true);//按返回键消失
        mDialog.setCanceledOnTouchOutside(true);//但点击dialog范围外不消失
        View mView = View.inflate(context, R.layout.dialog_loadingview, null);
        mDialog.setContentView(mView);
        mDialog.show();
    }

    public static void dissmiss() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
