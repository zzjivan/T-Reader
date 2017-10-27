package com.snick.zzj.t_reader.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

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

/**欢迎界面
 * Created by zzj on 17-2-6.
 */

public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";

    // 要申请的权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE};

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWelcomeImage();
        handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        //showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showWelcomeImage() {

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
                        Log.d(TAG,"welcome:"+welcomeImage.toString());
                        return null;
                    }
                })
                .subscribe(new Subscriber<Bitmap>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable arg0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FrameLayout frameLayout = (FrameLayout) findViewById(android.R.id.content);
                                frameLayout.setBackgroundResource(R.drawable.bg);
                            }
                        });

                        Log.d(TAG, "onError ===== " + arg0.toString());
                    }

                    @Override
                    public void onNext(Bitmap arg0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FrameLayout frameLayout = (FrameLayout) findViewById(android.R.id.content);
                                frameLayout.setBackgroundResource(R.drawable.bg);
                            }
                        });
                    }
                });
    }

    private interface RequestServices {
        @GET("{resolution}")
        Observable<WelcomeImage> getWelcomeImage(@Path("resolution") String resolution);
    }
}
