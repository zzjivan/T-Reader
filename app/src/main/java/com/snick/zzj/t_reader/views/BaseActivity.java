package com.snick.zzj.t_reader.views;

import androidx.appcompat.app.AppCompatActivity;

import com.snick.zzj.t_reader.AppApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by zzj on 17-9-12.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
