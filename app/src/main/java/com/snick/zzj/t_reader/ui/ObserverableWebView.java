package com.snick.zzj.t_reader.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * Created by zzj on 2017/2/25.
 */

public class ObserverableWebView extends WebView {

    private onScrollChangedCallback callback;

    public interface onScrollChangedCallback {
        void onScrollChangedCallback(int l, int t, int oldl, int oldt);
    }

    public ObserverableWebView(Context context) {
        super(context);
    }

    public ObserverableWebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
    }

    public ObserverableWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
    }

    public ObserverableWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ObserverableWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(callback != null)
            callback.onScrollChangedCallback(l , t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setonScrollChangedCallbackListener(onScrollChangedCallback callback) {
        this.callback = callback;
    }
}
