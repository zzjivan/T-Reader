package com.snick.zzj.t_reader.model.impl;

import android.util.Log;

import com.snick.zzj.t_reader.beans.ThemeNews;
import com.snick.zzj.t_reader.model.SingleThemeModel;
import com.snick.zzj.t_reader.utils.SourceUrl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zzj on 17-8-7.
 */

public class SingleThemeModelImpl implements SingleThemeModel {
    private OkHttpClient okHttpClient;

    public SingleThemeModelImpl() {
        //设置超时时间
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true).build();
    }

    @Override
    public void loadSingleTheme(Observer<ThemeNews> observer, String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SourceUrl.newsThemeRoot+SourceUrl.newsThemeItemTag+"/")
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加RxJava支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        DailyNewsRequestService dailyNewsRequestService = retrofit.create(DailyNewsRequestService.class);
        dailyNewsRequestService.getThemeNews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
