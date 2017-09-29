package com.jjs.base.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * 说明：验证码工具，生成图片、返回字符串进行对比
 * 每次调用getBitmap()将重置参数，code取值同时改变
 * Created by aa on 2017/9/28.
 */

public class CodeImgUtils {
    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'};

    private static CodeImgUtils instance;

    private CodeImgUtils() {
    }

    /**
     * 正确的单例实现
     */
    public static CodeImgUtils getInstance() {
        if (instance == null) {
            synchronized (CodeImgUtils.class) {
                if (instance == null) {
                    instance = new CodeImgUtils();
                }
            }
        }
        return instance;
    }

    private static final int DEFAULT_CODE_LENGTH = 4;// 验证码的长度 这里是4位
    private static final int DEFAULT_FONT_SIZE = 60;// 字体大小
    private static final int DEFAULT_LINE_NUMBER = 3;// 多少条干扰线
    private static final int BASE_PADDING_LEFT = 20; // 左边距
    private static final int RANGE_PADDING_LEFT = 35;// 左边距范围值
    private static final int BASE_PADDING_TOP = 42;// 上边距
    private static final int RANGE_PADDING_TOP = 15;// 上边距范围值
    private static final int DEFAULT_WIDTH = 200;// 默认宽度.图片的总宽
    private static final int DEFAULT_HEIGHT = 70;// 默认高度.图片的总高
    private final int DEFAULT_COLOR = 0xdf;// 默认背景颜色值

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    private int base_padding_left = BASE_PADDING_LEFT;
    private int range_padding_left = RANGE_PADDING_LEFT;
    private int base_padding_top = BASE_PADDING_TOP;
    private int range_padding_top = RANGE_PADDING_TOP;

    private int codeLength = DEFAULT_CODE_LENGTH;
    private int line_number = DEFAULT_LINE_NUMBER;
    private int font_size = DEFAULT_FONT_SIZE;

    private String code;// 保存生成的验证码
    private int padding_left, padding_top;
    private Random random = new Random();

    private Bitmap createBitmap() {
        padding_left = 0;
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);
        code = createCode();
        c.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR));
        Paint paint = new Paint();
        paint.setTextSize(font_size);
        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }
        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }
        c.save(Canvas.ALL_SAVE_FLAG);// 保存
        c.restore();//
        return bp;
    }

    /**
     * 返回小写字符
     */
    public String getLowerCode() {
        return code.toLowerCase();
    }

    /**
     * 返回字符
     */
    public String getCode() {
        return code;
    }

    /**
     * 每次更新图片，将同步更新code
     * 所以需要每次先取图片，再取code
     */
    public Bitmap getBitmap() {
        return createBitmap();
    }

    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean()); // true为粗体，false为非粗体
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX); // float类型参数，负数表示右斜，整数左斜
    }

    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }

}
