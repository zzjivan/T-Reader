package com.snick.zzj.t_reader.views.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import com.snick.zzj.t_reader.R;

/**
 * Created by zzj on 17-8-9.
 */

public class CircleImageView extends AppCompatImageView {

    private Paint mImgPaint;
    private Matrix mMatrix;
    private Shader mBitmapShader;
    private int mRadius;

    private int mWidth;
    private int mHeight;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化图片变换处理器
        mMatrix = new Matrix();

        mImgPaint = new Paint();
        mImgPaint.setAntiAlias(true);//抗锯齿,没有消除锯齿的话，图片变换会很难看的，因为有些像素点会失真
        mImgPaint.setStrokeWidth(12);//设置圆边界宽度
    }

    //使用BitmapShader画圆图形
    private void setBitmapShader() {
        //将图片转换成Bitmap
        Drawable drawable = getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //将bitmap放进图像着色器，后面两个模式是x，y轴的缩放模式，CLAMP表示拉伸
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //初始化图片与view之间伸缩比例，因为比例一般非整数，所以用float，免得精度丢失
        float scale = 1.0f;
        //将图片的宽度高度的最小者作为图片的边长，用来和view来计算伸缩比例
        int bitmapSize = Math.min(bitmap.getHeight(), bitmap.getWidth());
        //计算缩放比例，view的大小和图片的大小比例
        scale = mWidth * 1.0f / bitmapSize;
        //利用这个图像变换处理器，设置伸缩比例，长宽以相同比例伸缩
        mMatrix.setScale(scale, scale);
        //给那个图像着色器设置变换矩阵，绘制时就根据view的size，设置图片的size
        //使图片的较小的一边缩放到view的大小一致，这样就可以避免图片过小导致CLAMP拉伸模式或过大导致显示不全
        mBitmapShader.setLocalMatrix(mMatrix);
        //为画笔套上一个Shader的笔套
        mImgPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            setBitmapShader();
            //canvas.drawRect(0, 0, mWidth, mHeight, mPaintBackgroud);//直接构造，画背景的，为什么画背景？因为画布是方的，市面上所有圆形头像都是没有直接处理边角的，而是用Framelayout来进去覆盖，所以这里定义个背景色告诉大家，当然也封装好给大家使用
            canvas.drawCircle(mRadius, mRadius, mRadius, mImgPaint);
        } else {
            //如果在xml中这个继承ImageView的类没有被set图片就用默认的ImageView方案咯
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 在这里设置高度宽度，以设置的较小值为准，防止不成圆
         */
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int mCircleSize = Math.min(mHeight, mWidth);
        //圆的半径短的二分之一作为半径
        mRadius = mCircleSize / 2;
    }
}
