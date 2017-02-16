package com.snick.zzj.t_reader.model;

import com.snick.zzj.t_reader.beans.DailyNews;

import rx.Observer;

/**
 * Created by zzj on 17-2-6.
 */

public interface BaseModel {

    void refreshViews(Observer<DailyNews> observer);
}
