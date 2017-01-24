package com.snick.zzj.t_reader.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.snick.zzj.t_reader.model.beans.WelcomeImage;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zzj on 17-1-22.
 */

public class NewsContent implements LoaderManager.LoaderCallbacks<String> {
    public static final String TAG = "qqq";

    private Context mContext;
    private LoaderManager mLoaderManager;

    public NewsContent(Context context, LoaderManager loaderManager) {
        mContext = context;
        mLoaderManager = loaderManager;
    }

    public void refresh() {
        mLoaderManager.restartLoader(0, null, this);
        mLoaderManager.getLoader(0).forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private static final class NewsLoader extends AsyncTaskLoader<String> {

        public NewsLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://news-at.zhihu.com/api/4/start-image/")
                    //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //增加返回值为Gson的支持(以实体类返回)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestServices requestSerives = retrofit.create(RequestServices.class);//这里采用的是Java的动态代理模式
            Call<WelcomeImage> call = requestSerives.getWelcomeImage("1080*720");//传入我们请求的键值对的值
            call.enqueue(new Callback<WelcomeImage>() {
                @Override
                public void onResponse(Call<WelcomeImage> call, Response<WelcomeImage> response) {
                    Log.d(TAG,response.body().getText()+"\r\n"+response.body().getImg()
                    );
                }

                @Override
                public void onFailure(Call<WelcomeImage> call, Throwable t) {
                    Log.e(TAG,"error"+t);
                }
            });
            return null;
        }
    }

}
