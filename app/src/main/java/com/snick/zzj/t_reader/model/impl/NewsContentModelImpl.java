package com.snick.zzj.t_reader.model.impl;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.beans.NewsExtraInfo;
import com.snick.zzj.t_reader.model.NewsContentModel;
import com.snick.zzj.t_reader.utils.SourceUrl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zzj on 17-2-24.
 */

public class NewsContentModelImpl implements NewsContentModel {

    private OkHttpClient okHttpClient;

    public NewsContentModelImpl() {
        //设置超时时间
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true).build();
    }

    @Override
    public void loadNewsContent(String newsId, Observer<NewsContent> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SourceUrl.News)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        NewsContentRequestService newsContentRequestService = retrofit.create(NewsContentRequestService.class);
        newsContentRequestService.getNewsContent(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadNewsExtraInfo(String newsId, Observer<NewsExtraInfo> observer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SourceUrl.extraInfo)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        NewsContentRequestService newsContentRequestService = retrofit.create(NewsContentRequestService.class);
        newsContentRequestService.getNewsExtraInfo(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
