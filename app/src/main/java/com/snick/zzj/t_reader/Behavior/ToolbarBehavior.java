package com.snick.zzj.t_reader.Behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.snick.zzj.t_reader.R;

/**
 * Created by zzj on 17-6-22.
 */

public class ToolbarBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {
    private Context context;

    //一定要实现这个构造函数，否则会报Could not inflate Behavior subclass xxx 异常，可查看源码
    public ToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        return dependency != null && dependency.getId() == R.id.tbsContent;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        Log.d("zjzhu","toolbar:"+dependency.getTranslationY());
        if(dependency.getTranslationY() <= 0 && dependency.getTranslationY() >= -getAlphaChangeOffsetRange()) {
            child.setAlpha((float) (Math.abs(dependency.getTranslationY()) * 255 / getAlphaChangeOffsetRange()));
        }
        return false;
    }

    /**
     * Header移动real_height距离过程中，toolbar是改变透明度
     * @return
     */
    private int getAlphaChangeOffsetRange() {
        return context.getResources().getDimensionPixelOffset(R.dimen.news_header_real_height);
    }
}
