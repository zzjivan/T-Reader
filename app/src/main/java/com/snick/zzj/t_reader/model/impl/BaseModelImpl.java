package com.snick.zzj.t_reader.model.impl;

import android.graphics.Bitmap;

import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.model.BaseModel;
import com.snick.zzj.t_reader.utils.SourceUrl;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zzj on 17-2-6.
 */

public class BaseModelImpl implements BaseModel {

    @Override
    public void refreshViews(Observer<DailyNews> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SourceUrl.News)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加RxJava支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        DailyNewsRequestService dailyNewsRequestService = retrofit.create(DailyNewsRequestService.class);
        dailyNewsRequestService.getDailyNews("latest")
                .subscribeOn(Schedulers.io())//不加这个会出现android.os.NetworkOnMainThreadException
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getOldNews(String date, Observer<DailyNews> observer) {

    }

    @Override
    public void getNewsContent(String newsId, Observer<DailyNews> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SourceUrl.News)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加RxJava支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        DailyNewsRequestService dailyNewsRequestService = retrofit.create(DailyNewsRequestService.class);
        dailyNewsRequestService.getDailyNews(newsId)
                .subscribeOn(Schedulers.io())//不加这个会出现android.os.NetworkOnMainThreadException
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}