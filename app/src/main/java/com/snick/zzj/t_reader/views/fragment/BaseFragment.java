package com.snick.zzj.t_reader.views.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snick.zzj.t_reader.AppApplication;
import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.beans.Story;
import com.snick.zzj.t_reader.presenter.BasePresenter;
import com.snick.zzj.t_reader.presenter.impl.BasePresenterImpl;
import com.snick.zzj.t_reader.utils.SourceUrl;
import com.snick.zzj.t_reader.utils.StatusBarUtil;
import com.snick.zzj.t_reader.views.NewsContentActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;


/**
 * Created by zzj on 17-2-6.
 */

public class BaseFragment extends RealBaseFragment implements BaseView {

    private BasePresenter basePresenter;
    private RecyclerView listView;
    private ContentListAdapter listAdapter;

    private String date;
    private List<DailyNews> CachedNews = new ArrayList<>();
    private List<String> LoadedDate = new ArrayList<>();
    private boolean needLoadMore = false;

    public BaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basePresenter = new BasePresenterImpl(getActivity(), this);
        SimpleDateFormat dateFormat = new SimpleDateFormat(SourceUrl.DateFormat);
        date = String.valueOf(Integer.parseInt(dateFormat.format(System.currentTimeMillis())));
        basePresenter.refreshViews(SourceUrl.News, "latest");

        //不同的界面statusbar的处理方式不一样
        Activity activity = getActivity();
        int nightMode = ((AppApplication)activity.getApplication()).getDayNightMode();
        if (nightMode == MODE_NIGHT_NO) {
            StatusBarUtil.setStatusBar(getActivity(), false, false);
        } else {
            StatusBarUtil.setStatusBar(getActivity(), true, true);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, container, false);
        listView = view.findViewById(R.id.list);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                needLoadMore = isReachBottom(recyclerView, newState);
                //到达底部 && 已加载完成
                //&& !LoadedDate.contains(date)
                Log.d("zjzhu", needLoadMore+"---"+date+"---"+CachedNews.get(CachedNews.size()-1).getDate());
                if(needLoadMore  && CachedNews.get(CachedNews.size()-1).getDate().equals(date)) {
                    loadMore();
                    needLoadMore = false;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }

