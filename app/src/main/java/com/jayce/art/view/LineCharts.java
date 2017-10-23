package com.jayce.art.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.List;

/*
                  _ooOoo_
                 o8888888o
                 88" . "88
                 (| -_- |)
                 O\  =  /O
              ____/`---'\____
            .'  \\|     |//  `.
           /  \\|||  :  |||//  \
          /  _||||| -:- |||||-  \
          |   | \\\  -  /// |   |
          | \_|  ''\---/''  |   |
          \  .-\__  `-`  ___/-. /
        ___`. .'  /--.--\  `. . __
     ."" '<  `.___\_<|>_/___.'  >'"".
    | | :  `- \`.;`\ _ /`;.`/ - ` : | |
    \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                  `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            佛祖保佑       永无BUG
开发：杨智丹 on 2017/10/19 11:36
邮箱：yangzhidan@tederen.com
备注：
*/
public class LineCharts extends View {
    private Context mContext;

    private Scroller mScroller;

    private VelocityTracker velocityTracker;

    private int mTouchSlop;

    /**
     * 曲线画笔
     */
    private Paint linePaint;

    /**
     * 坐标点画笔
     */
    private Paint circlePaint;

    /**
     * 背景网格画笔
     */
    private Paint gridPaint;
    /**
     * 实心画笔
     */
    private Paint fillPaint;
    /**
     * 文字画笔
     */
    private Paint textPaint;

    /**
     * 曲线路径（不封口）
     */
    private Path linePath;
    /**
     * 实心区域路径线（需要封口）
     */
    private Path fillPath;

    private List<Float> lineDatas;

    private List<ChartData> datas;
    /**
     * 是否显示贝塞尔曲线，当isShowLine设置为false时无效
     */
    private boolean isBezier = true;
    /**
     * 是否绘制实心，当isShowLine设置为false时无效
     */
    private boolean isFill = true;
    /**
     * 是否显示线，默认显示。false时 isFill 无效
     */
    private boolean isChartline = true;
    /**
     * 是否绘制grid线
     */
    private boolean isGridLine = true;
    /**
     * 是否绘制竖线
     */
    private boolean isVerticalLine = true;
    /**
     * 是否绘制横线
     */
    private boolean isHorizontalLine = true;
    /**
     * 原点半径
     */
    private int radius;
    /**
     * 横向间距
     */
    private int specX;
    /**
     * 纵向间距
     */
    private int specY;

    //Y方向的最大值
    private int maxY = 50;
    //底部标签显示数量默认显示个数
    private int lineCountX = 7;
    //有方向分几层
    private int lineCountY = 5;
    //label高度
    private int labelHeight = 5;


    //当设置成WRAP_CONTENT时默认的宽高
    private int defaultSize = 200;

    //滑动倍数
    private float scrollMultiple = 1;

    private int mDownX;
    private int mLastMoveX;

    public LineCharts(Context context) {
        this(context, null);
    }

    public LineCharts(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineCharts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        init();
    }

    private void init() {
        mScroller = new Scroller(mContext);

        velocityTracker = VelocityTracker.obtain();

        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();


        linePaint = new Paint();
        linePaint.setDither(true);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(dip2px(2));
        linePaint.setStyle(Paint.Style.STROKE);


        textPaint = new Paint();
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dip2px(10));
        textPaint.setStyle(Paint.Style.FILL);

