package com.snick.zzj.t_reader.model;

import com.snick.zzj.t_reader.beans.ThemeNews;

import rx.Observer;

/**
 * Created by zzj on 17-8-7.
 */

public interface SingleThemeModel {

    void loadSingleTheme(Observer<ThemeNews> observer, String id);
}
