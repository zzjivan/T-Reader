package com.snick.zzj.t_reader.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.snick.zzj.t_reader.beans.ThemeNews;
import com.snick.zzj.t_reader.model.SingleThemeModel;
import com.snick.zzj.t_reader.model.impl.SingleThemeModelImpl;
import com.snick.zzj.t_reader.presenter.SingleThemePresenter;
import com.snick.zzj.t_reader.views.fragment.SingleThemeView;

import rx.Observer;

/**单个主题的页面
 * Created by zzj on 17-8-7.
 */

public class SingleThemePresenterImpl implements SingleThemePresenter {

    private SingleThemeModel singleThemeModel;
    private SingleThemeView singleThemeView;

    public SingleThemePresenterImpl(Context context, SingleThemeView view){
        this.singleThemeView = view;
        singleThemeModel = new SingleThemeModelImpl(context);
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
