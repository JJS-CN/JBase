package com.jjs.base.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jjs.base.adapter.QuickHolder;

/**
 * 说明：
 * Created by jjs on 2018/7/21.
 */

public class BaseBottomDialog extends BottomSheetDialogFragment {
    private View mRootView;
    private BottomSheetBehavior mBehavior;
    private boolean hasExpand;//是否需要最大化显示
    private OnCustomListener mCustomListener;
    public int mLayout;

    public static BaseBottomDialog newInstance(@LayoutRes int layoutId) {
        Bundle args = new Bundle();
        args.putInt("layout", layoutId);//布局文件
        BaseBottomDialog fragment = new BaseBottomDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layout = getArguments().getInt("layout");
        if (layout != 0) {
            mLayout = layout;
        }
        mRootView = inflater.inflate(mLayout, container);
        if (mCustomListener != null) {
            QuickHolder viewHolder = new QuickHolder(mRootView);
            mCustomListener.onCustom(viewHolder, this);
        }
        return mRootView;
    }

    public void setCustomListener(OnCustomListener customListener) {
        this.mCustomListener = customListener;
    }

    public interface OnCustomListener {
        void onCustom(BaseViewHolder holder, BottomSheetDialogFragment dialog);
    }

    public void setHasExpand(boolean hasExpand) {
        this.hasExpand = hasExpand;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasExpand) {
            if (mBehavior == null) {
                mBehavior = BottomSheetBehavior.from((View) mRootView.getParent());
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }
}
