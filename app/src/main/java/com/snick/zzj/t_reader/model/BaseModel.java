package com.snick.zzj.t_reader.model;

import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.beans.NewsContent;

import rx.Observer;

/**
 * Created by zzj on 17-2-6.
 */

public interface BaseModel {

    //获取某一天的新闻列表，date：20170704，则获取7月3日的列表
    void refreshViews(Observer<DailyNews> observer, String type, String date);

    void getNewsContent(String newsId, Observer<DailyNews> observer);
}
