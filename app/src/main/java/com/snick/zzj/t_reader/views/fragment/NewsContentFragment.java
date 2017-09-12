package com.snick.zzj.t_reader.views.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.beans.NewsExtraInfo;
import com.snick.zzj.t_reader.model.NewsContentModel;
import com.snick.zzj.t_reader.model.impl.NewsContentModelImpl;
import com.snick.zzj.t_reader.presenter.NewsContentPresenter;
import com.snick.zzj.t_reader.presenter.impl.NewsContentPresenterImpl;
import com.snick.zzj.t_reader.utils.SourceUrl;
import com.snick.zzj.t_reader.R;
import com.squareup.picasso.Picasso;
import com.tencent.smtt.sdk.WebView;


/**
 * Created by zzj on 17-2-24.
 */

public class NewsContentFragment extends RealBaseFragment implements NewsContentView{

    private NewsContentPresenter newsContentPresenter;
    private NewsContentModel newsContentModel;

    private WebView tbsContent;
    private ImageView headerImage;
    private Toolbar toolbar;

    private int discuss;
    private int zan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String newsId = getArguments().getString(SourceUrl.NEWS_ID);
        newsContentModel = new NewsContentModelImpl();
        newsContentPresenter = new NewsContentPresenterImpl(getActivity(), newsContentModel, this);
        newsContentPresenter.loadNewsContent(newsId);
        newsContentPresenter.loadNewsExtraInfo(newsId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newscontentfragment, null);
        tbsContent = (WebView) view.findViewById(R.id.tbsContent);
        headerImage = (ImageView) view.findViewById(R.id.headerImage);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        String headImg = getArguments().getString(SourceUrl.NEWS_HEADER_IMG_ID);
        Picasso.with(getContext()).load(headImg).into(headerImage);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.newscontent, menu);
        ((ImageView)menu.getItem(2).getActionView().findViewById(R.id.menu_item_icon))
                .setImageResource(R.drawable.ic_message_black_24dp);
        menu.getItem(2).getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menu.getItem(2));
            }
        });
        menu.getItem(3).getActionView().setPaddingRelative(0,0,36,0);
        ((ImageView)menu.getItem(3).getActionView().findViewById(R.id.menu_item_icon))
                .setImageResource(R.drawable.ic_thumb_up_black_24dp);
        menu.getItem(3).getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menu.getItem(3));
            }
        });
        menu.getItem(3).getActionView().findViewById(R.id.menu_item_icon).setPaddingRelative(36,0,0,9);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        ((TextView)menu.getItem(2).getActionView().findViewById(R.id.menu_item_title)).setText(String.valueOf(discuss));
        ((TextView)menu.getItem(3).getActionView().findViewById(R.id.menu_item_title)).setText(String.valueOf(zan));
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_share:
                break;
            case R.id.action_favourite:
                break;
            case R.id.action_discuss:
                break;
            case R.id.action_zan:
                zan++;
                getActivity().invalidateOptionsMenu();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewsLoaded(final NewsContent content) {
        String css = "<link rel=\"stylesheet\" href=" + content.getCss().get(0) + "\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");//布局对header有一个200px的图片预留，知乎应该有js来实现滑动，我没获取到。
        tbsContent.loadDataWithBaseURL("x-data://base", html, "text/html", "utf-8", null);
    }

    @Override
    public void onNewsExtraInfoLoaded(NewsExtraInfo info) {
        //刷新menu中的数据
        discuss = info.getComments();
        zan = info.getPopularity();
        getActivity().invalidateOptionsMenu();
    }
}
