package com.jjs.base.utils;

import java.util.Random;

/**
 * 本页：随机数工具类
 * Created by jjs on 2017-03-31.
 * Email:994462623@qq.com
 */

public class RandomUtil {
    /**
     * 获取min和max之间的随机数Int,当没有中间值时，取最小值
     */
    public static int getRandomInt(int min, int max) {
        if (min > max) {
            max = min;

        }
        Random r = new Random();
        int random = r.nextInt(max - min);
        return random + min + 1 >= max ? random + min : random + min + 1;
    }

    /**
     * 获取0到1之间的随机double值,包括0和1
     * @param length 想要保留小数点后几位
     */
    public static double getRandomDouble(int length) {
        StringBuffer format = new StringBuffer("#.");
        for (int i = 0; i < length; i++) {
            format.append("0");
        }
        String d = new java.text.DecimalFormat(format.toString()).format(new Random().nextDouble());
        return Double.parseDouble(d);
    }

    /**
     * 获取随机验证码，位数不够自动补0。
     * @param length 想要生成的随机数长度
     */
    public static String getRandomSMSCode(int length) {
        StringBuffer sb = new StringBuffer("1");
        for (int i = 0; i < length; i++) {
            sb.append(0);
        }
        int random = getRandomInt(0, Integer.parseInt(sb.toString()));
        return String.format("%0" + String.valueOf(Integer.parseInt(sb.toString()) - 1).length() + "d", random);
    }
}
