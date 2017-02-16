package com.snick.zzj.t_reader.views.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.presenter.BasePresenter;
import com.snick.zzj.t_reader.presenter.impl.BasePresenterImpl;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


/**
 * Created by zzj on 17-2-6.
 */

public class BaseFragment extends Fragment implements BaseView{

    private BasePresenter basePresenter;

    private RecyclerView listView;

    ContentListAdapter listAdapter;

    public BaseFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basePresenter = new BasePresenterImpl(getActivity(),this);
        basePresenter.refreshViews();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void refreshViews(DailyNews dailyNews) {
        if(listAdapter == null) {
            listAdapter = new ContentListAdapter(getActivity(), dailyNews);
            listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            listView.setAdapter(listAdapter);
        } else {
            listAdapter.refresh(dailyNews);
            listAdapter.notifyDataSetChanged();
        }
    }

    class ContentListAdapter extends RecyclerView.Adapter {
        private Context context;
        private DailyNews dailyNews;

        ContentListAdapter(Context context, DailyNews dailyNews) {
            this.context = context;
            this.dailyNews = dailyNews;
        }

        public void refresh(DailyNews dailyNews) {
            this.dailyNews = dailyNews;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //可以依据viewType创建不同的布局，对应不同的ViewHolder。
            return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            NormalViewHolder normalViewHolder = (NormalViewHolder)holder;
            normalViewHolder.textView.setText(dailyNews.getStories().get(position).getTitle());
            Picasso.with(context).load(dailyNews.getStories().get(position).getImages().get(0)).into(normalViewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            Log.d("zjzhu","count:"+dailyNews.getStories().size());
            return dailyNews.getStories().size();
        }

        class NormalViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout linearLayout;
            private TextView textView;
            private ImageView imageView;
            public NormalViewHolder(View itemView) {
                super(itemView);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.base_swipe_item_container);
                textView = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
                imageView = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }
}
