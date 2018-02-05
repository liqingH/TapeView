package com.lqh.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.lqh.library.listener.OnScaleChangeListener;
import com.lqh.library.plotter.BackgroundPlotter;
import com.lqh.library.plotter.IndicatorPlotter;
import com.lqh.library.plotter.ScaleTextPlotter;
import com.lqh.library.plotter.ScaleLinePlotter;

/**
 * 类描述：
 * 创建人：lqh
 * 创建时间：2018/1/31
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version v1.0
 */

public class TapeView extends View {

    private Axis axis;

    private float min;          //标尺最小值
    private float max;          //标尺最大值
    private float current;      //标尺当前值
    private int interval;       //长刻度线间的间隔数
    private float shortSpace;   //短刻度线间距
    private float textSize;     //刻度值字体大小

    private int scaleLineColor;      //刻度线颜色
    private int scaleTextColor;      //刻度值颜色
    private int indicatorColor;      //指示器颜色
    private int backgroundColor;     //标尺背景色

    private BackgroundPlotter backgroundPlotter;    //背景绘图类
    private ScaleLinePlotter scaleLinePlotter;      //刻度线绘图类
    private IndicatorPlotter indicatorPlotter;      //指示器绘图类
    private ScaleTextPlotter scaleTextPlotter;      //刻度值绘图类

    private OverScroller scroller;
    private GestureDetector gestureDetector;
    private OnScaleChangeListener onScaleChangeListener;

    private boolean isFling = false;    //是否在惯性滑动
    private float diff;                 //当前值与起始值的差值

    public TapeView(Context context) {
        this(context, null);
    }

