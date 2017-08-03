package com.snick.zzj.t_reader.model.impl;

import com.snick.zzj.t_reader.beans.NewsThemes;
import com.snick.zzj.t_reader.model.MainNavModel;
import com.snick.zzj.t_reader.utils.SourceUrl;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zzj on 17-8-3.
 */

public class MainNavModelImpl implements MainNavModel {

    @Override
    public void loadThemes(Observer<NewsThemes> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SourceUrl.newsThemeList)
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加RxJava支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
//        DailyNewsRequestService dailyNewsRequestService = retrofit.create(DailyNewsRequestService.class);
//        dailyNewsRequestService.getDailyNews(date)
//                .subscribeOn(Schedulers.io())//不加这个会出现android.os.NetworkOnMainThreadException
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
    }
}
