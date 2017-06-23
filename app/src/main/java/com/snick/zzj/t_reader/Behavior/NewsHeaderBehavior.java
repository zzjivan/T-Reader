package com.snick.zzj.t_reader.Behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

/**
 * Created by zzj on 17-6-22.
 */

public class NewsHeaderBehavior extends CoordinatorLayout.Behavior {
    private Context context;

    //一定要实现这个构造函数，否则会报Could not inflate Behavior subclass xxx 异常，可查看源码
    public NewsHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
}
