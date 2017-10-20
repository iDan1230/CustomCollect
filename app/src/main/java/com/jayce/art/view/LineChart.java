package com.jayce.art.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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
public class LineChart extends View {
    private Context mContext;

    private Scroller mScroller;

    private VelocityTracker velocityTracker;

    private int mTouchSlop;

    private Paint linePaint;

    private Paint circlePaint;

    private Paint gridPaint;

    private Paint fillPaint;

    private Path linePath;

    private List<Float> lineDatas;

    private boolean isBezier = true;

    private boolean isFill = true;

    private int radius;

    private int specX;

    //Y方向的最大值
    private int maxY = 50;
    //底部标签显示数量默认显示个数
    private int lineCountX = 7;
    //有方向分几层
    private int lineCountY = 5;


    //当设置成WRAP_CONTENT时默认的宽高
    private int defaultSize = 200;

    //滑动倍数
    private float scrollMultiple = 1;

    private int mDownX;
    private int mLastMoveX;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
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

        circlePaint = new Paint();
        circlePaint.setDither(true);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.YELLOW);
        circlePaint.setStrokeWidth(dip2px(2));
        circlePaint.setStyle(Paint.Style.STROKE);


        gridPaint = new Paint();
        gridPaint.setDither(true);
        gridPaint.setAntiAlias(true);
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStrokeWidth(dip2px(1));
        gridPaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint();
        fillPaint.setDither(true);
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(Color.GREEN);
        fillPaint.setStrokeWidth(dip2px(1));


        linePath = new Path();

        radius = (int) dip2px(3);

        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

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
        specX = (int) ((getWidth() - getPaddingLeft() - getPaddingRight() - linePaint.getStrokeWidth()) / (lineCountX - 1));
        drawGrid(canvas);
        drawBezierLine(canvas);


    }

    /**
     * 绘制线
     *
     * @param canvas
     */
    private void drawBezierLine(Canvas canvas) {
//        linePath.close();
        linePath.reset();
        //path绘制线条移动到起始点
        linePath.moveTo(getPaddingLeft() + gridPaint.getStrokeWidth() / 2,
                getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * lineDatas.get(0) + gridPaint.getStrokeWidth() / 2);


        for (int i = 1; i < lineDatas.size(); i++) {
            //是否绘制贝塞尔曲线
            if (isBezier) {
                Point controlPoint2 = new Point((int) (getPaddingLeft() + i * specX + gridPaint.getStrokeWidth() / 2 - specX / 2), (int) (getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * lineDatas.get(i) + gridPaint.getStrokeWidth()));
                Point controlPoint1 = new Point((int) (getPaddingLeft() + i * specX + gridPaint.getStrokeWidth() / 2 - specX / 2), (int) (getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * lineDatas.get(i - 1) + gridPaint.getStrokeWidth()));
                linePath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, getPaddingLeft() + i * specX + gridPaint.getStrokeWidth() / 2,
                        getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * lineDatas.get(i) + gridPaint.getStrokeWidth() / 2);
            } else {
                linePath.lineTo(getPaddingLeft() + i * specX + gridPaint.getStrokeWidth() / 2,
                        getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * lineDatas.get(i) + gridPaint.getStrokeWidth() / 2);


            }
        }

        //是否绘制实心
        if (isFill) {
            linePaint.setShader(new LinearGradient(0, 0, 0, getHeight(), linePaint.getColor(),
                    Color.WHITE, Shader.TileMode.MIRROR));
            fillPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), fillPaint.getColor(),
                    Color.WHITE, Shader.TileMode.MIRROR));

            linePath.lineTo(getPaddingLeft() + (lineDatas.size() - 1) * specX + gridPaint.getStrokeWidth() / 2,
                    getHeight() - getPaddingBottom() - gridPaint.getStrokeWidth() - fillPaint.getStrokeWidth());
            linePath.lineTo(getPaddingLeft() + gridPaint.getStrokeWidth() / 2,
                    getHeight() - getPaddingBottom() - gridPaint.getStrokeWidth() - fillPaint.getStrokeWidth());
            linePath.close();

            canvas.drawPath(linePath, fillPaint);
        }
        canvas.drawPath(linePath, linePaint);


        for (int i = 0; i < lineDatas.size(); i++) {
            //绘制圆
            canvas.drawCircle(getPaddingLeft() + i * specX + gridPaint.getStrokeWidth() / 2,
                    getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) * lineDatas.get(i) + gridPaint.getStrokeWidth() / 2,
                    radius, circlePaint);

        }

    }

    /**
     * 背景网格
     *
     * @param canvas
     */
    private void drawGrid(Canvas canvas) {
        int specY = (int) ((getHeight() - getPaddingTop() - getPaddingBottom() - gridPaint.getStrokeWidth()) / (lineCountY - 1));
        //上一步绘制rect时已经绘制了最上面和最下面的线-->绘制横线
        for (int i = 0; i < lineCountY; i++) {
            canvas.drawLine(getPaddingLeft() + gridPaint.getStrokeWidth() / 2, getPaddingTop() + i * specY + gridPaint.getStrokeWidth() / 2, specX * (lineDatas.size() - 1) + getPaddingLeft(), getPaddingTop() + i * specY + gridPaint.getStrokeWidth() / 2, gridPaint);
        }
        //绘制数显
        for (int i = 0; i < lineDatas.size(); i++) {
            canvas.drawLine(getPaddingLeft() + i * specX + gridPaint.getStrokeWidth() / 2, getPaddingTop() + gridPaint.getStrokeWidth() / 2, getPaddingLeft() + i * specX + gridPaint.getStrokeWidth() / 2, getHeight() - getPaddingBottom() - gridPaint.getStrokeWidth() / 2, gridPaint);
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
                mScroller.fling(getScrollX(), 0, (int) xVelocity * -1, 0, 0, (lineDatas.size() - 1) * specX - getWidth(), 0, 0);
//                else
//                    mScroller.startScroll(getScrollX(), 0, 0, 0, 1000);
                invalidate();
                break;
        }


        return true;
    }

    public LineChart setBezier(boolean bezier) {
        isBezier = bezier;
        return this;
    }

    public LineChart setFill(boolean fill) {
        isFill = fill;
        return this;
    }

    public LineChart setDatas(List<Float> lineDatas) {
        this.lineDatas = lineDatas;
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
}
