package com.jjs.base.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 点击事件拦截。用于提交信息的二次展示
 * 此时UI大部分不能点击，但少部分需要可触发click跳转
 * Created by jjs on 2018/4/25.
 */

public class TouchLinearLayout extends LinearLayout {
    private boolean hasIntercept;
    private List<View> mHasTouchViewList = new ArrayList<>();

    public TouchLinearLayout(Context context) {
        super(context);
    }

    public TouchLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置是否需要拦截触摸事件
     */
    public void hasInterceptTouch(boolean hasIntercept) {
        this.hasIntercept = hasIntercept;
        if (hasIntercept) {
            this.setFocusable(true);
            this.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        } else {
            this.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        }
    }

    /**
     * 添加对应控件到过滤列表
     */
    public void addClickViews(View... views) {
        mHasTouchViewList.addAll(Arrays.asList(views));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (hasIntercept) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                for (int i = 0; i < mHasTouchViewList.size(); i++) {
                    if (canClick(mHasTouchViewList.get(i), ev)) {
                        return super.onInterceptTouchEvent(ev);
                    }
                }
            } else {
                return super.onInterceptTouchEvent(ev);
            }
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    /**
     * 当点击view为 group/下层view 时，会判断 子view/上层view 是否设置点击事件，设置的话此次点击无效
     */
    private boolean canClick(View view, MotionEvent ev) {
        if (inRangeOfView(view, ev)) {
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View item = group.getChildAt(i);
                    if (inRangeOfView(item, ev) && item.hasOnClickListeners()) {
                        return false;
                    }
                }
            } else {
                ViewGroup group = (ViewGroup) view.getParent();
                boolean isViewPosition = false;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View item = group.getChildAt(i);
                    if (view.getId() == item.getId()) {
                        isViewPosition = true;
                        continue;
                    }
                    if (!isViewPosition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (view.getZ() > item.getZ()) {
                                //如果z轴大于item不处理
                                continue;
                            }
                        }
                    }
                    if (inRangeOfView(item, ev) && item.hasOnClickListeners()) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        float touchX = ev.getRawX();
        float touchY = ev.getRawY();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int minX = location[0];//控件在屏幕中的位置
        int minY = location[1];

        int maxX = minX + view.getWidth();
        int maxY = minY + view.getHeight();
        return touchX >= minX && touchX <= maxX && touchY >= minY && touchY <= maxY;
    }


}
