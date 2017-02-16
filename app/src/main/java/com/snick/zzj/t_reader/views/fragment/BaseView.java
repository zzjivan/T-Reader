package com.snick.zzj.t_reader.views.fragment;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.snick.zzj.t_reader.beans.DailyNews;

/**
 * Created by zzj on 17-2-6.
 */

public interface BaseView {

    void refreshViews(DailyNews dailyNews);
}
