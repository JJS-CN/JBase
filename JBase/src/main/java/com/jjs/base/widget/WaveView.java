package com.jjs.base.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * 本页：绘制波浪图，绘制内容在onDraw中，通过属性动画循环调用onDraw进行动画效果播放
 * 通过setAnim控制动画来控制具体显示效果，activity监听onresume和onpause设置动画启动和停止，高度变化由动画执行监听不停赋值变动
 * <p>
 * 由于实现方式的原因，2层绘制于同一个画布，由同一个动画控制，导致死板，需要灵性的变动需要自己重写
 */
public class WaveView extends View {
    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public enum ShapeType {
        CIRCLE,
        SQUARE
    }

    // if true, the shader will display the wave
    private boolean mShowWave;

    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewPaint;
    // paint to draw border
    private Paint mBorderPaint;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = 0f;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * 基本无用，动画设置无效
     * 设置固定将没有动态效果
     * 死循环，只能更改动画效果去改变
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * 设置波浪位置（高度：view百分比 取值：0~1f，默认0.001）
     * 动画时间，大于0 时将执行渐变动画
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * 波峰值（波浪的起伏高度），对应view高度百分比，默认0.05
     * 通过动画控制时：设置无效
     * 注意：amplitude+waterLvel高度大于1时，会超出控件显示区域
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    /**
     * 设置显示波浪图像
     */
    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    /**
     * 绘制圆环
     *
     * @param width 圆环宽度
     * @param color 圆环颜色
     */
    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);

        invalidate();
    }

    /**
     * 设置双层波浪颜色（ 2层颜色会以三原色原则，相交部分互相干涉形成新颜色）
     *
     * @param behindWaveColor 下层颜色
     * @param frontWaveColor  上层颜色
     */
    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
        mBehindWaveColor = behindWaveColor;
        mFrontWaveColor = frontWaveColor;

        if (getWidth() > 0 && getHeight() > 0) {
            // need to recreate shader when color changed
            mWaveShader = null;
            createShader();
            invalidate();
        }
    }

    /**
     * 设置绘图形状（默认CIRCLE圆形）
     * SQUARE矩形
     *
     * @param shapeType
     */
    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mBehindWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mFrontWaveColor);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }
        //添加这段代码可新增图层
       /* final int wave2Shift2 = (int) (mDefaultWaveLength / 2);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift2) % endX], beginX, endY, wavePaint);
        }*/

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // modify paint shader according to mShowWave state
        if (mShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0,
                    mDefaultWaterLevel);
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(
                    mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            switch (mShapeType) {
                case CIRCLE:
                    if (borderWidth > 0) {
                        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                                (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    }
                    float radius = getWidth() / 2f - borderWidth;
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);
                    break;
                case SQUARE:
                    if (borderWidth > 0) {
                        canvas.drawRect(
                                borderWidth / 2f,
                                borderWidth / 2f,
                                getWidth() - borderWidth / 2f - 0.5f,
                                getHeight() - borderWidth / 2f - 0.5f,
                                mBorderPaint);
                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth,
                            getHeight() - borderWidth, mViewPaint);
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }
    }

    /***
     * 动画部分(单独控制)
     */
    ObjectAnimator waveShiftAnim;
    ObjectAnimator waterLevelAnim;
    ObjectAnimator amplitudeAnim;

    /**
     * 控制水平动画，传入时间控制速度（毫秒）
     */
    public void setAnimShift(int duration) {
        this.ShiftDuration = duration;
        animShift();
    }

    int ShiftDuration = 1500;//横向移动一屏幕时的速率（毫秒）

    //水平移动
    private void animShift() {
        //必须是1f的倍数
        waveShiftAnim = ObjectAnimator.ofFloat(
                WaveView.this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(ShiftDuration);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        waveShiftAnim.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限
        // waveShiftAnim.start();
    }

    /**
     * 设置波浪高度，由动画控制上移，同时设置动画时间（毫秒）
     * 高度由动画监听设置，重设时使用现有高度
     */

    public void setAnimLevel(float toLevel, int duration) {
        this.toLevel = toLevel;
        this.LevelDuration = duration;
        animLevel();
    }

    float toLevel;//设置波浪位置
    int LevelDuration = 1500;//从底部到顶部所需时间（毫秒）

    private void animLevel() {
        if ((toLevel - mWaterLevelRatio) * 2000 < LevelDuration) {
            LevelDuration = (int) ((toLevel - mWaterLevelRatio) * 2000);
        }
        waterLevelAnim = ObjectAnimator.ofFloat(
                WaveView.this, "waterLevelRatio", mWaterLevelRatio, toLevel);
        waterLevelAnim.setDuration(LevelDuration);

        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        waterLevelAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("11111", animation.getAnimatedValue() + "");
                mWaterLevelRatio = (float) animation.getAnimatedValue();
            }
        });
        // waterLevelAnim.start();
    }

    /**
     * 设置波浪高度变化值
     * min最低(推荐不超过0.01)，min最高(推荐不超过0.03)，波峰变化周期（毫秒）
     */
    public void setAnimLitude(float minLitude, float maxLitude, int litudeDuration) {
        this.minLitude = minLitude;
        this.maxLitude = maxLitude;
        this.LitudeDuration = litudeDuration;
        animLitude();
    }

    float minLitude = 0.005f;
    float maxLitude = 0.01f;
    int LitudeDuration = 5000;//波峰高低变化（毫秒）

    private void animLitude() {
        amplitudeAnim = ObjectAnimator.ofFloat(
                WaveView.this, "amplitudeRatio", minLitude, maxLitude);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(LitudeDuration);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        //   amplitudeAnim.start();
    }

    public void animStart() {
        if (amplitudeAnim != null) {
            amplitudeAnim.start();
        }
        if (waterLevelAnim != null) {
            animLevel();
            waterLevelAnim.start();
        }
        if (waveShiftAnim != null) {
            waveShiftAnim.start();
        } else {
            animShift();
            waveShiftAnim.start();
        }
    }

    public void animCancel() {
        if (amplitudeAnim != null) {
            amplitudeAnim.end();
        }
        if (waterLevelAnim != null) {
            waterLevelAnim.end();
        }
        if (waveShiftAnim != null) {
            waveShiftAnim.end();
        }
    }


}
