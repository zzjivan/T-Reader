package com.snick.zzj.t_reader.views.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.snick.zzj.t_reader.AppApplication;
import com.snick.zzj.t_reader.utils.StatusBarUtil;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by zzj on 17-9-12.
 */

public class RealBaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBar(getActivity(), false, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
