package com.snick.zzj.t_reader.presenter.impl;

import android.content.Context;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.beans.NewsExtraInfo;
import com.snick.zzj.t_reader.model.NewsContentModel;
import com.snick.zzj.t_reader.presenter.NewsContentPresenter;
import com.snick.zzj.t_reader.views.fragment.NewsContentView;

import rx.Observer;

/**
 * Created by zzj on 17-2-24.
 */

public class NewsContentPresenterImpl implements NewsContentPresenter {

    private Context context;
    private NewsContentModel newsContentModel;
    private NewsContentView newsContentView;

    public NewsContentPresenterImpl(Context context, NewsContentModel model, NewsContentView view) {
        this.context = context;
        this.newsContentModel = model;
        this.newsContentView = view;
    }


    @Override
    public void loadNewsContent(String newsId) {
        newsContentModel.loadNewsContent(newsId, new Observer<NewsContent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewsContent newsContent) {
                newsContentView.onNewsLoaded(newsContent);
            }
        });
    }

    @Override
    public void loadNewsExtraInfo(String newsId) {
        newsContentModel.loadNewsExtraInfo(newsId, new Observer<NewsExtraInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewsExtraInfo newsExtraInfo) {
                newsContentView.onNewsExtraInfoLoaded(newsExtraInfo);
            }
        });
    }
}
