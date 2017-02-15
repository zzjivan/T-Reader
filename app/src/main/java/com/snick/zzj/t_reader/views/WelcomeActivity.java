package com.snick.zzj.t_reader.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.beans.WelcomeImage;
import com.snick.zzj.t_reader.utils.SourceUrl;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zzj on 17-2-6.
 */

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWelcomeImage();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);

    }

    private void showWelcomeImage() {
        //获取本地图片

        //本地没有图片，从网络获取
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SourceUrl.welcomeImage)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为RxJava支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RequestServices request = retrofit.create(RequestServices.class);
        request.getWelcomeImage("1920*1080")
                .subscribeOn(Schedulers.newThread())//在新线程中实现该方法
                .map(new Func1<WelcomeImage, Bitmap>() {
                    @Override
                    public Bitmap call(WelcomeImage welcomeImage) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<Bitmap>() {

                    ProgressDialog dialog = new ProgressDialog(WelcomeActivity.this);

                    @Override
                    public void onStart() {
                        dialog.show();
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable arg0) {
                        Log.d(TAG, "onError ===== " + arg0.toString());
                    }

                    @Override
                    public void onNext(Bitmap arg0) {
                        LinearLayout linearLayout = (LinearLayout) findViewById(android.R.id.content);
                        linearLayout.setBackground(new BitmapDrawable(arg0));
                    }
                });

    }


    private interface RequestServices {
        @GET("{resolution}")
        Observable<WelcomeImage> getWelcomeImage(@Path("resolution") String resolution);
    }
}
