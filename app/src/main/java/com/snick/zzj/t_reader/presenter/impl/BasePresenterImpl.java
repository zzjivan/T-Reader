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

    private final boolean DEBUG = true;

    private final String TAG = "BasePresenterImpl";

    private Context context;
    private BaseModel baseModel;
    private BaseView baseView;

    public BasePresenterImpl(Context context, BaseView baseview) {
        this.context = context;
        this.baseView = baseview;
        baseModel = new BaseModelImpl();
    }

    @Override
    public void refreshViews(String type, String date) {
        baseModel.refreshViews(new Observer<DailyNews>() {//rxAndroid处理回调
            @Override
            public void onCompleted() {
                Log.d("zjzhu","refreshViews complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("zjzhu","refreshViews error");
                e.printStackTrace();
            }

            @Override
            public void onNext(DailyNews dailyNews) {
                Log.d("zjzhu","refreshViews next");
                baseView.refreshViews(dailyNews);
            }
        }, type, date);
    }

    @Override
    public void handleNewsClick(String newsId) {
        baseModel.getNewsContent(newsId, new Observer<DailyNews>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DailyNews dailyNews) {

            }
        });
    }
}
