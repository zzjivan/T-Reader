package com.snick.zzj.t_reader.Behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.snick.zzj.t_reader.R;

/**
 * Created by zzj on 17-6-22.
 */

public class NewsHeaderBehavior extends CoordinatorLayout.Behavior<FrameLayout> {
    private Context context;

    //一定要实现这个构造函数，否则会报Could not inflate Behavior subclass xxx 异常，可查看源码
    public NewsHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    //手指上滑，dy>0
    //手指下滑, dy<0
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, int dx, int dy, int[] consumed) {
        float halfOfDis = dy / 4.0f;
        if(child.getTranslationY() - halfOfDis > 0) {
            child.setTranslationY(0);
        } else if(child.getTranslationY() - halfOfDis < - getHeaderOffsetRange()){
            child.setTranslationY(-getHeaderOffsetRange());
        } else {
            child.setTranslationY(child.getTranslationY() - halfOfDis);
            consumed[1] = dy;//consumed数组中必须和dy同符号，否则dispatchNestedPreScroll会返回false
        }
    }

    private int getHeaderOffsetRange() {
        return context.getResources().getDimensionPixelOffset(R.dimen.header_motion);
    }
}
