package com.snick.zzj.t_reader.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.ui.ObserverableWebView;
import com.snick.zzj.t_reader.utils.SourceUrl;
import com.snick.zzj.t_reader.views.fragment.NewsContentFragment;

/**
 * Created by zzj on 17-2-24.
 */

public class NewsContentActivity extends AppCompatActivity implements ObserverableWebView.onScrollChangedCallback {

    private Toolbar toolbar;
    private float toolbarAlpha = 1.0f;
    private int scrollY = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newscontent_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        NewsContentFragment fragment = new NewsContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SourceUrl.NEWS_ID, getIntent().getStringExtra(SourceUrl.NEWS_ID));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newscontent,menu);
        return true;
    }

    @Override
    public void onScrollChangedCallback(int l, int t, int oldl, int oldt) {
        Log.d("zjzhu", "scroll:" + l + "," + t + "," + oldl + "," + oldt);
        scrollY += (t - oldt);
        if (scrollY <= 600 || (t > 600 && oldt < 600)) { //180dp  //从500多 到600多的跨越，临界值特殊处理
            float delta = Math.abs(((float) t - (float) oldt) / 600);
            if (t - oldt > 0) { //向下滑动
                toolbarAlpha -= delta;
            } else { //向上滑动
                toolbarAlpha += delta;
            }
            if (toolbarAlpha < 0) { //这应该是不可能的，保护处理。
                toolbarAlpha = 0;
            } else if (toolbarAlpha > 1) {
                toolbarAlpha = 1.0f;
            }
            if (toolbar != null) {
                toolbar.setAlpha(toolbarAlpha);
            }
        }
    }
}
