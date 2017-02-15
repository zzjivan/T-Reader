package com.snick.zzj.t_reader.views.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.presenter.BasePresenter;
import com.snick.zzj.t_reader.presenter.impl.BasePresenterImpl;


/**
 * Created by zzj on 17-2-6.
 */

public class BaseFragment extends Fragment implements BaseView{

    private BasePresenter basePresenter = new BasePresenterImpl(this);

    private ImageView iv_image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basePresenter.refresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, null);
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        return view;
    }

    @Override
    public void setImages(Drawable drawable) {
        if(iv_image != null)
            iv_image.setBackground(drawable);
    }
}
