package com.snick.zzj.t_reader.Behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.snick.zzj.t_reader.R;

/**
 * Created by zzj on 17-6-22.
 */


public class NewsContentBehavior extends CoordinatorLayout.Behavior<NestedScrollView> {

    private Context context;

    //一定要实现这个构造函数，否则会报Could not inflate Behavior subclass xxx 异常，可查看源码
    public NewsContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override

    public boolean layoutDependsOn(CoordinatorLayout parent, NestedScrollView child, View dependency) {
        return dependency != null && dependency.getId() == R.id.anchor;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, NestedScrollView child, View dependency) {

        if(dependency.getTranslationY() == 0) {
            child.setTranslationY(getInitOffset());
        } else {
            child.setTranslationY(getInitOffset() + dependency.getTranslationY() * getInitOffset() / getHeaderOffset());
        }
        return false;
    }

    private int getInitOffset() {
        return context.getResources().getDimensionPixelOffset(R.dimen.news_content_init_offset);
    }

    private int getHeaderOffset() {
        return context.getResources().getDimensionPixelOffset(R.dimen.news_header_offset_range);
    }
}
