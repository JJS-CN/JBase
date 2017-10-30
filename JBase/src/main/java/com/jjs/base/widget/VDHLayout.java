package com.jjs.base.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：可拖动view的group,只能控制下层view，多层无法触发
 * vdhLayout.setHasDragView(false); //设置是否能够拖动
 * vdhLayout.setHasInView(true);    //设置子view移动是否限制在group之内
 * vdhLayout.setLeftDragView(textview4,false);  //设置需要左滑
 * vdhLayout.setResetAfter(textview4,true);  //设置需要重置状态
 * vdhLayout.setCallBackListener(new VDHLayout.CallBackListener() {});  //设置各种监听,用于自定义操作
 * Created by aa on 2017/10/25.
 */

public class VDHLayout extends LinearLayout {
    private ViewDragHelper mDragHelper;
    private boolean hasInView = true;//是否需要itemView存在于view之中
    private boolean hasDragView = true;//是否能够移动view
    private Map<View, Point> fillAfterMap = new HashMap<>();//松开按钮返回的view列表
    private CallBackListener listener;//监听回调
    private View leftDragView;//左滑的view
    private boolean hasLeftTryCap;//左滑是否还需要能够点击拖动

    public VDHLayout(Context context) {
        super(context);
        init(context);
    }

    public VDHLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VDHLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //参数：1、父容器   2、灵敏度，值越大越。。  3、监听回调方法
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //系统依次返回父容器中的子view，如果返回true则表示可以捕获该view；（可以捕获，后续拖动操作还是不行的）
                boolean capTure = hasDragView;
                if (listener != null) {
                    capTure = listener.tryCaptureView(child, pointerId);
                }
                if (leftDragView == child && !hasLeftTryCap) {
                    capTure = false;
                }
                return capTure;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //拖动时一直回调，设置拖动view的x位置
                if (listener != null) {
                    return listener.clampViewPositionHorizontal(child, left, dx);
                }
                return clampHorizontal(child, left, dx);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                //需要重写这2个方法，因为默认返回值一直为0。
                if (listener != null) {
                    return listener.clampViewPositionVertical(child, top, dy);
                }
                return clampVertical(child, top, dy);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //手指释放的时候回调
                //左滑的控制手势，不会回调此方法
                for (View key : fillAfterMap.keySet()) {
                    //需要回弹的参数
                    if (key == releasedChild) {
                        mDragHelper.settleCapturedViewAt(fillAfterMap.get(key).x, fillAfterMap.get(key).y);
                        invalidate();
                        return;
                    }
                }
                if (listener != null) {
                    listener.onViewReleased(releasedChild, xvel, yvel);
                }
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                if (leftDragView != null) {
                    mDragHelper.captureChildView(leftDragView, pointerId);
                } else {
                    super.onEdgeDragStarted(edgeFlags, pointerId);
                }
            }
        });
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    /**
     * 默认横向处理
     */
    public int clampHorizontal(View child, int left, int dx) {
        if (hasInView) {
            if (left < 0) {
                //进行左边距的限制
                left = 0;
            } else if (left > VDHLayout.this.getWidth() - child.getWidth()) {
                left = VDHLayout.this.getWidth() - child.getWidth();
            }
        }
        return left;
    }

    /**
     * 默认竖向处理
     */
    public int clampVertical(View child, int top, int dy) {
        if (hasInView) {
            if (top < 0) {
                top = 0;
            } else if (top > VDHLayout.this.getHeight() - child.getHeight()) {
                top = VDHLayout.this.getHeight() - child.getHeight();
            }
        }
        return top;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (hasDragView) {
            return mDragHelper.shouldInterceptTouchEvent(event);
        } else {
            return super.onInterceptTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (hasDragView) {
            mDragHelper.processTouchEvent(event);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    /**
     * 设置是否能够拖动view
     */
    public void setHasDragView(boolean hasDragView) {
        this.hasDragView = hasDragView;
    }

    /**
     * 设置子view是否显示在viewGroup之内
     */
    public void setHasInView(boolean hasInView) {
        this.hasInView = hasInView;
    }


    /**
     * 设置需要松开回弹的view
     */
    public void setResetAfter(View view) {
        setResetAfter(view, true);
    }

    /**
     * 设置需要松开回弹的view
     *
     * @param view        view
     * @param isFillAfter true需要，false不需要（用于清除）
     */
    public void setResetAfter(final View view, boolean isFillAfter) {
        boolean hasKey = fillAfterMap.containsKey(view);
        //需要添加
        if (isFillAfter) {
            if (hasKey) {
                return;//已经存在直接返回
            } else {
                //不存在，进行添加
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        if (view == null)
                            return;
                        Point point = new Point();
                        point.x = view.getLeft();
                        point.y = view.getTop();
                        fillAfterMap.put(view, point);
                    }
                });
            }
        } else {
            //不添加
            if (hasKey) {
                //有数据，进行删除
                fillAfterMap.remove(view);
            } else {
                return;
            }
        }
    }

    /**
     * 设置需要左滑的view
     */
    public void setLeftDragView(View view) {
        setLeftDragView(view, false);
    }

    /**
     * 设置需要左滑的view
     *
     * @param view          view的id
     * @param hasLeftTryCap 是否仍然需要能够进行拖动，默认为false
     */
    public void setLeftDragView(View view, boolean hasLeftTryCap) {
        this.hasLeftTryCap = hasLeftTryCap;
        if (leftDragView != null) {
            Log.e("VDHLayout", "The LeftDragView must be singleton");
        }
        leftDragView = view;
    }

    /**
     * 设置监听回调，进行自定义操作
     *
     * @param listener
     */
    public void setCallBackListener(CallBackListener listener) {
        this.listener = listener;
    }


    public interface CallBackListener {
        boolean tryCaptureView(View child, int pointerId);

        void onViewReleased(View releasedChild, float xvel, float yvel);

        int clampViewPositionHorizontal(View child, int left, int dx);

        int clampViewPositionVertical(View child, int top, int dy);
    }
}
