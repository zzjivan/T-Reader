package com.snick.zzj.t_reader.presenter;

/**
 * Created by zzj on 17-2-6.
 */

public interface BasePresenter {
    void refreshViews(String type, String date);
    void handleNewsClick(String newsId);
}
