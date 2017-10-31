package com.jjs.base.utils.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 说明：分割线，多布局不好使用。传统布局还是不错的
 * Created by aa on 2017/10/31.
 */

public class DivDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private boolean isMeasure;//是否第一次创建

    private boolean hasEndDecoration;
    private boolean isLinearManager;//判断是否为线性布局管理器
    private int spanCount;//grid模式下，item的个数
    private boolean isVertically;//判断是否是竖形滚动
    private int mDivHeight;//分割线宽度
    private Paint mPaint;//分割线颜色所使用的画笔
    private Drawable mDrawable;//图片（优先级高于画笔）

    /**
     * 默认填充画笔为灰色 1宽度
     */
    public DivDecoration(Context context) {
        mContext = context;
        mDivHeight = 1;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
    }

    /**
     * 设置分割线的宽度
     */
    public DivDecoration setDivHeight(double divHeight) {
        this.mDivHeight = dp2px(divHeight) < 1 ? 1 : dp2px(divHeight);
        return this;
    }

    /**
     * 设置分割线的颜色
     */
    public DivDecoration setDivColor(@ColorInt int color) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        return this;
    }

    /**
     * 设置分割线图片，优先于画笔
     */
    public DivDecoration setDivDrawable(@DrawableRes int drawableid) {
        mDrawable = mContext.getResources().getDrawable(drawableid);
        return this;
    }

    /**
     * 设置最后一个item是否需要分割线，默认不需要
     */
    public DivDecoration hasEndDecoration(boolean hasEndDecoration) {
        this.hasEndDecoration = hasEndDecoration;
        return this;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //测绘每个item的显示区域，这时要空出对应区域，以便onDraw进行绘制（最先调用）
        if (!isMeasure) {
            isMeasure = true;
            RecyclerView.LayoutManager manager = parent.getLayoutManager();
            isVertically = manager.canScrollVertically();
            if (manager instanceof StaggeredGridLayoutManager) {
                spanCount = ((StaggeredGridLayoutManager) manager).getSpanCount();
            } else if (manager instanceof GridLayoutManager) {
                spanCount = ((GridLayoutManager) manager).getSpanCount();
            } else if (manager instanceof LinearLayoutManager) {
                isLinearManager = true;
            }
        }
        //如果有值
        if (isLinearManager) {
            if (mDivHeight > 0) {
                if (isVertically) {
                    if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                        outRect.bottom = 0;
                    } else {
                        outRect.bottom = mDivHeight;
                    }
                } else {
                    if (isLastColum(parent, parent.getChildAdapterPosition(view), 1, parent.getAdapter().getItemCount()))// 如果是最后一列，则不需要绘制右边
                    {
                        outRect.left = 0;
                    } else {
                        outRect.left = mDivHeight;
                    }

                }

            }
        } else {
            int childCount = parent.getAdapter().getItemCount();
            if (isLastRaw(parent, parent.getChildAdapterPosition(view), spanCount, childCount))// 如果是最后一行，则不需要绘制底部
            {
                outRect.set(0, 0, mDivHeight, 0);
            } else if (isLastColum(parent, parent.getChildAdapterPosition(view), spanCount, childCount))// 如果是最后一列，则不需要绘制右边
            {
                outRect.set(0, 0, 0, mDivHeight);
            } else {
                outRect.set(0, 0, mDivHeight,
                        mDivHeight);
            }
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (isLinearManager) {
            if (isVertically) {
                drawHorizontal(c, parent);
            } else {
                drawVertical(c, parent);
            }
        } else {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //作用于RecyclerView，所以需要for循环计算Canvas位置，绘制于item之上！(最后调用)
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (!hasEndDecoration && isLinearManager && i == childCount - 1)
                return;
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivHeight;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivHeight;
            if (mDrawable != null) {
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);
            } else if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (!hasEndDecoration && isLinearManager && i == childCount - 1)
                return;
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivHeight;
            if (mDrawable != null) {
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);
            } else if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private int dp2px(double dpValue) {
        double scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
