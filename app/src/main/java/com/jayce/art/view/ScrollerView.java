package com.jayce.art.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
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
开发：杨智丹 on 2017/10/19 13:52
邮箱：yangzhidan@tederen.com
备注：练习自定义一个类似ViewPager的控件
注意点:
onMeasure: 计算子View的宽高，循环调用measureChild()为子View设置宽高;
onLayout: 循环为子View设置位置，调用子View的layout方法
onInterceptTouchEvent: 拦截事件当滑动距离小于一个临界值时拦截事件，不让onTouchEvent执行
onTouchEvent: 处理触摸事件判断是否需要滑动和滑动到上一个还是下一个
Scroller: 处理滑动，statScroll()方法.
computeScroll():平滑逻辑
*/
public class ScrollerView extends ViewGroup {

    private Context mContext;
    private Scroller mScroller;
    private VelocityTracker velocityTracker;

    //触发滑动事件的滑动距离临界值
    private int mTouchSlop;
    //按下为位置
    private int mDownX;
    //上一次移动后的位置
    private int mLastMoveX;
    //左右两班的边界点
    private int leftBorder, rightBorder;

    public ScrollerView(Context context) {
        this(context, null);
    }

    public ScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 第一步:初始化工具实例
     */
    private void init() {
        //创建Scroller的实例
        mScroller = new Scroller(mContext);
        //获取临界值
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

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

        /**
         * 循环每个子View
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView != null) {
                //此方法会触发childView的onMeasure方法去测量宽高
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    /**
     * 第三步:设置子View的位置
     * <p>
     * <p>
     * 注意：
     * layout方法是控制View自己本身的位置
     * onLayout是控制 子View在View中的位置
     * <p>
     * 一般情况下载onLayout中获取控件的实际宽高时用getWidth和getMeasuredWidth获取的值是相同的
     * 除非在layout方法中重新设置了位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //触发每个子View的layout方法确定位置
            childView.layout(i * getWidth(), 0, (i + 1) * getMeasuredWidth(), getHeight());
        }

        if (childCount > 0) {
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(childCount - 1).getRight();
        }
    }

    /**
     * 第四步:事件拦截
     * 仿照ViewPager所以只需要根据x方向来确定是否拦截事件（判断移动距离是否大于了mTouchSlop）
     * <p>
     * getRawX():相对于父控件的位置
     * getX():相对于控件本身左上角的位置
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("TAG", "onInterceptTouchEvent");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getRawX();
                mLastMoveX = mDownX;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) Math.abs(ev.getRawX() - mDownX);
                //移动距离大于临界点拦截事件-->触发onTouchEvent方法
                if (moveX > mTouchSlop) {
                    return true;
                }
                break;
        }


        return super.onInterceptTouchEvent(ev);
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
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
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
                scrollBy(scrollX, 0);
                mLastMoveX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                velocityTracker.clear();
                if (Math.abs(xVelocity) >= 50) {
                    targetIndex = xVelocity < 0 ? targetIndex + 1 : targetIndex - 1;
                }
                if (targetIndex >= getChildCount()) {
                    targetIndex = getChildCount() - 1;
                }
                if (targetIndex < 0) {
                    targetIndex = 0;
                }
                int dx = targetIndex * getWidth() - getScrollX();
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mScroller.startScroll(getScrollX(), 0, dx, 0, 1000);
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
}
