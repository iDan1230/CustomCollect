package com.jayce.art.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jayce.art.R;

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
开发：杨智丹 on 2017/10/19 10:26
邮箱：yangzhidan@tederen.com
备注：
*/
public class CircleView extends ImageView {

    private Context mContext;
    private Paint mPaint;


        private int color;
//    private Drawable color;

    //当设置成WRAP_CONTENT时默认的宽高
    private int defaultSize = 200;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray t = mContext.obtainStyledAttributes(attrs, R.styleable.CircleView);

        color = t.getColor(R.styleable.CircleView_color,Color.WHITE);

        //释放资源
        t.recycle();

        init();
    }

    private void init() {
        defaultSize = dip2px(mContext, 200);
        mPaint = new Paint();
        //画笔颜色
        mPaint.setColor(color);
        //画笔样式（空心）
        mPaint.setStyle(Paint.Style.STROKE);
        //画笔宽度
        mPaint.setStrokeWidth(dip2px(mContext, 2));
        //抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);

    }


    /**
     * 测量View宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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

        //圆心（x，y）
        int widthCenter = (getWidth() - getPaddingRight() - getPaddingLeft()) / 2 + getPaddingLeft();
        int heightCenter = (getHeight() - getPaddingBottom() - getPaddingTop()) / 2 + getPaddingTop();
        //半径
        int radius = (int) (Math.min((getWidth() - getPaddingRight() - getPaddingLeft()), (getHeight() - getPaddingBottom() - getPaddingTop()) - mPaint.getStrokeWidth()) / 2);
        //绘制圆
        canvas.drawCircle(widthCenter, heightCenter, radius, mPaint);


    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
