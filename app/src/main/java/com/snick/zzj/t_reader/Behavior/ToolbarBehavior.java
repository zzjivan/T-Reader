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
        return dependency != null && dependency.getId() == R.id.content_webview;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        if(dependency.getTranslationY() <= getContentInitOffset() && dependency.getTranslationY() >= getToolbarHeight()) {
            child.setAlpha((float) (1 - Math.abs(getContentInitOffset() - dependency.getTranslationY()) / (getContentInitOffset()-getToolbarHeight())));
        }
        return false;
    }

    private int getContentInitOffset() {
        return context.getResources().getDimensionPixelOffset(R.dimen.news_content_init_offset);
    }

    private int getToolbarHeight() {
        return context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
    }
}
