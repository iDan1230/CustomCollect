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
public class RefreshLayout extends LinearLayout {

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

    private int hideHoder;

    private RefreshHeadLayout refreshHeadLayout;

    private OnRefreshListener onRefreshListener;


    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
                maxMoveY += childView.getMeasuredHeight();

                if (i == 0) {
                    hideHoder = childView.getHeight();
                }
            }
        }
        scrollTo(0, hideHoder);
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
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getRawY();
                smoothScrollBy((int) (mLastY - event.getRawY()));
                mLastY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                Log.e("msg", "ScrollY: " + getScrollY());
                //拖动到头部的四分之三时触发 刷新
                if (getScrollY() < hideHoder / 4) {
                    moveHideBegin();
                } else if (getScrollY() < hideHoder) {
                    moveHideEnd();
                } else if (getScrollY() > maxMoveY - getHeight()) {
                    moveHideFooter();
                } else {
                    int yVelicoty = (int) velocityTracker.getYVelocity();
                    mScroller.fling(0, getScrollY(), 0, yVelicoty * -1, 0, 0, hideHoder, maxMoveY - getHeight());
                }
                invalidate();
                break;
        }
        return true;
    }


    /**
     * 隐藏的头部
     */
    public void moveHideEnd() {
        mScroller.startScroll(0, getScrollY(), 0, getScrollY() * -1 + hideHoder, 500);
        invalidate();
    }

    /**
     * 显示头部
     */
    public void moveHideBegin() {
        mScroller.startScroll(0, getScrollY(), 0, getScrollY() * -1, 500);
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveHideEnd();
                }
            }, 2000);
        }
        invalidate();
    }

    /**
     * 回弹到底部
     */
    public void moveHideFooter() {

        mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - maxMoveY + getHeight()), 500);

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void smoothScrollBy(int scrollY) {

        if (getScrollY() < 0 || getScrollY() > maxMoveY - getHeight()) {

            scrollY *= 0.5;
        }

        scrollBy(0, scrollY);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener {

        void onRefresh();

        void onLoadmore();
    }
}
