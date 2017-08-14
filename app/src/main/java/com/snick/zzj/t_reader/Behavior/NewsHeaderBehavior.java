package com.snick.zzj.t_reader.Behavior;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.views.fragment.NewsContentFragment;

/**
 * Created by zzj on 17-6-22.
 */

public class NewsHeaderBehavior extends CoordinatorLayout.Behavior<FrameLayout> {
    private Context context;
    boolean flag = true;
    boolean is_top = true;
    NestedScrollView nestedScrollView;

    private OverScroller overScroller;
    private FlingRunnable flingRunnable;

    //一定要实现这个构造函数，否则会报Could not inflate Behavior subclass xxx 异常，可查看源码
    public NewsHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi
                * 0.84f;
        overScroller = new OverScroller(context);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        if(flag) {
            flag = false;
            nestedScrollView = (NestedScrollView) coordinatorLayout.findViewById(R.id.content_webview);
            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == 0)
                        is_top = true;
                    else is_top = false;
                }
            });
        }
        return true;
    }

    //手指上滑，dy>0
    //手指下滑, dy<0
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, int dx, int dy, int[] consumed) {
        float halfOfDis = dy / 4.0f;
        //到最低处了
        if(child.getTranslationY() - halfOfDis > 0) {
            child.setTranslationY(0);
        }
        //到最高处了
        else if(child.getTranslationY() - halfOfDis < - getHeaderOffsetRange()){
            child.setTranslationY(-getHeaderOffsetRange());
        }
        //可以嵌套滚动的区域
        else {
            /**
             * 当toolbar和header完全消失，content占据整个屏幕时，“下滑”  并不一定就要嵌套滚动，也可能是content内容浏览的滚动
             * 我们判断content中内容是否到顶部了 来决定是否嵌套滚动
             */
            if(dy < 0 && !is_top) {
                consumed[1] = 0;
            } else {
                child.setTranslationY(child.getTranslationY() - halfOfDis);
                consumed[1] = dy;//consumed数组中必须和dy同符号，否则dispatchNestedPreScroll会返回false
            }
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, float velocityX, float velocityY) {
        if(canScroll(child, 0)) {
            //TODO:这里怎么做到child能依据velocityY来进行滑动？
            handleFling(coordinatorLayout, child ,velocityY);
            return true;
        } else {
            return false;
        }
    }

    private boolean canScroll(View child, int distance){
        if((child.getTranslationY()+distance > - getHeaderOffsetRange() && child.getTranslationY()+distance < 0) ||
                (child.getTranslationY()+distance == -getHeaderOffsetRange() && distance < 0))
            return true;
        else return false;
    }

    private int getHeaderOffsetRange() {
        return context.getResources().getDimensionPixelOffset(R.dimen.header_motion);
    }

    /**
     * http://blog.csdn.net/tyro_smallnew/article/details/54613947
     * 从这篇博客粘过来的通过速率计算滑动距离的方法
     */

    private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    // Fling friction
    private static float mFlingFriction = ViewConfiguration.getScrollFriction();
    private static float mPhysicalCoeff;
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));

    private double getSplineDeceleration(float velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    //通过初始速度获取最终滑动距离
    private double getSplineFlingDistance(float velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }

    //获取滑动的时间
    /* Returns the duration, expressed in milliseconds */
    private int getSplineFlingDuration(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return (int) (1000.0 * Math.exp(l / decelMinusOne));
    }

    private void handleFling(CoordinatorLayout parent, final View child, float volecityY) {
        if (flingRunnable != null) {
            child.removeCallbacks(flingRunnable);
            flingRunnable = null;
        }
        flingRunnable = new FlingRunnable(parent, child, volecityY);
        flingRunnable.scrollTo();
    }

    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;
        private final float volecityY;

        FlingRunnable(CoordinatorLayout parent, View layout, float volecity) {
            mParent = parent;
            mLayout = layout;
            this.volecityY = volecity;
        }

        public void scrollTo() {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            if(volecityY > 0) {
                overScroller.startScroll(0, (int) curTranslationY, 0, -getHeaderOffsetRange() - (int) curTranslationY, 800);
            } else {
                overScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, 800);
            }
            start();
        }

        private void start() {
            if (overScroller.computeScrollOffset()) {
                ViewCompat.postOnAnimation(mLayout, flingRunnable);
            }
        }

        @Override
        public void run() {
            if (mLayout != null && overScroller != null) {
                if (overScroller.computeScrollOffset()) {
                    ViewCompat.setTranslationY(mLayout, overScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                }
            }
        }
    }
}
