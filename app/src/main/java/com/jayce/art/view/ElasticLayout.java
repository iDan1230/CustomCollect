package com.jayce.art.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

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
开发：杨智丹 on 2017/10/23 15:12
邮箱：yangzhidan@tederen.com
备注：上下回弹
*/
public class ElasticLayout extends LinearLayout {

    private Context mContext;

    private Scroller mScroller;

    private VelocityTracker velocityTracker;

    private int mDownX;

    private int mDownY;

    private int mMoveX;

    private int mMoveY;

    private int mLastX;

    private int mLastY;

    private int maxMoveY;


    public ElasticLayout(Context context) {
        this(context, null);
    }

    public ElasticLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        mScroller = new Scroller(mContext);
        velocityTracker = VelocityTracker.obtain();
    }

    /**
     * 第二步:测量所有子View的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取子View的个数
        int childCount = getChildCount();
        maxMoveY = 0;
        /**
         * 循环每个子View
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView != null) {
                //此方法会触发childView的onMeasure方法去测量宽高
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            }

            maxMoveY += childView.getMeasuredHeight();

            Log.e("MSG", " childHeight: " + childView.getHeight() + "  " + maxMoveY);
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getRawX();
                mDownY = (int) ev.getRawY();
                mLastX = mDownX;
                mLastY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) (ev.getRawX() - mDownX);
                mMoveY = (int) (ev.getRawY() - mDownY);
                if (Math.abs(mMoveX) < Math.abs(mMoveY)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("msg", "down");
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getRawY();

                smoothScrollBy((int) (mLastY - event.getRawY()));
                Log.e("msg", "height: " +getHeight() + "  ScrollY：" + getScrollY() + " maxMoveY： " + maxMoveY);
                mLastY = moveY;
                break;
            case MotionEvent.ACTION_UP:

                velocityTracker.computeCurrentVelocity(1000);

                Log.e("msg", "ScrollY: " + getScrollY());
                if (getScrollY() < 0) {
                    mScroller.startScroll(0, getScrollY(), 0, getScrollY() * -1, 500);

                } else if (getScrollY() > maxMoveY - getHeight()) {

                    mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - maxMoveY + getHeight()), 500);
                    Log.e("msg", "startScroll");
                } else {
                    int yVelicoty = (int) velocityTracker.getYVelocity();
                    mScroller.fling(0, getScrollY(), 0, yVelicoty * -1, 0, 0, 0, maxMoveY - getHeight());
                    Log.e("MSG", "FLING");
                }
                invalidate();
                break;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void smoothScrollBy(int scrollY) {

        if (getScrollY() < 0 || getScrollY() > maxMoveY - getHeight()){

            scrollY*=0.6;
        }

        scrollBy(0, scrollY);
    }
}
