package com.snick.zzj.t_reader.model.impl;

import com.snick.zzj.t_reader.beans.DailyNews;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zzj on 17-2-6.
 */

public interface DailyNewsRequestService {

    @GET("{when}")
    Observable<DailyNews> getDailyNews(@Path("when") String when);
}
