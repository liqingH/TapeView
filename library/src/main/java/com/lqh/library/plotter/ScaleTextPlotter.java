package com.lqh.library.plotter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.lqh.library.Axis;
import com.lqh.library.Utils;

/**
 * 类描述：
 * 创建人：lqh
 * 创建时间：2018/2/1
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version v1.0
 */

public class ScaleTextPlotter implements Plotter {

    private Axis axis;
    private Rect bound;
    private Paint paint;

    public ScaleTextPlotter(Axis axis) {
        this.axis = axis;
        this.bound = axis.getBound();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setTextSize(float textSize){
        paint.setTextSize(textSize);
    }

    @Override
    public void draw(Canvas canvas) {
        float y = bound.bottom - Utils.dpToPixel(20) - (paint.ascent() + paint.descent()) / 2;
        for (int i = 0; i < axis.getLongLineCount(); i++) {
            String text = textTransform(axis.longStart + axis.longSpace * i);
            float x = (axis.longStart - axis.shortStart) / axis.shortSpace * axis.getDistance()
                    + i * axis.getDistance() * axis.interval
                    + axis.getBorderOffset() - paint.measureText(text) / 2;

            if (x < axis.currMinPixel - axis.distance * axis.interval) {
                continue;
            } else if (x > axis.currMaxPixel + axis.distance * axis.interval) {
                return;
            }
            canvas.drawText(text, x, y, paint);
        }
    }

    private String textTransform(float position) {
        return Utils.subZeroAndDot(String.valueOf(position));
    }
}
