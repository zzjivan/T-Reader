package com.snick.zzj.t_reader.model;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.beans.NewsExtraInfo;

import rx.Observer;

/**
 * Created by zzj on 17-2-24.
 */

public interface NewsContentModel {

    void loadNewsContent(String newsId, Observer<NewsContent> observer);
    void loadNewsExtraInfo(String newsId, Observer<NewsExtraInfo> observer);
}
