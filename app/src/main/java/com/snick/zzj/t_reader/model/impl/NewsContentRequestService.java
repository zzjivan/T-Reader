package com.snick.zzj.t_reader.model.impl;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.beans.NewsExtraInfo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zzj on 17-2-24.
 */

public interface NewsContentRequestService {
    @GET("{newsId}")
    Observable<NewsContent> getNewsContent(@Path("newsId") String newsId);

    @GET("{newsId}")
    Observable<NewsExtraInfo> getNewsExtraInfo(@Path("newsId") String newsId);
}
