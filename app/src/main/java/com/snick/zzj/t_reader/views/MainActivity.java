package com.snick.zzj.t_reader.views;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.utils.StatusBarUtil;
import com.snick.zzj.t_reader.views.fragment.BaseFragment;

public class MainActivity extends BaseActivity {
    private BaseFragment homepageFragment;

    private FrameLayout fl_statusbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_statusbar = findViewById(R.id.fl_statusbar);
        StatusBarUtil.initStatusBar(this, fl_statusbar);
        homepageFragment = new BaseFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, homepageFragment, "homepage").commit();
    }
}
