package com.snick.zzj.t_reader.views.fragment;

import com.snick.zzj.t_reader.beans.NewsContent;
import com.snick.zzj.t_reader.beans.NewsExtraInfo;

/**
 * Created by zzj on 17-2-24.
 */

public interface NewsContentView {

    void onNewsLoaded(NewsContent content);
    void onNewsExtraInfoLoaded(NewsExtraInfo info);
}
