package com.snick.zzj.t_reader.model.impl;

import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.beans.NewsThemes;
import com.snick.zzj.t_reader.beans.ThemeNews;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zzj on 17-2-6.
 */

public interface DailyNewsRequestService {

    @GET("{when}")
    Observable<DailyNews> getDailyNews(@Path("when") String when);
    @GET("{url}") //没有数据就写 /  或者 .  不能空着
    Observable<NewsThemes> getNewsThemes(@Path("url") String url);
    @GET("{url}") //没有数据就写 /  或者 .  不能空着
    Observable<ThemeNews> getThemeNews(@Path("url") String url);
}
