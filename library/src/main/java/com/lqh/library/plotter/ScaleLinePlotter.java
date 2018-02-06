package com.lqh.library.plotter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.lqh.library.Axis;
import com.lqh.library.Utils;

/**
 * 类描述：刻度线的绘图器
 * 创建人：lqh
 * 创建时间：2018/2/1
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version v1.0
 */

public class ScaleLinePlotter implements Plotter {

    private Axis axis;
    private Paint paint;

    public ScaleLinePlotter(Axis axis) {
        this.axis = axis;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setStrokeWidth(5);
        for (int i = 0; i < axis.getLongLineCount(); i++) {
            drawLongLine(canvas, i);
        }

        paint.setStrokeWidth(3);
        for (int i = 0; i < axis.getShortLineCount(); i++) {
            drawShortLine(canvas, i);
        }
    }

    private void drawShortLine(Canvas canvas, int position) {
        float x = axis.distance * position + axis.getBorderOffset();
        if (x < axis.currMinPixel - axis.distance || x > axis.currMaxPixel + axis.distance) {
            return;
        }
        canvas.drawLine(x, 0, x, Utils.dpToPixel(20), paint);
    }

    private void drawLongLine(Canvas canvas, int position) {
        float x = (axis.longStart - axis.shortStart) / axis.shortSpace * axis.distance
                + axis.distance * position * axis.interval + axis.getBorderOffset();
        if (x < axis.currMinPixel - axis.distance || x > axis.currMaxPixel + axis.distance) {
            return;
        }
        canvas.drawLine(x, 0, x, Utils.dpToPixel(40), paint);
    }

}
