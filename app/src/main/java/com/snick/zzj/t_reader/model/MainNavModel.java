package com.snick.zzj.t_reader.model;

import com.snick.zzj.t_reader.beans.NewsThemes;

import rx.Observer;

/**
 * Created by zzj on 17-8-3.
 */

public interface MainNavModel {

    void loadThemes(Observer<NewsThemes> observer);
}
