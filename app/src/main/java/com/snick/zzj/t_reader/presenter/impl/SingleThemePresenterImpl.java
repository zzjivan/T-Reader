package com.snick.zzj.t_reader.presenter.impl;

import android.util.Log;

import com.snick.zzj.t_reader.beans.ThemeNews;
import com.snick.zzj.t_reader.model.SingleThemeModel;
import com.snick.zzj.t_reader.model.impl.SingleThemeModelImpl;
import com.snick.zzj.t_reader.presenter.SingleThemePresenter;
import com.snick.zzj.t_reader.views.fragment.SingleThemeView;

import rx.Observer;

/**
 * Created by zzj on 17-8-7.
 */

public class SingleThemePresenterImpl implements SingleThemePresenter {

    private SingleThemeModel singleThemeModel;
    private SingleThemeView singleThemeView;

    public SingleThemePresenterImpl(SingleThemeView view){
        this.singleThemeView = view;
        singleThemeModel = new SingleThemeModelImpl();
    }

    @Override
    public void loadThemeNews(String id) {
        singleThemeModel.loadSingleTheme(new Observer<ThemeNews>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ThemeNews themeNews) {
                singleThemeView.onNewsLoaded(themeNews);
            }
        }, id);
    }
}
