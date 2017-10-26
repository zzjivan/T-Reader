package com.snick.zzj.t_reader.model.impl;


import android.content.Context;

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

/**主页Model
 * Created by zzj on 17-2-6.
 */

public class BaseModelImpl extends RootModelImpl implements BaseModel {

    public BaseModelImpl(Context context) {
        super(context);
    }

    //获取某一天的新闻列表，date：20170704，则获取7月3日的列表
    @Override
    public void refreshViews(Observer<DailyNews> observer, String type, String date) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(type)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加RxJava支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        DailyNewsRequestService dailyNewsRequestService = retrofit.create(DailyNewsRequestService.class);
        dailyNewsRequestService.getDailyNews(date)
                .retryWhen(new RetryWhenHandler(5))
                .subscribeOn(Schedulers.io())//不加这个会出现android.os.NetworkOnMainThreadException
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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
                .client(okHttpClient)
                .build();
        DailyNewsRequestService dailyNewsRequestService = retrofit.create(DailyNewsRequestService.class);
        dailyNewsRequestService.getDailyNews(newsId)
                .retryWhen(new RetryWhenHandler(5))
                .subscribeOn(Schedulers.io())//不加这个会出现android.os.NetworkOnMainThreadException
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
