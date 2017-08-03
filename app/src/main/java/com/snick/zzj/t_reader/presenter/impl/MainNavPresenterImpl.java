package com.snick.zzj.t_reader.presenter.impl;

import android.util.Log;

import com.snick.zzj.t_reader.beans.NewsThemes;
import com.snick.zzj.t_reader.model.MainNavModel;
import com.snick.zzj.t_reader.model.impl.MainNavModelImpl;
import com.snick.zzj.t_reader.presenter.MainNavPresenter;
import com.snick.zzj.t_reader.views.MainNavView;

import rx.Observer;

/**
 * Created by zzj on 17-8-3.
 */

public class MainNavPresenterImpl implements MainNavPresenter {

    private MainNavModel mainNavModel;
    private MainNavView mainNavView;

    public MainNavPresenterImpl(MainNavView view) {
        mainNavModel = new MainNavModelImpl();
        mainNavView = view;
    }

    @Override
    public void getThemes() {
        mainNavModel.loadThemes(new Observer<NewsThemes>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(NewsThemes newsThemes) {
                Log.d("zjzhu","onNext:"+newsThemes.getOthers().get(1).getName());mainNavView.onThemesLoaded(newsThemes);
            }
        });
    }
}