    public TapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        init();
    }

    private void init() {
        diff = current - min;
        scroller = new OverScroller(getContext());
        scroller.setFriction(0.1f);
        axis = new Axis.Builder().min(min)
                .max(max)
                .shortSpace(shortSpace)
                .interval(interval)
                .build();

        backgroundPlotter = new BackgroundPlotter(backgroundColor);

        indicatorPlotter = new IndicatorPlotter(axis);
        indicatorPlotter.setColor(indicatorColor);

        scaleLinePlotter = new ScaleLinePlotter(axis);
        scaleLinePlotter.setColor(scaleLineColor);

        scaleTextPlotter = new ScaleTextPlotter(axis);
        scaleTextPlotter.setColor(scaleTextColor);
        scaleTextPlotter.setTextSize(textSize);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scrollXFlag = getScrollX();
                scroller.startScroll(getScrollX(), getScrollY(), (int) distanceX, 0, 0);
                postInvalidate();
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //惯性滑动
                scroller.fling(scroller.getCurrX(), scroller.getCurrY(), -(int) velocityX, 0,
                        axis.minPixel, axis.maxPixel, 0, 0);
                isFling = true;

                return true;
            }
        });
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TapeView);
        min = ta.getFloat(R.styleable.TapeView_min, 0);
        max = ta.getFloat(R.styleable.TapeView_max, 100);
        current = ta.getFloat(R.styleable.TapeView_current, 0);
        interval = ta.getInt(R.styleable.TapeView_interval, 10);
        shortSpace = ta.getFloat(R.styleable.TapeView_space, 1);
        textSize = ta.getDimension(R.styleable.TapeView_textSize, 10);
        scaleLineColor = ta.getColor(R.styleable.TapeView_scaleLineColor, Color.WHITE);
        scaleTextColor = ta.getColor(R.styleable.TapeView_scaleTextColor, Color.WHITE);
        indicatorColor = ta.getColor(R.styleable.TapeView_indicatorColor, Color.WHITE);
        backgroundColor = ta.getColor(R.styleable.TapeView_backgroundColor, Color.GREEN);
        ta.recycle();
    }

    @Override
    public void computeScroll() {
        //配合scroller进行滑动
        int oldx = scrollXFlag;
        if (scroller.computeScrollOffset()) {
            axis.moveToPixel(scroller.getCurrX() - oldx);
            scrollXFlag = scroller.getCurrX();
            scrollBy(axis.scrollPixel, 0);
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backgroundPlotter.draw(canvas); //画背景
        scaleLinePlotter.draw(canvas);  //画刻度线
        scaleTextPlotter.draw(canvas);  //画刻度值
        indicatorPlotter.draw(canvas);  //画指示器
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        current = Utils.nearestScalePosition(axis.shortStart, l, axis.distance, axis.shortSpace);
        if (onScaleChangeListener != null) {
            onScaleChangeListener.onScaleChange(current);
        }
        postDelayed(adjustFlingScroll, 100);

    }

    private Runnable adjustFlingScroll = new Runnable() {
        @Override
        public void run() {
            if (scroller.isFinished() && isFling) {
                isFling = false;
                adjustScroll();
            }
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = w - getPaddingRight();
        int bottom = h - getPaddingBottom();
        Rect bound = new Rect(left, top, right, bottom);
        axis.setBound(bound);

        //移动到current的位置
        int pixel = Utils.nearestCurrentPixel(diff, axis.distance, axis.shortSpace);
        scrollXFlag = getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), pixel, 0);
        postInvalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {

            adjustScroll();

            return true;
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(ev))
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int scrollXFlag;

    /**
     * 修正滑动位置，使每次滑动之后指示器都正好落在刻度线上
     */
    private void adjustScroll() {
        int pixel = Utils.nearestAdjustPixel(getScrollX(), axis.distance, axis.getBorderOffset());
        scrollXFlag = getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), pixel - indicatorPlotter.getCurrentX(), 0, 500);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(widthMeasureSpec, measure(heightMeasureSpec));
    }

    /**
     * 测量高度 设置了最小高度为80dp
     *
     * @param measureSpec MeasuredDimension
     * @return 控件高度
     */
    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) Utils.dpToPixel(80);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    /* ================================== 对外API =================================== */

    /**
     * 滑动监听，刻度变化
     *
     * @param onScaleChangeListener {@link OnScaleChangeListener}
     */
    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.onScaleChangeListener = onScaleChangeListener;
    }

    /**
     * 获取当前值
     *
     * @return 当前值
     */
    public float getCurrent() {
        return current;
    }

    /**
     * 设置当前值
     *
     * @param current 当前值
     */
    public void setCurrent(float current) {
        if (current < min || current > max || !Utils.nearestAccuracy(axis.min, current, axis.shortSpace)) {
            return;
        }
        diff = current - Math.max(min, this.current);
        this.current = current;

        int pixel = Utils.nearestCurrentPixel(diff, axis.distance, axis.shortSpace);
        scrollXFlag = getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), pixel, 0, 500);
        postInvalidate();
    }

    /**
     * 设置最小值
     *
     * @param min 最小值
     */
    public void setMin(float min) {
        this.min = min;
        axis.setMin(min);
        axis.calculate();
        postInvalidate();
    }

    /**
     * 设置最大值
     *
     * @param max 最大值
     */
    public void setMax(float max) {
        this.max = max;
        axis.setMax(max);
        axis.calculate();
        postInvalidate();
    }

    /**
     * 设置长刻度线间的间隔数
     *
     * @param interval 间隔数
     */
    public void setInterval(int interval) {
        this.interval = interval;
        axis.setInterval(interval);
        postInvalidate();
    }

    /**
     * 设置短刻度线之间的间距值
     *
     * @param shortSpace 间距值
     */
    public void setShortSpace(float shortSpace) {
        this.shortSpace = shortSpace;
        axis.setShortSpace(shortSpace);
        axis.calculate();
        postInvalidate();
    }

    /**
     * 设置刻度值大小
     *
     * @param textSize 字体尺寸
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        scaleTextPlotter.setTextSize(textSize);
        postInvalidate();
    }

    /**
     * 设置刻度线颜色
     *
     * @param scaleLineColor 刻度线颜色
     */
    public void setScaleLineColor(int scaleLineColor) {
        this.scaleLineColor = scaleLineColor;
        scaleLinePlotter.setColor(scaleLineColor);
        postInvalidate();
    }

    /**
     * 设置刻度值颜色
     *
     * @param scaleTextColor 刻度值颜色
     */
    public void setScaleTextColor(int scaleTextColor) {
        this.scaleTextColor = scaleTextColor;
        scaleTextPlotter.setColor(scaleTextColor);
        postInvalidate();
    }

    /**
     * 设置指示器颜色
     *
     * @param indicatorColor 指示器颜色
     */
    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        indicatorPlotter.setColor(indicatorColor);
        postInvalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPlotter.setColor(backgroundColor);
        postInvalidate();
    }

    public float getMin() {
        return axis.min;
    }

    public float getMax() {
        return axis.max;
    }

    public int getInterval() {
        return axis.interval;
    }

    public float getShortSpace() {
        return axis.shortSpace;
    }

    public float getLongSpace() {
        return axis.longSpace;
    }
}
