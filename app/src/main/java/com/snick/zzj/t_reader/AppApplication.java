package com.snick.zzj.t_reader;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by zzj on 17-8-30.
 */

public class AppApplication extends Application {
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        AppApplication application = (AppApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

}