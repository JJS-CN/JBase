package com.jjs.base.widget;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 说明：
 * Created by aa on 2017/9/12.
 */

public class BaseDialogFragment extends DialogFragment implements View.OnClickListener {
    private ViewHolder mViewHolder;//view的集合
    private OnInitViewListener initViewListener;//初始化一次的监听
    private OnBaseClickListener clickListener;//每次点击回调的监听，注意多次setClick会进行覆盖
    private boolean hasTranSparent;//是否设置背景透明

    /**
     * 通过此方法创建
     *
     * @param layoutId 传入显示dialog的布局
     */
    public static BaseDialogFragment getInstance(@LayoutRes int layoutId) {
        BaseDialogFragment dialogFragment = new BaseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public static BaseDialogFragment getInstance(@LayoutRes int layoutId, @ColorInt int BackgroundColor) {
        BaseDialogFragment dialogFragment = new BaseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("color", BackgroundColor); //设置内部布局背景颜色
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //设置内部布局背景颜色
        if (getArguments().getInt("color", 0) != 0) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getArguments().getInt("color", 0)));
        }
        View view = inflater.inflate(getArguments().getInt("layoutId"), container, false);
        mViewHolder = new ViewHolder(view);
        if (initViewListener != null) {
            initViewListener.onInit(mViewHolder);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasTranSparent) {
            //设置背景透明
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0;
            lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(lp);
        }
    }

    /**
     * 设置为需要背景透明
     *
     * @param transparent default false
     */
    public BaseDialogFragment hasTranSparent(boolean transparent) {
        this.hasTranSparent = transparent;
        return this;
    }

    /**
     * 设置返回键和点击屏幕是否能否关闭dialog
     *
     * @param cancelable default true
     */
    public BaseDialogFragment hasCancelable(boolean cancelable) {
        this.setCancelable(cancelable);
        return this;
    }

    /**
     * 批量设置点击事件
     */
    public BaseDialogFragment setOnClick(@IdRes int... viewIds) {
        for (int i = 0; i < viewIds.length; i++) {
            mViewHolder.getView(viewIds[i]).setOnClickListener(this);
        }
        return this;
    }

    /**
     * 点击事件回调
     */
    public BaseDialogFragment setBaseClickListener(OnBaseClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    /**
     * 初始化监听回调
     */
    public BaseDialogFragment setInitViewListener(OnInitViewListener initViewListener) {
        this.initViewListener = initViewListener;
        return this;
    }

    public interface OnBaseClickListener {
        void onClick(ViewHolder mHolder, View v);
    }

    public interface OnInitViewListener {
        void onInit(ViewHolder mHolder);
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onClick(mViewHolder,v);
        }
    }

    /**
     * show方法在api-11之后也许会崩溃，所以进行处理
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException  e) {
            e.printStackTrace();
        }
    }

    /**
     * 控件修改操作类
     */
    public class ViewHolder {
        SparseArray<Object> viewArray = new SparseArray<>();
        private View rootView;

        public ViewHolder(View rootView) {
            if (rootView == null) {
                Log.e("ViewHolder", " create is can set view,but the view is null");
            } else {
                this.rootView = rootView;
            }
        }

        public <T extends View> T getView(@IdRes int viewId) {
            T view = (T) viewArray.get(viewId);
            if (view == null) {
                view = (T) rootView.findViewById(viewId);
                viewArray.put(viewId, view);
            }
            return view;
        }

        public View getRootView() {
            return rootView;
        }

        public void setText(@IdRes int viewId, @Nullable String str) {
            ((TextView) getView(viewId)).setText(str);
        }

        public void setImageResources(@IdRes int viewId, @DrawableRes int drawableId) {
            ((ImageView) getView(viewId)).setImageResource(drawableId);
        }

        public void setClick(@IdRes int viewId) {
            getView(viewId).setOnClickListener(BaseDialogFragment.this);
        }

        public void setDismiss(@IdRes int viewId) {
            getView(viewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        public void dismiss() {
            BaseDialogFragment.this.dismiss();
        }
    }
}
