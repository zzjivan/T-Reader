package com.snick.zzj.t_reader.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.utils.SourceUrl;
import com.snick.zzj.t_reader.views.fragment.NewsContentFragment;

/**
 * Created by zzj on 17-2-24.
 */

public class NewsContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newscontent_activity);
        NewsContentFragment fragment = new NewsContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SourceUrl.NEWS_ID, getIntent().getStringExtra(SourceUrl.NEWS_ID));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}
