package com.jjs.base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseViewHolder;


/**
 * Created by jjs on 2018/5/28.
 */

public class BaseDialog extends Dialog {
    private int mLayoutId;//dialog布局
    private OnCustomListener mCustomListener;

    public BaseDialog(@NonNull Context context, @LayoutRes int layoutId) {
        super(context);
        mLayoutId = layoutId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mCustomView = View.inflate(getContext(), mLayoutId, null);
        if (mCustomListener != null) {
            BaseViewHolder viewHolder = new BaseViewHolder(mCustomView);
            mCustomListener.onCustom(viewHolder, BaseDialog.this);
        }
        setContentView(mCustomView);
    }

    public void setCustomListener(OnCustomListener customListener) {
        this.mCustomListener = customListener;
    }

    public interface OnCustomListener {
        void onCustom(BaseViewHolder holder, BaseDialog dialog);
    }

    @Override
    public void show() {
        super.show();
        if (getContext() instanceof Activity) {
            Window window = this.getWindow();
            Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (display.getWidth() * 0.8);
            window.setAttributes(params);
        }
    }
}
