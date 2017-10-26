package com.snick.zzj.t_reader.model.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.snick.zzj.t_reader.AppApplication;

/**网络工具类
 * Created by zzj on 17-10-26.
 */

class NetworkUtil {
    /**
     * 判断是否有网络
     *
     * @return 返回值
     */
    static boolean isNetworkConnected() {
        Context context = AppApplication.getContext();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
