package com.snick.zzj.t_reader.model.impl;

import android.content.Context;
import android.util.Log;

import com.snick.zzj.t_reader.AppApplication;
import com.snick.zzj.t_reader.model.network.CacheInterceptor;
import com.snick.zzj.t_reader.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**XModelImpl的父类
 * 添加okHttpClient
 * Created by zzj on 17-10-24.
 */

class RootModelImpl {
    OkHttpClient okHttpClient;

    RootModelImpl(Context context) {
        //设置超时时间
        Cache cache = new Cache(context.getExternalCacheDir(), CacheInterceptor.CACHE_SIZE);
        Interceptor interceptor = new CacheInterceptor();
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
    }

    //RetryWhen操作符在应用进入后台后，需要取消订阅。否则持有Activity的context，造成内存泄露。
    final class RetryWhenHandler implements Func1<Observable<? extends Throwable>, Observable<Long>> {

        private static final int INITIAL = 1;
        private int maxConnectCount = 1;

        RetryWhenHandler(int retryCount) {
            this.maxConnectCount += retryCount;
        }

        @Override
        public Observable<Long> call(Observable<? extends Throwable> errorObservable) {
            /*
              zipWith是用来生成ThrowableWrapper对象
              concatMap是按次序执行
             */
            return errorObservable.zipWith(Observable.range(INITIAL, maxConnectCount),
                    new Func2<Throwable, Integer, ThrowableWrapper>() {
                        @Override
                        public ThrowableWrapper call(Throwable throwable, Integer i) {

                            //①
                            if (Constants.DEBUG_NETWORK)
                                Log.d("zjzhu", " network error:"+throwable);

                            /*
                            UnknownHostException extends IOException
                            app在前台才重试，否则取消。
                            */
                            if (AppApplication.getAppState() > 0 && throwable instanceof IOException)
                                return new ThrowableWrapper(throwable, i);

                            return new ThrowableWrapper(throwable, maxConnectCount);
                        }
                    }).concatMap(new Func1<ThrowableWrapper, Observable<Long>>() {
                @Override
                public Observable<Long> call(ThrowableWrapper throwableWrapper) {

                    final int retryCount = throwableWrapper.getRetryCount();

                    //②
                    if (maxConnectCount == retryCount) {
                        return Observable.error(throwableWrapper.getSourceThrowable());
                    }
                    if (Constants.DEBUG_NETWORK)
                        Log.d("zjzhu","rxJava retry:"+retryCount);
                    //③
                    return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS,
                            Schedulers.immediate());
                }
            });
        }

        private final class ThrowableWrapper {

            private Throwable sourceThrowable;
            private Integer retryCount;

            ThrowableWrapper(Throwable sourceThrowable, Integer retryCount) {
                this.sourceThrowable = sourceThrowable;
                this.retryCount = retryCount;
            }

            Throwable getSourceThrowable() {
                return sourceThrowable;
            }

            Integer getRetryCount() {
                return retryCount;
            }
        }
    }
}
