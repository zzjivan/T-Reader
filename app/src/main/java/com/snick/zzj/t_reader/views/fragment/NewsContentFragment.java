package com.snick.zzj.t_reader.views.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.model.NewsContentModel;
import com.snick.zzj.t_reader.model.impl.NewsContentModelImpl;
import com.snick.zzj.t_reader.presenter.NewsContentPresenter;
import com.snick.zzj.t_reader.presenter.impl.NewsContentPresenterImpl;
import com.snick.zzj.t_reader.utils.SourceUrl;
import com.snick.zzj.t_reader.ui.ObserverableWebView;
import com.snick.zzj.t_reader.R;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.w3c.dom.Document;

/**
 * Created by zzj on 17-2-24.
 */

public class NewsContentFragment extends Fragment implements NewsContentView , ObserverableWebView.onScrollChangedCallback{

    private NewsContentPresenter newsContentPresenter;
    private NewsContentModel newsContentModel;

    private Toolbar toolbar;
    private ObserverableWebView tbsContent;

    private float toolbarAlpha = 1.0f;
    private int scrollY = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
        }

        String newsId = getArguments().getString(SourceUrl.NEWS_ID);
        newsContentModel = new NewsContentModelImpl();
        newsContentPresenter = new NewsContentPresenterImpl(getActivity(), newsContentModel, this);
        newsContentPresenter.loadNewsContent(newsId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newscontentfragment, null);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tbsContent = (ObserverableWebView) view.findViewById(R.id.tbsContent);
        tbsContent.setonScrollChangedCallbackListener(this);
        return view;
    }

    @Override
    public void onNewsLoaded(final NewsContent content) {
        //TODO:弱爆了，不知道正确的CSS引入姿势是什么。。
        String s = "<link rel=\"stylesheet\" href=" + content.getCss().get(0) + "\">"+content.getBody();
        tbsContent.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
        tbsContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onScrollChangedCallback(int l, int t, int oldl, int oldt) {
        Log.d("zjzhu","scroll:"+l+","+t+","+oldl+","+oldt);
        scrollY += (t - oldt);
        if(scrollY <= 600 || (t > 600 && oldt < 600)) { //180dp  //从500多 到600多的跨越，临界值特殊处理
            float delta = Math.abs(((float)t-(float)oldt) / 600);
            if(t - oldt > 0) { //向下滑动
                toolbarAlpha -= delta;
            } else { //向上滑动
                toolbarAlpha += delta;
            }
            if(toolbarAlpha < 0) { //这应该是不可能的，保护处理。
                toolbarAlpha = 0;
            } else if(toolbarAlpha > 1) {
                toolbarAlpha = 1.0f;
            }
            if(toolbar != null) {
                toolbar.setAlpha(toolbarAlpha);
            }
        }
    }
}
