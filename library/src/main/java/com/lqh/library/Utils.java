package com.lqh.library;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.math.BigDecimal;

public class Utils {
    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    public static float pixelToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / metrics.density;
    }

    /**
     * 计算起始值，因为设定的起始值不一定正好是长刻度或者短刻度，通过这个方法计算出长刻度或者短刻度的起始值
     *
     * @param target      目标值
     * @param coefficient 系数，也就是每一格的值
     * @return
     */
    static float nearestStartValue(float target, float coefficient) {
        int i = 1;
        while (true) {
            if ((coefficient * i) < target) {
                i++;
            } else {
                return coefficient * i;
            }
        }
    }

    /**
     * 计算当前滑动的坐标最接近哪个值
     *
     * @param startValue  起始值
     * @param scrollPixel 滑动的像素
     * @param distance    每一小格的像素值
     * @param space       每一小格的值
     * @return 刻度值
     */
    static float nearestScalePosition(float startValue, int scrollPixel, int distance, float space) {
        BigDecimal startValueBD = new BigDecimal(String.valueOf(startValue));
        BigDecimal spaceBD = new BigDecimal(String.valueOf(space));
        int i = scrollPixel / distance;
        int f = scrollPixel % distance;
        if (f > distance / 2) {
            i++;
        }
        return spaceBD.multiply(new BigDecimal(String.valueOf(i))).add(startValueBD).floatValue();
    }

    /**
     * 计算修正值，即当前坐标距离最近的刻度值坐标的差值
     *
     * @param scrollPixel 滑动的像素
     * @param distance    每一小格的像素值
     * @param offset      补偿值，即屏幕宽度的一半
     * @return 修正值
     */
    static int nearestAdjustPixel(int scrollPixel, int distance, float offset) {
        int i = scrollPixel / distance;
        float f = scrollPixel % distance;
        if (f > distance / 2) {
            i++;
        }

        return (int) (i * distance + offset);
    }

    /**
     * 计算当前值所在的坐标像素值
     *
     * @param diff     当前值与起始值的差值
     * @param distance 每一小格的像素值
     * @param space    每一小格的值
     * @return 坐标像素值
     */
    static int nearestCurrentPixel(float diff, int distance, float space) {
        BigDecimal diffBD = new BigDecimal(String.valueOf(diff));
        BigDecimal spaceBD = new BigDecimal(String.valueOf(space));
        BigDecimal distanceBD = new BigDecimal(String.valueOf(distance));

        return diffBD.divide(spaceBD, BigDecimal.ROUND_UNNECESSARY).multiply(distanceBD).intValue();
    }

    /**
     * 用于设置current时计算值是否是刻度值
     *
     * @param min    最小值
     * @param target 当前值/目标值
     * @param space  每一小格的值
     * @return 是否是刻度值
     */
    static boolean nearestAccuracy(float min, float target, float space) {
        BigDecimal minBD = new BigDecimal(String.valueOf(min));
        BigDecimal targetBD = new BigDecimal(String.valueOf(target));
        BigDecimal spaceBD = new BigDecimal(String.valueOf(space));
        return targetBD.subtract(minBD).divideAndRemainder(spaceBD)[1].floatValue() == 0;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
