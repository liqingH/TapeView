package com.lqh.library.plotter;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 类描述：绘制背景
 * 创建人：lqh
 * 创建时间：2018/1/31
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version v1.0
 */

public class BackgroundPlotter implements Plotter {

    private int color;

    public BackgroundPlotter(int color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(color);
    }
}
