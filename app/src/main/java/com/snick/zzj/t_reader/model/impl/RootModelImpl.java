package com.snick.zzj.t_reader.model.impl;

import android.util.Log;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by zzj on 17-10-24.
 */

public class RootModelImpl {
    OkHttpClient okHttpClient;

    /**
     * 本想用拦截器实现连接超时重连，发现有问题。
     * 暂时保留，后期其他功能也许会用到这个OkHttpClient
     */
    public RootModelImpl() {
        //设置超时时间
        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(2, TimeUnit.SECONDS)
//                .readTimeout(2, TimeUnit.SECONDS)
//                .writeTimeout(2, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true).build();
    }

    final class RetryWhenHandler implements Func1<Observable<? extends Throwable>, Observable<Long>> {

        private static final int INITIAL = 1;
        private int maxConnectCount = 1;

        RetryWhenHandler(int retryCount) {
            this.maxConnectCount += retryCount;
        }

        @Override
        public Observable<Long> call(Observable<? extends Throwable> errorObservable) {
            /**
             * zipWith是用来生成ThrowableWrapper对象
             * concatMap是按次序执行
             */
            return errorObservable.zipWith(Observable.range(INITIAL, maxConnectCount),
                    new Func2<Throwable, Integer, ThrowableWrapper>() {
                        @Override
                        public ThrowableWrapper call(Throwable throwable, Integer i) {

                            //①
                            if (throwable instanceof ConnectException)
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
