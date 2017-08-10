package com.snick.zzj.t_reader.views.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snick.zzj.t_reader.R;

/**
 * Created by zzj on 17-8-7.
 */

public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;


    private SparseArrayCompat<View> mHeadersView = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootersView = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    public boolean isHeader(int position) {
        return position < mHeadersView.size();
    }

    public boolean isFooter(int position) {
        return position > mHeadersView.size() + mInnerAdapter.getItemCount();
    }

    public void addHeadView(View header) {
        mHeadersView.put(mHeadersView.size() + BASE_ITEM_TYPE_HEADER, header);
    }

    public void addFooterView(View footer) {
        mHeadersView.put(mHeadersView.size() + BASE_ITEM_TYPE_FOOTER, footer);
    }

    public void cleanAll() {
        mHeadersView.clear();
        mFootersView.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeader(position))
            return mHeadersView.keyAt(position);
        else if(isFooter(position))
            return mFootersView.keyAt(position - mHeadersView.size() - mInnerAdapter.getItemCount());
        return mInnerAdapter.getItemViewType(position - mHeadersView.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里才是精华！！
        // 每一个Header和Footer都当做一个viewtype，而且type数值起的非常大，这不会影响正常定义其他的viewtype
        //header和footer没必要创建viewholder，毕竟不需要重用？
        if(mHeadersView.get(viewType) != null)
            return new NormalViewHolder(mHeadersView.get(viewType));
        else if(mFootersView.get(viewType) != null)
            return new NormalViewHolder(mHeadersView.get(viewType));
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHeader(position))
            return;
        else if(isFooter(position))
            return;
        else
            mInnerAdapter.onBindViewHolder(holder, position - mHeadersView.size());
    }

    @Override
    public int getItemCount() {
        return mHeadersView.size() + mInnerAdapter.getItemCount() + mFootersView.size();
    }

}
