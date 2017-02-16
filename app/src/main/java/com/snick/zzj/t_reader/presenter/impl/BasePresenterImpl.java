package com.snick.zzj.t_reader.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.model.BaseModel;
import com.snick.zzj.t_reader.model.impl.BaseModelImpl;
import com.snick.zzj.t_reader.presenter.BasePresenter;
import com.snick.zzj.t_reader.views.fragment.BaseView;

import rx.Observer;

/**
 * Created by zzj on 17-2-6.
 */

public class BasePresenterImpl implements BasePresenter {

    private Context context;
    private BaseModel baseModel;
    private BaseView baseView;

    public BasePresenterImpl(Context context, BaseView baseview) {
        this.context = context;
        this.baseView = baseview;
        baseModel = new BaseModelImpl();
    }

    @Override
    public void refreshViews() {
        baseModel.refreshViews(new Observer<DailyNews>() {//rxAndroid处理回调
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(DailyNews dailyNews) {
                DailyNews news = dailyNews;
                baseView.refreshViews(dailyNews);

            }
        });
    }
}
