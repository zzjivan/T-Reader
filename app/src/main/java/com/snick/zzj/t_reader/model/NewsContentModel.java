package com.snick.zzj.t_reader.model;

import com.snick.zzj.t_reader.beans.NewsContent;

import rx.Observer;

/**
 * Created by zzj on 17-2-24.
 */

public interface NewsContentModel {

    void loadNewsContent(String newsId, Observer<NewsContent> observer);
}
