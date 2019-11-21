package com.snick.zzj.t_reader.views;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.snick.zzj.t_reader.ActivityManager;
import com.snick.zzj.t_reader.AppApplication;
import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.utils.StatusBarUtil;
import com.snick.zzj.t_reader.views.fragment.BaseFragment;

import java.util.List;


public class MainActivity extends BaseActivity {
    private BaseFragment homepageFragment;

    private FrameLayout fl_statusbar;
    private ImageView iv_user_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_statusbar = findViewById(R.id.fl_statusbar);
        iv_user_logo = findViewById(R.id.iv_user_logo);
        StatusBarUtil.initStatusBar(this, fl_statusbar);
        homepageFragment = new BaseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, homepageFragment, "homepage").commitAllowingStateLoss();

        iv_user_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppApplication.changeTheme();
                List<BaseActivity> list= ActivityManager.getInstance().getAllActivity();
                for (BaseActivity activity : list)
                    activity.recreate();
            }
        });
    }
}
