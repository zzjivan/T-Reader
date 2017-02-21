package com.snick.zzj.t_reader.views.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.presenter.BasePresenter;
import com.snick.zzj.t_reader.presenter.impl.BasePresenterImpl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


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
        private final int TYPE_TOP_NEWS = 0;
        private final int TYPE_FIRST_NEWS = 1;
        private final int TYPE_NORMAL_NEWS = 2;
        private Context context;
        private DailyNews dailyNews;
        private TopNewsPagerAdaper topNewsPagerAdaper;

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
            switch (viewType) {
                case TYPE_TOP_NEWS:
                    return new TopNewsViewHolder(LayoutInflater.from(context).inflate(R.layout.top_news_pager, parent, false));
                case TYPE_FIRST_NEWS:
                    return new FirstNewsViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_first_recyclerview_item, parent, false));
                case TYPE_NORMAL_NEWS:
                    return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false));
                default:
                    return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof NormalViewHolder) {
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                normalViewHolder.textView.setText(dailyNews.getStories().get(position).getTitle());
                Picasso.with(context).load(dailyNews.getStories().get(position).getImages().get(0)).into(normalViewHolder.imageView);
            } else if(holder instanceof FirstNewsViewHolder) {
                FirstNewsViewHolder firstNewsViewHolder = (FirstNewsViewHolder) holder;
                firstNewsViewHolder.date.setText(dailyNews.getDate());
                firstNewsViewHolder.title.setText(dailyNews.getStories().get(position).getTitle());
                Picasso.with(context).load(dailyNews.getStories().get(position).getImages().get(0)).into(firstNewsViewHolder.imageView);
            } else if(holder instanceof TopNewsViewHolder) {
                final TopNewsViewHolder topNewsViewHolder = (TopNewsViewHolder) holder;
                if(topNewsPagerAdaper == null) {
                    topNewsPagerAdaper = new TopNewsPagerAdaper(context, dailyNews);
                    topNewsViewHolder.viewPager_top_news.setAdapter(topNewsPagerAdaper);
                    topNewsViewHolder.viewPager_top_news.setCurrentItem(0);
                    topNewsViewHolder.viewPager_top_news.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                            topNewsViewHolder.viewPager_top_news.setCurrentItem(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    topNewsViewHolder.viewpager_indicator.setViewPager(topNewsViewHolder.viewPager_top_news);
                    topNewsPagerAdaper.registerDataSetObserver(topNewsViewHolder.viewpager_indicator.getDataSetObserver());
                } else {
                    topNewsPagerAdaper.setData(dailyNews);
                    topNewsPagerAdaper.notifyDataSetChanged();
                }
            }
        }

        @Override
        public int getItemCount() {
            return dailyNews.getStories().size();
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0) {
                return TYPE_TOP_NEWS;
            } else if(position == 1) {
                return TYPE_FIRST_NEWS;
            } else {
                return TYPE_NORMAL_NEWS;
            }
        }

        class TopNewsViewHolder extends RecyclerView.ViewHolder {
            private ViewPager viewPager_top_news;
            private CircleIndicator viewpager_indicator;
            public TopNewsViewHolder(View itemView) {
                super(itemView);
                viewPager_top_news = (ViewPager) itemView.findViewById(R.id.vp_top_news);
                viewpager_indicator = (CircleIndicator) itemView.findViewById(R.id.viewpager_indicator);
            }
        }

        class FirstNewsViewHolder extends RecyclerView.ViewHolder {
            private TextView date;
            private LinearLayout linearLayout;
            private TextView title;
            private ImageView imageView;
            public FirstNewsViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.tv_date);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.base_swipe_item_container);
                title = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
                imageView = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
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

    class TopNewsPagerAdaper extends PagerAdapter {
        private Context context;
        private DailyNews dailyNews;
        private List<View> itemList = new ArrayList<>();

        public TopNewsPagerAdaper(Context context, DailyNews dailyNews) {
            this.context = context;
            this.dailyNews = dailyNews;
        }

        @Override
        public int getCount() {
            return dailyNews.getTop_stories().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(itemList.size() <= position) {
                View item = LayoutInflater.from(context).inflate(R.layout.top_news_item, null);
                itemList.add(item);
            }
            container.addView(itemList.get(position));
            Picasso.with(context).load(dailyNews.getTop_stories().get(position).getImage()).into(
                    (ImageView) itemList.get(position).findViewById(R.id.im_top_news_item_img));
            ((TextView) itemList.get(position).findViewById(R.id.tv_top_news_title)).setText(
                    dailyNews.getTop_stories().get(position).getTitle());
            return itemList.get(position);


        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(itemList.get(position));
        }

        public void setData(DailyNews dailyNews) {
            this.dailyNews = dailyNews;
        }
    }
}
