package com.snick.zzj.t_reader.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.beans.ThemeNews;
import com.snick.zzj.t_reader.presenter.SingleThemePresenter;
import com.snick.zzj.t_reader.presenter.impl.SingleThemePresenterImpl;
import com.snick.zzj.t_reader.utils.SourceUrl;

/**
 * Created by zzj on 17-8-7.
 */

public class SingleThemeFragment extends Fragment implements SingleThemeView {
    private SingleThemePresenter singleThemePresenter = new SingleThemePresenterImpl(this);
    private RecyclerView listView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String themeId = getArguments().getString("theme_id");
        singleThemePresenter.loadThemeNews(themeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                needLoadMore = isReachBottom(recyclerView);
//                //到达底部 && 已加载完成
//                //&& !LoadedDate.contains(date)
//                if(needLoadMore  && CachedNews.get(CachedNews.size()-1).getDate().equals(date)) {
//                    basePresenter.refreshViews(SourceUrl.oldNews, date);//api规则：获取5号的News，date则为6号，会+1
//                    needLoadMore = false;
//                    LoadedDate.add(date);
//                    date = String.valueOf(Integer.parseInt(date)-1);
//                }
//                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    @Override
    public void onNewsLoaded(ThemeNews themeNews) {

    }
}
