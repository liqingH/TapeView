package com.lqh.library.plotter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.lqh.library.Axis;
import com.lqh.library.Utils;

/**
 * 类描述：指示器绘图器
 * 创建人：lqh
 * 创建时间：2018/2/1
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version v1.0
 */

public class IndicatorPlotter implements Plotter {

    private Axis axis;
    private Paint paint;
    private int currentX;

    public IndicatorPlotter(Axis axis) {
        this.axis = axis;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        Path path = new Path();
        currentX = (int) ((axis.currMaxPixel + axis.currMinPixel) / 2);
        path.moveTo(currentX, Utils.dpToPixel(8));
        path.lineTo(currentX - Utils.dpToPixel(8), 0);
        path.lineTo(currentX + Utils.dpToPixel(8), 0);
        path.close();
        canvas.drawPath(path, paint);
    }

    public int getCurrentX() {
        return currentX;
    }
}
