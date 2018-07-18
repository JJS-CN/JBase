package com.jjs.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 说明：禁止滑动的viewpager
 * 属性：setOffscreenPageLimit  预加载多少个item，在首页时使用为好
 * Created by jjs on 2017/9/15.
 */

public class CustomViewPager extends ViewPager {
    /**
     * 控制是否可以滑动
     * 默认切换效果和手势操作将改变
     */
    private boolean isCanMove = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanMove)
            return super.onInterceptTouchEvent(arg0);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanMove)
            return super.onTouchEvent(arg0);
        else
            return false;
    }

    /**
     * 设置是否可以滑动切换页面
     *
     * @param isCanMove
     */
    public void setCanMove(boolean isCanMove) {
        this.isCanMove = isCanMove;
    }

    /**
     * 设置翻页的时候是否需要动画效果
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, isCanMove);
    }
}
