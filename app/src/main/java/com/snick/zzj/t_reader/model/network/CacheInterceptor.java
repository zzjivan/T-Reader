package com.snick.zzj.t_reader.model.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zzj on 17-10-26.
 * OkHttp实现缓存。
 * TODO：要适配Picasso的缓存策略，不然可能出现有内容，无图片的问题
 */

public class CacheInterceptor implements Interceptor {
    public static final int CACHE_SIZE = 1024 * 1024 * 10;
    private static final int TIMEOUT_CONNECT = 60 * 60 * 24; //24小时

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String cache = request.header("cache");
        boolean isNetworkConnected = NetworkUtil.isNetworkConnected();

        //若没有网络，强制读取缓存
        if (!isNetworkConnected) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);

        if (isNetworkConnected) {
            String cacheControl = originalResponse.header("Cache-Control");
            //如果cacheControl为空，就让他TIMEOUT_CONNECT秒的缓存;否则直接返回response
            if (cacheControl == null || cacheControl.contains("no-cache")) {
                //如果cache没值，缓存时间为TIMEOUT_CONNECT，有的话就为cache的值
                if (cache == null || "".equals(cache)) {
                    cache = TIMEOUT_CONNECT + "";
                }
                originalResponse = originalResponse.newBuilder()
                        //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + cache)
                        .build();
            }
            return originalResponse;
        } else {
            return originalResponse.newBuilder()
                    //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                    .header("Cache-Control", "public, only-if-cached, max-age=" + TIMEOUT_CONNECT)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