    @Override
    public void refreshViews(DailyNews dailyNews) {
        //获取latest新闻后，在获取一次上一天的内容
        if (dailyNews.getTop_stories() != null)
            loadMore();

        if(!CachedNews.contains(dailyNews)) {
            CachedNews.add(dailyNews);
        }

        if (listAdapter == null) {
            listAdapter = new ContentListAdapter(getActivity(), CachedNews);
            listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            listView.setAdapter(listAdapter);
        } else {
            listAdapter.refresh(CachedNews);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void loadMore() {
        basePresenter.refreshViews(SourceUrl.oldNews, date);//api规则：获取5号的News，date则为6号，会+1
        LoadedDate.add(date);
        date = String.valueOf(Integer.parseInt(date)-1);
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isReachBottom(RecyclerView recyclerView, int newState) {
        //当前状态为停止滑动状态SCROLL_STATE_IDLE时
        int lastPosition = 0;
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                //通过LayoutManager找到当前显示的最后的item的position
                lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                lastPosition = findMax(lastPositions);
            }

            //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
            //如果相等则说明已经滑动到最后了
            if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }

    class ContentListAdapter extends RecyclerView.Adapter {
        private final int TYPE_TOP_NEWS = 0;
        private final int TYPE_FIRST_NEWS = 1;
        private final int TYPE_NORMAL_NEWS = 2;
        private Context context;
        private List<DailyNews> dailyNewsList;
        private List<Story> storyList = new ArrayList<>();
        private TopNewsPagerAdaper topNewsPagerAdaper;
        private List<Integer> firstNewsFlag = new ArrayList<>();

        ContentListAdapter(Context context, List<DailyNews> dailyNewsList) {
            this.context = context;
            refresh(dailyNewsList);
        }

        public void refresh(List<DailyNews> dailyNewsList) {
            this.dailyNewsList = dailyNewsList;

            int flag = 1;
            firstNewsFlag.clear();
            for(int i = 0; i < dailyNewsList.size(); i ++) {
                if(i >= 1)
                    flag += dailyNewsList.get(i-1).getStories().size();
                firstNewsFlag.add(flag);
            }

            storyList.clear();
            for(DailyNews dailyNews : dailyNewsList)
                storyList.addAll(dailyNews.getStories());
        }

        private DailyNews getDailyNewsByPosition(int position) {
            DailyNews dailyNews = null;
            int count = 0;
            for(int i = 0; i < dailyNewsList.size(); i ++) {
                dailyNews = dailyNewsList.get(i);
                count += dailyNews.getStories().size();
                if(count >= position)
                    break;
                dailyNews = null;
            }
            return dailyNews;
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
            //因为第一个item为TopNews的容器，因此position偏移1
            //下面的ViewHolder中的click事件需一样处理
            if(position != 0)
                position -= 1;

            //获取dailyNews时，position是不需要-1的。
            //例如第一个dailyNews获取是用position=1来获取，而不是position=0.
            DailyNews dailyNews = getDailyNewsByPosition(position+1);
            if(dailyNews == null) return;

            if (holder instanceof NormalViewHolder) {
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                normalViewHolder.relativeLayout.setTag(storyList.get(position).getId());
                normalViewHolder.textView.setText(storyList.get(position).getTitle());
                normalViewHolder.summary.setText((storyList.get(position).getHint()));
                Picasso.with(context).load(storyList.get(position).getImages().get(0)).into(normalViewHolder.imageView);
            } else if (holder instanceof FirstNewsViewHolder) {
                FirstNewsViewHolder firstNewsViewHolder = (FirstNewsViewHolder) holder;
                firstNewsViewHolder.relativeLayout.setTag(storyList.get(position).getId());
                firstNewsViewHolder.date.setText(dateFormat(dailyNews.getDate()));
                firstNewsViewHolder.title.setText(storyList.get(position).getTitle());
                firstNewsViewHolder.summary.setText(storyList.get(position).getHint());
                if ("hide".equals(dateFormat((dailyNews.getDate()))))
                    firstNewsViewHolder.ll_date.setVisibility(View.INVISIBLE);
                Picasso.with(context).load(storyList.get(position).getImages().get(0)).into(firstNewsViewHolder.imageView);
            } else if (holder instanceof TopNewsViewHolder) {
                final TopNewsViewHolder topNewsViewHolder = (TopNewsViewHolder) holder;
                if (topNewsPagerAdaper == null) {
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
            //第一个item为topNews的容器，count从1开始加
            int count = 1;
            for(DailyNews dailyNews : dailyNewsList)
                count += dailyNews.getStories().size();
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_TOP_NEWS;
            } else if (firstNewsFlag.contains(position)) {
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
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsContent(String.valueOf(dailyNewsList.get(0).getStories().get(getAdapterPosition()).getId()));
                    }
                });
            }
        }

        class FirstNewsViewHolder extends RecyclerView.ViewHolder {
            private TextView date;
            private RelativeLayout relativeLayout;
            private LinearLayout ll_date;
            private TextView summary;
            private TextView title;
            private ImageView imageView;

            public FirstNewsViewHolder(View itemView) {
                super(itemView);
                ll_date = itemView.findViewById(R.id.ll_date);
                date = (TextView) itemView.findViewById(R.id.tv_date);
                relativeLayout = itemView.findViewById(R.id.base_swipe_item_container);
                title = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
                summary = itemView.findViewById(R.id.tv_summary);
                imageView = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsContent(String.valueOf(storyList.get(getAdapterPosition()-1).getId()));
                    }
                });
            }
        }

        class NormalViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout relativeLayout;
            private TextView textView;
            private TextView summary;
            private ImageView imageView;

            public NormalViewHolder(View itemView) {
                super(itemView);
                relativeLayout = itemView.findViewById(R.id.base_swipe_item_container);
                textView = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
                summary = itemView.findViewById(R.id.tv_summary);
                imageView = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsContent(String.valueOf(storyList.get(getAdapterPosition()-1).getId()));
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
            return dailyNews != null && dailyNews.getTop_stories() != null ? dailyNews.getTop_stories().size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (itemList.size() <= position) {
                View item = LayoutInflater.from(context).inflate(R.layout.top_news_item, null);
                itemList.add(item);
                item.setTag(dailyNews.getTop_stories().get(position).getId());
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsContent(String.valueOf(dailyNews.getTop_stories().get(position).getId()));
                    }
                });
            }
            container.addView(itemList.get(position));
            Picasso.with(context).setIndicatorsEnabled(true);
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

    private void startNewsContent(String news){
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsContentActivity.class);
        intent.putExtra(SourceUrl.NEWS_ID, news);
        startActivity(intent);
    }

    private String dateFormat(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy");
        if(simpleDateFormat.format(System.currentTimeMillis()).equals(date))
            return "hide";
        else {
            int year = Integer.parseInt(date.substring(0,4));
            int month = Integer.parseInt(date.substring(4,6));
            int day = Integer.parseInt(date.substring(6,8));
            StringBuilder result = new StringBuilder();
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month-1);
            c.set(Calendar.DATE, day);

            if (!yyyyFormat.format(System.currentTimeMillis()).equals(String.valueOf(year)))
                result.append(year).append("年");
            result.append(month).append("月");
            result.append(day).append("日");

            return result.toString();
        }
    }
}
