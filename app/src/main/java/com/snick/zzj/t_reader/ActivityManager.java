package com.snick.zzj.t_reader;

import com.snick.zzj.t_reader.views.BaseActivity;

import java.util.LinkedList;

public class ActivityManager {
    private LinkedList<BaseActivity> list = new LinkedList<>();

    private static class SingletonHandler {
        public static ActivityManager instance = new ActivityManager();
    }

    public static ActivityManager getInstance() {
        return SingletonHandler.instance;
    }
    public void add(BaseActivity activity) {
        list.add(activity);
    }

    public void remove(BaseActivity activity) {
        list.remove(activity);
    }

    public LinkedList<BaseActivity> getAllActivity() {
        return list;
    }
}
