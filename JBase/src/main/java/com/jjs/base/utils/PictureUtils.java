package com.jjs.base.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 已更换为Luban进行压缩
 * 图片压缩处理方法，判断处理图片旋转问题，默认压缩宽度1080，只进行了图片尺寸的压缩
 * 步骤：拿到图片先判断旋转角度，如果旋转了就开始旋转处理，正常之后只进行图片尺寸的压缩，压缩至1080宽度
 * Created by jjs on 2016/9/5.
 */
public class PictureUtils {
    /**
     * 图片压缩方法
     *
     * @param inFile   设置输入图片路径。精确到图片类型.jpg
     * @param outFile  设置压缩后图片输出路径。精确到图片理性.jpg（为null时将以infile为目标，覆盖原图片）
     * @param maxWidth 设置图片最大宽度( 小于等于0 时，默认以1080为标准压缩)
     * @return 返回压缩后的图片路径
     */
    public static File scFile(File inFile, File outFile, float maxWidth) {
        if (maxWidth <= 0) {
            maxWidth = 1080;
        }
        if (outFile == null) {
            outFile = inFile;
        }
        /** 处理图片旋转问题 */
        rotateFile(inFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(inFile.getAbsolutePath(), options);
        float height = options.outHeight;
        float width = options.outWidth;
        Log.e("pictureUtils","输入参数：inFileSize:" + inFile.length() / 1024 + " kb---picWidth:" + width + "---picHeight:" + height);
        float scale = maxWidth / width;
        int scHeight = (int) (height * scale);
        int scWidth = (int) (width * scale);
        if (scale < 1) {
            options.outHeight = scHeight;
            options.outWidth = scWidth;
            options.inSampleSize = (int) (width / maxWidth) + 4;
        }

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(inFile.getAbsolutePath(), options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, scWidth, scHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BitmapFactory.Options x = new BitmapFactory.Options();
        x.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(outFile.getAbsolutePath(), x);
        Log.e("pictureUtils","输出参数：outFileSize:" + outFile.length() / 1024 + " kb---outWidth:" + x.outWidth + "---outHeight:" + x.outHeight);
        return outFile;
    }

    public static File rotateFile(File file) {
        /**
         * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
         */
        int degree = readPictureDegree(file.getAbsolutePath());
        /** 获得图片*/
        if (degree != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 10;  // 图片的大小设置为原来的十分之一（只是加载到内存，对文件无影响）
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            Bitmap outBmp = rotaingImageView(degree, bmp);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            outBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}