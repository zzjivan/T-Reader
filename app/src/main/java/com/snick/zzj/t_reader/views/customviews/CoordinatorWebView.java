package com.snick.zzj.t_reader.views.customviews;

import com.tencent.smtt.sdk.WebView;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Map;

/**
 * Created by zzj on 2017/7/26.
 */

public class CoordinatorWebView extends WebView implements NestedScrollingChild {

    public CoordinatorWebView(Context context) {
        super(context);
    }

    public CoordinatorWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public CoordinatorWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public CoordinatorWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public CoordinatorWebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private int[] mConsumed = new int[2];

    private int[] mOffset = new int[2];

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("zjzhu","hehe");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下事件调用startNestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动事件调用startNestedScroll
                dispatchNestedPreScroll(0,20,mConsumed,mOffset);
                // 输出一下偏移
                Log.d("zjzhu", "offset--x:" + mOffset[0] + ",offset--y:" + mOffset[1]);
                dispatchNestedScroll(50,50,50,50,mOffset);

                break;
            case MotionEvent.ACTION_UP:
                // 弹起事件调用startNestedScroll
                stopNestedScroll();
                break;
            default:
                break;
        }
        return true;
    }
}
