package com.snick.zzj.t_reader.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.model.NewsContentModel;
import com.snick.zzj.t_reader.model.impl.NewsContentModelImpl;
import com.snick.zzj.t_reader.presenter.NewsContentPresenter;
import com.snick.zzj.t_reader.presenter.impl.NewsContentPresenterImpl;
import com.snick.zzj.t_reader.utils.SourceUrl;
import com.snick.zzj.t_reader.R;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.w3c.dom.Document;

/**
 * Created by zzj on 17-2-24.
 */

public class NewsContentFragment extends Fragment implements NewsContentView {

    private NewsContentPresenter newsContentPresenter;
    private NewsContentModel newsContentModel;

    private WebView tbsContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String newsId = getArguments().getString(SourceUrl.NEWS_ID);
        newsContentModel = new NewsContentModelImpl();
        newsContentPresenter = new NewsContentPresenterImpl(getActivity(), newsContentModel, this);
        newsContentPresenter.loadNewsContent(newsId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newscontentfragment, null);
        tbsContent = (com.tencent.smtt.sdk.WebView) view.findViewById(R.id.tbsContent);

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

}
