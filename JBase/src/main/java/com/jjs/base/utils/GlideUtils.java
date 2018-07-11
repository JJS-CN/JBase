package com.jjs.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;

/**
 * 说明：基于glide常用操作进行简单封装
 * 暴露as(@NonNull RequestOptions options) 方法进行自定义
 * Created by aa on 2017/6/23.
 */

public class GlideUtils {


    public static void load(Context mContext, Object path, ImageView imageView) {
        Glide.with(mContext).load(path).into(imageView);
    }

    public static Builder loadBuild(@NonNull Context mContext, @NonNull Object path, @NonNull ImageView imageView) {
        return new Builder(mContext, path, imageView);
    }

    public static class Builder {
        private Context mContext;
        private Object mPath;
        private ImageView mImageView;
        private RequestOptions mOptions;

        private Builder(Context mContext, Object path, ImageView imageView) {
            this.mContext = mContext;
            this.mPath = path;
            this.mImageView = imageView;
            mOptions = new RequestOptions();
        }

        public Builder setPlaceholderId(int placeId) {
            mOptions.placeholder(placeId);
            return this;
        }

        public void asSimple() {
            as();
        }

        public void asBlur() {
            mOptions.bitmapTransform(new GlideBlurformation());
            as();
        }

        public void asCircle() {
            mOptions.optionalCircleCrop();
            as();
        }

        public void asRound(int roundDp) {
            mOptions.bitmapTransform(new RoundTransform(roundDp));
            as();
        }

        public void as(@NonNull RequestOptions options) {
            mOptions = options;
            as();
        }

        private void as() {
            Glide.with(mContext).load(mPath).apply(mOptions).into(mImageView);
        }
    }


    /**
     * 处理弧角图片
     */
    public static class RoundTransform extends BitmapTransformation {
        int radius;

        /**
         * 构造函数 默认圆角半径 4dp
         */
        public RoundTransform() {
            this(4);
        }

        /**
         * 构造函数
         *
         * @param dp 圆角半径
         */
        public RoundTransform(int dp) {
            radius = dp;
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            if (toTransform == null)
                return null;

            int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
            int x = (toTransform.getWidth() - size) / 2;
            int y = (toTransform.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, result.getWidth(), result.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }

    /**
     * 处理图片为高斯模糊
     */
    public static class GlideBlurformation extends BitmapTransformation {
        private float scale;
        private float radius;

        public GlideBlurformation() {
            this.scale = 0.2f;
            this.radius = 25;
        }

        public GlideBlurformation(@FloatRange(from = 0, to = 1, fromInclusive = false) float scale,
                                  @FloatRange(from = 0, to = 25, fromInclusive = false) float radius) {
            this.scale = scale;
            this.radius = radius;
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return ImageUtils.fastBlur(toTransform, scale, radius);
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }
}
