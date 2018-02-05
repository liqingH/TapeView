package com.lqh.library;

import android.graphics.Rect;

/**
 * 类描述：轴相关的数据类
 * 创建人：lqh
 * 创建时间：2018/2/1
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version v1.0
 */

public class Axis {

    public static final int MIN_BORDER = 0;
    public static final int NOT_BORDER = 1;
    public static final int MAX_BORDER = 2;

    public float min;
    public float max;

    public float longStart;
    public float shortStart;

    public float longSpace;
    public float shortSpace;
    public int interval;

    public int longLineCount;
    public int shortLineCount;

    public int minPixel;
    public int maxPixel;
    public int currMinPixel;
    public int currMaxPixel;
    public int scrollPixel;

    public int distance;
    private Rect bound;

    private Axis() {
        this.bound = new Rect();
        this.distance = (int) Utils.dpToPixel(10);
    }

    public void calculate() {
        if (min % longSpace == 0) {
            longStart = min;
        } else {
            longStart = Utils.nearestStartValue(min, longSpace);
        }
        longLineCount = (int) ((max - longStart) / longSpace) + 1;

        if (min % shortSpace == 0) {
            shortStart = min;
        } else {
            shortStart = Utils.nearestStartValue(min, shortSpace);
        }
        shortLineCount = (int) ((max - shortStart) / shortSpace) + 1;

        maxPixel = (shortLineCount - 1) * distance + bound.width();

    }

    float lastMin = 0;
    float lastMax = 0;

    public int moveToPixel(float pixel) {
        if (pixel == 0) {
            scrollPixel = 0;
            return NOT_BORDER;
        }

        if (currMaxPixel == maxPixel && pixel > 0) {
            scrollPixel = 0;
            return MAX_BORDER;
        }

        if (currMinPixel == minPixel && pixel < 0) {
            scrollPixel = 0;
            return MIN_BORDER;
        }

        if (currMinPixel + pixel < minPixel) {
            scrollPixel = minPixel - currMinPixel;
            currMaxPixel = currMaxPixel + scrollPixel;
            currMinPixel = minPixel;
        } else if (currMaxPixel + pixel > maxPixel) {
            scrollPixel = maxPixel - currMaxPixel;
            currMinPixel = currMinPixel + scrollPixel;
            currMaxPixel = maxPixel;
        } else {
            scrollPixel = (int) pixel;
            currMinPixel = currMinPixel + scrollPixel;
            currMaxPixel = currMaxPixel + scrollPixel;
        }

        lastMin = currMinPixel;
        lastMax = currMaxPixel;
        return NOT_BORDER;
    }

    public static class Builder {
        private Axis axis;

        public Builder() {
            axis = new Axis();
        }

        public Builder min(float min) {
            axis.setMin(min);
            return this;
        }

        public Builder max(float max) {
            axis.setMax(max);
            return this;
        }

        public Builder interval(int interval) {
            axis.setInterval(interval);
            return this;
        }

        public Builder shortSpace(float shortSpace) {
            axis.setShortSpace(shortSpace);
            return this;
        }

        public Axis build() {
            axis.calculate();
            return axis;
        }
    }

    public void setBound(Rect bound) {
        this.bound.set(bound);
        currMinPixel = bound.left;
        currMaxPixel = bound.right;
        minPixel = bound.left;
        maxPixel = (shortLineCount - 1) * distance + bound.width();
    }

    public Rect getBound() {
        return bound;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getBorderOffset() {
        return bound.left + bound.width() * 0.5f;
    }

    public float getLongStart() {
        return longStart;
    }

    public float getShortStart() {
        return shortStart;
    }

    public int getDistance() {
        return distance;
    }

    public void setShortSpace(float shortSpace) {
        this.shortSpace = shortSpace;
        longSpace = interval * shortSpace;
    }

    public int getLongLineCount() {
        return longLineCount;
    }

    public int getShortLineCount() {
        return shortLineCount;
    }

    public void setInterval(int interval) {
        this.interval = interval;
        longSpace = interval * shortSpace;
    }
}
