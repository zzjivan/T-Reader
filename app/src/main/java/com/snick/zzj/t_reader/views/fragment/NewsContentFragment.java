package com.snick.zzj.t_reader.views.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.tencent.smtt.sdk.WebViewClient;


/**内容详情fragment
 * Created by zzj on 17-2-24.
 */

public class NewsContentFragment extends RealBaseFragment implements NewsContentView , View.OnClickListener {

    private NewsContentPresenter newsContentPresenter;
    private NewsContentModel newsContentModel;

    private LinearLayout anchor;
    private WebView tbsContent;
    private ImageView headerImage;
    private RelativeLayout rl_discuss_panel;
    private RelativeLayout rl_thumb_panel;
    private RelativeLayout rl_favourite_panel;
    private RelativeLayout rl_share_panel;
    private TextView tv_discuss;
    private TextView tv_thumb;
    private ImageButton ib_back;

    private WebViewClient client = new WebViewClient() {
        // 防止加载网页时调起系统浏览器
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String newsId = getArguments().getString(SourceUrl.NEWS_ID);
        newsContentModel = new NewsContentModelImpl(getContext());
        newsContentPresenter = new NewsContentPresenterImpl(getActivity(), newsContentModel, this);
        newsContentPresenter.loadNewsContent(newsId);
        newsContentPresenter.loadNewsExtraInfo(newsId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newscontentfragment, null);
        anchor = view.findViewById(R.id.anchor);
        headerImage = view.findViewById(R.id.headerImage);
        rl_discuss_panel = view.findViewById(R.id.rl_discuss_panel);
        rl_favourite_panel = view.findViewById(R.id.rl_favourite_panel);
        rl_share_panel = view.findViewById(R.id.rl_share_panel);
        rl_thumb_panel = view.findViewById(R.id.rl_thumb_panel);
        tv_discuss = view.findViewById(R.id.tv_discuss);
        tv_thumb = view.findViewById(R.id.tv_thumb);
        ib_back = view.findViewById(R.id.ib_back);
        rl_discuss_panel.setOnClickListener(this);
        rl_favourite_panel.setOnClickListener(this);
        rl_share_panel.setOnClickListener(this);
        rl_thumb_panel.setOnClickListener(this);
        ib_back.setOnClickListener(this);

        //动态添加WebView，避免内存泄漏。这里传入application context可能导致对话框弹出异常
        tbsContent = new WebView(getActivity().getApplicationContext());
        anchor.addView(tbsContent);
        tbsContent.setWebViewClient(client);

        return view;
    }

    @Override
    public void onNewsLoaded(final NewsContent content) {
        String css = "<link rel=\"stylesheet\" href=" + content.getCss().get(0) + "\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");//布局对header有一个200px的图片预留，知乎应该有js来实现滑动，我没获取到。
        tbsContent.loadDataWithBaseURL("x-data://base", html, "text/html", "utf-8", null);
        //加载清晰的header图片
        Picasso.with(getContext()).load(content.getImage()).into(headerImage);
    }

    @Override
    public void onNewsExtraInfoLoaded(NewsExtraInfo info) {
        //刷新menu中的数据
        tv_discuss.setText(String.valueOf(info.getComments()));
        tv_thumb.setText(String.valueOf(info.getPopularity()));
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {
        anchor.removeView(tbsContent);
        tbsContent.destroy();
        tbsContent = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                getActivity().finish();
                break;
            case R.id.rl_discuss_panel:
                break;
            case R.id.rl_favourite_panel:
                break;
            case R.id.rl_share_panel:
                break;
            case R.id.rl_thumb_panel:
                break;
             default:
                break;
        }
    }
}
