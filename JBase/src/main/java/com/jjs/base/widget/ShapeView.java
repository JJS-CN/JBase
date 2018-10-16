package com.jjs.base.widget;

/**
 * 代码设置shape，免去xml绘制shape文件
 * Created by Administrator on 2017/5/24 0024.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jjs.base.R;


public class ShapeView extends android.support.v7.widget.AppCompatTextView {
    int solidColor, stroke_Color, touchColor, touchTextColor, normalTextColor, enableColor, enableTextColor;
    int cornesRadius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius, stroke_Width, strokeDashWidth, strokeDashGap, shapeType;
    GradientDrawable gradientDrawable = new GradientDrawable();

    public ShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }

    public ShapeView(Context context) {
        super(context);
    }

    private void initData(Context context, AttributeSet attrs) {
        setClickable(true);
        normalTextColor = getCurrentTextColor();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
        touchTextColor = a.getColor(R.styleable.ShapeView_touchTextColor, getCurrentTextColor());
        solidColor = a.getColor(R.styleable.ShapeView_solidColor, Color.GRAY);
        stroke_Color = a.getColor(R.styleable.ShapeView_stroke_Color, Color.TRANSPARENT);
        touchColor = a.getColor(R.styleable.ShapeView_touchSolidColor, Color.TRANSPARENT);
        enableColor = a.getColor(R.styleable.ShapeView_enableColor, solidColor);
        enableTextColor = a.getColor(R.styleable.ShapeView_enableTextColor, getCurrentTextColor());
        cornesRadius = (int) a.getDimension(R.styleable.ShapeView_cornesRadius, 0);
        topLeftRadius = (int) a.getDimension(R.styleable.ShapeView_topLeftRadius, 0);
        topRightRadius = (int) a.getDimension(R.styleable.ShapeView_topRightRadius, 0);
        bottomLeftRadius = (int) a.getDimension(R.styleable.ShapeView_bottomLeftRadius, 0);
        bottomRightRadius = (int) a.getDimension(R.styleable.ShapeView_bottomRightRadius, 0);
        stroke_Width = (int) a.getDimension(R.styleable.ShapeView_stroke_Width, 0);
        strokeDashWidth = (int) a.getDimension(R.styleable.ShapeView_strokeDashWidth, 0);
        strokeDashGap = (int) a.getDimension(R.styleable.ShapeView_strokeDashGap, 0);
        shapeType = a.getInt(R.styleable.ShapeView_shapeType, -1);
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke(stroke_Width, stroke_Color, strokeDashWidth, strokeDashGap);

        if (!isEnabled()) {
            gradientDrawable.setColor(enableColor);
            setTextColor(enableTextColor);
        } else {
            gradientDrawable.setColor(solidColor);
            setTextColor(normalTextColor);
        }
        if (shapeType != -1) {
            gradientDrawable.setShape(shapeType);
        }
        if (shapeType != GradientDrawable.OVAL) {
            if (cornesRadius != 0) {
                gradientDrawable.setCornerRadius(cornesRadius);
            } else {
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                gradientDrawable.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
            }
        }
        setBackgroundDrawable(gradientDrawable);

    }

    /**
     * 设置绘制方式类型
     * 0：长方形
     * 1：椭圆
     * 2：线
     * 3：圆环
     */
    public ShapeView setShapeType(int shapeType) {
        gradientDrawable.setShape(shapeType);
        return this;
    }

    /**
     * 设置圆角
     *
     * @param radius
     */
    public ShapeView setRadius(float radius) {
        gradientDrawable.setCornerRadius(radius);
        return this;
    }

    public ShapeView setRadius(float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
        gradientDrawable.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
        return this;
    }

    /**
     * 设置颜色
     */
    public ShapeView setSolidColor(@ColorInt int solidColor) {
        gradientDrawable.setColor(solidColor);
        return this;
    }

    /**
     * 设置边线颜色
     */
    public ShapeView setStrokeColor(@ColorInt int strokeColor) {
        stroke_Width = strokeColor;
        gradientDrawable.setStroke(stroke_Width, stroke_Color, strokeDashWidth, strokeDashGap);
        return this;
    }


    /**
     * 进行绘制
     */
    public void show() {
        setBackgroundDrawable(gradientDrawable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (gradientDrawable != null) {
            if (enabled) {
                gradientDrawable.setColor(solidColor);
                setTextColor(normalTextColor);
            } else {
                gradientDrawable.setColor(enableColor);
                setTextColor(enableTextColor);
            }
            setBackgroundDrawable(gradientDrawable);
        }

        super.setEnabled(enabled);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (touchColor != Color.TRANSPARENT) {
                    gradientDrawable.setColor(touchColor);
                    setBackgroundDrawable(gradientDrawable);
                    setTextColor(touchTextColor);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (touchColor != Color.TRANSPARENT) {
                    gradientDrawable.setColor(solidColor);
                    setBackgroundDrawable(gradientDrawable);
                    setTextColor(normalTextColor);
                }
            }
        }
        return super.onTouchEvent(event);
    }

}

