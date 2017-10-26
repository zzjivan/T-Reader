package com.snick.zzj.t_reader.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.snick.zzj.t_reader.beans.NewsThemes;
import com.snick.zzj.t_reader.model.MainNavModel;
import com.snick.zzj.t_reader.model.impl.MainNavModelImpl;
import com.snick.zzj.t_reader.presenter.MainNavPresenter;
import com.snick.zzj.t_reader.views.MainNavView;

import rx.Observer;

/**侧面抽屉栏
 * Created by zzj on 17-8-3.
 */

public class MainNavPresenterImpl implements MainNavPresenter {

    private MainNavModel mainNavModel;
    private MainNavView mainNavView;

    public MainNavPresenterImpl(Context context, MainNavView view) {
        mainNavModel = new MainNavModelImpl(context);
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
            public void onNext(NewsThemes newsThemes) {mainNavView.onThemesLoaded(newsThemes);
            }
        });
    }
}
