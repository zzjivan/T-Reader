package com.snick.zzj.t_reader.views;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.snick.zzj.t_reader.ActivityManager;
import com.snick.zzj.t_reader.AppApplication;
import com.snick.zzj.t_reader.utils.StatusBarUtil;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by zzj on 17-9-12.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager.getInstance().add(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager.getInstance().remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
