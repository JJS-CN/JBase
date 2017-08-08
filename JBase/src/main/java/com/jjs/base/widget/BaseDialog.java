package com.jjs.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.View;

/**
 * 说明：
 * Created by aa on 2017/8/7.
 */

public class BaseDialog implements View.OnClickListener {
    private Dialog dialog;
    private Context context;
    private View view;
    private int viewId;
    private int[] clickIds;
    private OnDialogClickListener clickListener;

    public BaseDialog(@NonNull Context context) {
        this.context = context;
        dialog = new Dialog(context);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        this.context = context;
        dialog = new Dialog(context, themeResId);
    }

    /**
     * 设置显示的viewId
     */
    public BaseDialog setView(@LayoutRes int viewId) {
        this.viewId = viewId;
        return this;
    }

    /**
     * 设置需要进行点击操作的clickViewId
     */
    public BaseDialog setClick(@IdRes int... clickIds) {
        this.clickIds = clickIds;
        return this;
    }

    /**
     * view点击之后进行的操作
     */
    public BaseDialog setListener(OnDialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    /**
     * 设置系统返回键是否有效
     */
    public BaseDialog setCancelable(boolean cancelSys) {
        dialog.setCancelable(cancelSys);
        return this;
    }

    /**
     * 设置点击dialog屏幕外是否有效
     */
    public BaseDialog setCanceledOnTouchOutside(boolean cancelTouch) {
        dialog.setCanceledOnTouchOutside(cancelTouch);
        return this;
    }

    /**
     * 设置dialog退出监听
     */
    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
    }

    /**
     * 必须调用create方法设置view和点击事件
     * 之后返回dialog对象进行控制操作
     */
    public Dialog create() {
        if (viewId != 0) {
            Log.e("BaseDialog", "you can setView of show viewId");
            return dialog;
        } else {
            view = View.inflate(context, viewId, null);
        }
        if (clickIds == null || clickListener == null) {
            Log.e("BaseDialog", "you not set clickIds or clickListener,so don't setClick");
        } else {
            for (int i = 0; i < clickIds.length; i++) {
                View clickView = view.findViewById(clickIds[i]);
                if (clickView != null) {
                    clickView.setOnClickListener(this);
                }
            }
        }
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onClick(view, v);
        }
    }

    public interface OnDialogClickListener {
        void onClick(View rootView, View checkView);
    }
}
