package com.snick.zzj.t_reader.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snick.zzj.t_reader.model.NewsContent;

/**
 * Created by zzj on 17-1-22.
 */

public class BaseFragment extends ListFragment {

    private NewsContent news;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news = new NewsContent(getContext(), getLoaderManager());
        news.refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