        circlePaint = new Paint();
        circlePaint.setDither(true);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.YELLOW);
        circlePaint.setStrokeWidth(dip2px(2));
        circlePaint.setStyle(Paint.Style.FILL);


        gridPaint = new Paint();
        gridPaint.setDither(true);
        gridPaint.setAntiAlias(true);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(dip2px(1f));
        gridPaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint();
        fillPaint.setDither(true);
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.GREEN);
        fillPaint.setStrokeWidth(dip2px(1));


        linePath = new Path();
        fillPath = new Path();

        radius = (int) dip2px(5);

        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        Log.e("msg", getPaddingBottom() + " getPaddingBottom");

        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), 50);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取模式
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        //宽高都是设置的WRAP_CONTENT
        if (widthSpec == MeasureSpec.AT_MOST && heightSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultSize, defaultSize);
        } else if (widthSpec == MeasureSpec.AT_MOST && heightSpec == MeasureSpec.EXACTLY) {
            //宽设置为WRAP_CONTENT,高度为精准模式（EXACTLY）
            setMeasuredDimension(defaultSize, heightSize);
        } else if (heightSpec == MeasureSpec.AT_MOST) {
            //高设置WRAP_CONTENT,宽度为精准模式
            setMeasuredDimension(widthSize, defaultSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (datas == null)
            return;
        specX = (int) ((getWidth() - getPaddingLeft() - getPaddingRight()) / (lineCountX - 1));
        specY = (int) ((getHeight() - getPaddingTop() - getPaddingBottom()) / (lineCountY - 1));
        drawGrid(canvas);
        drawBezierLine(canvas);


    }

    /**
     * 绘制线
     *
     * @param canvas
     */
    private void drawBezierLine(Canvas canvas) {

        //是否显示线
        if (isChartline) {
            linePath.reset();
            //path绘制线条移动到起始点
            linePath.moveTo(getPaddingLeft(),
                    getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(0).getSize());
            if (isFill)
                fillPath.moveTo(getPaddingLeft() + gridPaint.getStrokeWidth() / 2 + fillPaint.getStrokeWidth() / 2,
                        getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(0).getSize() + linePaint.getStrokeWidth() / 2);
            for (int i = 1; i < datas.size(); i++) {
                //是否绘制贝塞尔曲线
                if (isBezier) {
                    Point controlPoint2 = new Point(getPaddingLeft() + i * specX - specX / 2, (int) (getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i).getSize()));
                    Point controlPoint1 = new Point(getPaddingLeft() + i * specX - specX / 2, (int) (getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i - 1).getSize()));
                    linePath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, getPaddingLeft() + i * specX,
                            getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i).getSize());
                    if (isFill) {
                        Point controlPointfill2 = new Point(getPaddingLeft() + i * specX - specX / 2, (int) (getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i).getSize()));
                        Point controlPointfill1 = new Point(getPaddingLeft() + i * specX - specX / 2, (int) (getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i - 1).getSize()));

                        fillPath.cubicTo(controlPointfill1.x, controlPointfill1.y, controlPointfill2.x, controlPointfill2.y, getPaddingLeft() + i * specX,
                                getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i).getSize());

                    }
                } else {
                    linePath.lineTo(getPaddingLeft() + i * specX,
                            getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i).getSize() - gridPaint.getStrokeWidth() / 2);


                }
            }

            //是否绘制实心
            if (isFill) {
                fillPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), fillPaint.getColor(),
                        Color.WHITE, Shader.TileMode.MIRROR));

                fillPath.lineTo(getPaddingLeft() + (datas.size() - 1) * specX - gridPaint.getStrokeWidth() / 2,
                        getHeight() - getPaddingBottom() - gridPaint.getStrokeWidth());

                fillPath.lineTo(getPaddingLeft() + gridPaint.getStrokeWidth() / 2,
                        getHeight() - getPaddingBottom() - gridPaint.getStrokeWidth());
                fillPath.close();

                canvas.drawPath(fillPath, fillPaint);
                fillPath.reset();
            }
            canvas.drawPath(linePath, linePaint);

        }
        /**
         *  这里绘制了两种颜色不一样的圆，可以只要一个
         */
        for (int i = 0; i < datas.size(); i++) {
            //绘制圆
            circlePaint.setColor(Color.YELLOW);
            canvas.drawCircle(getPaddingLeft() + i * specX,
                    getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i).getSize(),
                    radius, circlePaint);
            circlePaint.setColor(Color.RED);
            //绘制圆
            canvas.drawCircle(getPaddingLeft() + i * specX,
                    getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * datas.get(i).getSize(),
                    radius * 0.5f, circlePaint);

        }

    }

    /**
     * 背景网格
     *
     * @param canvas
     */
    private void drawGrid(Canvas canvas) {

        //横向线
        if (isGridLine && isHorizontalLine) {
            for (int i = 0; i < lineCountY - 1; i++) {
                canvas.drawLine(getPaddingLeft() - gridPaint.getStrokeWidth() / 2, getPaddingTop() + i * specY, ((datas.size() < 7 ? 7 : datas.size()) - 1) * specX + getPaddingLeft() + gridPaint.getStrokeWidth() / 2, getPaddingTop() + i * specY, gridPaint);
            }
        }
        //底线
        canvas.drawLine(getPaddingLeft() - gridPaint.getStrokeWidth() / 2, getPaddingTop() + (lineCountY - 1) * specY + gridPaint.getStrokeWidth() / 2, ((datas.size() < 7 ? 7 : datas.size()) - 1) * specX + getPaddingLeft() + gridPaint.getStrokeWidth() / 2, getPaddingTop() + (lineCountY - 1) * specY + gridPaint.getStrokeWidth() / 2, gridPaint);


        if (isGridLine && isVerticalLine) {
            for (int i = 0; i < (datas.size() < 7 ? 7 : datas.size()); i++) {
                //纵向线
                canvas.drawLine(getPaddingLeft() + i * specX, getPaddingTop(), getPaddingLeft() + i * specX, getHeight() - getPaddingBottom() + labelHeight, gridPaint);

                //绘制文字
                Rect textRect = new Rect(getPaddingLeft() + i * specX - specX / 2, getHeight() - getPaddingBottom() + labelHeight, getPaddingLeft() + i * specX + specX / 2, getHeight() + labelHeight);
                Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
                int baseline = (textRect.bottom + textRect.top - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
                if (i < datas.size())
                    canvas.drawText(datas.get(i).getLabelBottom(), textRect.centerX(), baseline, textPaint);
            }
        }


    }

    /**
     * 第五步: 事件处理
     * 1、先在move方法中调用scrollTo移动起来
     * <p>
     * <p>
     * 注意点getScrollX的含义:相当于是手指按下触摸时的点距离屏幕左上角的距离
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null)
            velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getRawX();
                mLastMoveX = mDownX;
                //如果还没有结束上衣的滑动
                if (!mScroller.isFinished()) {
                    //停职动画滑动
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //当前触摸的位置
                int moveX = (int) event.getRawX();
                //滑动的距离
                int scrollX = mLastMoveX - moveX;
                smoothScrollBy(scrollX, 0);

                mLastMoveX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                velocityTracker.clear();
                Log.e("TAG", xVelocity + "  :速率");
//                if (Math.abs(xVelocity) > 2000)
                if (datas != null)
                    mScroller.fling(getScrollX(), 0, (int) xVelocity * -1, 0, (int) (0 - gridPaint.getStrokeWidth() / 2), (int) ((datas.size() - 7) * specX + gridPaint.getStrokeWidth() * 2), 0, 0);
//                else
//                    mScroller.startScroll(getScrollX(), 0, 0, 0, 1000);
                invalidate();
                break;
        }


        return true;
    }

    public LineCharts setBezier(boolean bezier) {
        isBezier = bezier;
        return this;
    }

    public LineCharts setFill(boolean fill) {
        isFill = fill;
        return this;
    }

    public LineCharts setChartLine(boolean showline) {
        isChartline = showline;
        return this;
    }

    public LineCharts setDatas(List<ChartData> chartDatas) {
        this.datas = chartDatas;
        return this;
    }

    public LineCharts setGridLine(boolean gridLine) {
        isGridLine = gridLine;
        return this;
    }

    public LineCharts setVerticalLine(boolean verticalLine) {
        isVerticalLine = verticalLine;
        return this;
    }

    public LineCharts setHorizontalLine(boolean horizontalLine) {
        isHorizontalLine = horizontalLine;
        return this;
    }

    private void smoothScrollBy(int scrollX, int scrollY) {
        if (mLastMoveX > getWidth() / 2 / 3 * 2) {
            scrollMultiple = 2;
        } else if (mLastMoveX > getWidth() / 2 / 3) {
            scrollMultiple = 1.5f;
        } else {
            scrollMultiple = 1;
        }

        scrollBy((int) (scrollX / scrollMultiple), 0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public float dip2px(float size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }


    public enum To {
        START, END
    }
}
