package com.snick.zzj.t_reader.views.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.beans.DailyNews;
import com.snick.zzj.t_reader.beans.Story;
import com.snick.zzj.t_reader.presenter.BasePresenter;
import com.snick.zzj.t_reader.presenter.impl.BasePresenterImpl;
import com.snick.zzj.t_reader.utils.SourceUrl;
import com.snick.zzj.t_reader.views.NewsContentActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by zzj on 17-2-6.
 */

public class BaseFragment extends Fragment implements BaseView {

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                needLoadMore = isReachBottom(recyclerView);
                //到达底部 && 已加载完成
                //&& !LoadedDate.contains(date)
                if(needLoadMore  && CachedNews.get(CachedNews.size()-1).getDate().equals(date)) {
                    basePresenter.refreshViews(SourceUrl.oldNews, date);//api规则：获取5号的News，date则为6号，会+1
                    needLoadMore = false;
                    LoadedDate.add(date);
                    date = String.valueOf(Integer.parseInt(date)-1);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    @Override
    public void refreshViews(DailyNews dailyNews) {
        if(!CachedNews.contains(dailyNews)) {
            Log.d("zjzhu","cachedNews add");
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

    public void refresh() {
        CachedNews.clear();
        listAdapter = null;
        basePresenter.refreshViews(SourceUrl.News, "latest");
    }

    private boolean isReachBottom(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int scrollState = recyclerView.getScrollState();
        if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 &&
                scrollState == recyclerView.SCROLL_STATE_SETTLING)
            return true;
        else
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
                normalViewHolder.linearLayout.setTag(storyList.get(position).getId());
                normalViewHolder.textView.setText(storyList.get(position).getTitle());
                Picasso.with(context).load(storyList.get(position).getImages().get(0)).into(normalViewHolder.imageView);
            } else if (holder instanceof FirstNewsViewHolder) {
                FirstNewsViewHolder firstNewsViewHolder = (FirstNewsViewHolder) holder;
                firstNewsViewHolder.linearLayout.setTag(storyList.get(position).getId());
                firstNewsViewHolder.date.setText(dateFormat(dailyNews.getDate()));
                firstNewsViewHolder.title.setText(storyList.get(position).getTitle());
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
                        startNewsContent(String.valueOf(dailyNewsList.get(0).getStories().get(getAdapterPosition()).getId()),
                                dailyNewsList.get(0).getStories().get(getAdapterPosition()).getImages().get(0));
                    }
                });
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
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsContent(String.valueOf(storyList.get(getAdapterPosition()-1).getId()),
                                storyList.get(getAdapterPosition()-1).getImages().get(0));
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
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsContent(String.valueOf(storyList.get(getAdapterPosition()-1).getId()),
                                storyList.get(getAdapterPosition()-1).getImages().get(0));
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
        public Object instantiateItem(ViewGroup container, final int position) {
            if (itemList.size() <= position) {
                View item = LayoutInflater.from(context).inflate(R.layout.top_news_item, null);
                itemList.add(item);
                item.setTag(dailyNews.getTop_stories().get(position).getId());
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsContent(String.valueOf(dailyNews.getTop_stories().get(position).getId()),
                                dailyNews.getTop_stories().get(position).getImage());
                    }
                });
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

    private void startNewsContent(String news, String header_img_path){
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsContentActivity.class);
        intent.putExtra(SourceUrl.NEWS_ID, news);
        intent.putExtra(SourceUrl.NEWS_HEADER_IMG_ID, header_img_path);
        startActivity(intent);
    }

    private String dateFormat(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        if(simpleDateFormat.format(System.currentTimeMillis()).equals(date))
            return getContext().getResources().getText(R.string.date_today).toString();
        else {
            int year = Integer.parseInt(date.substring(0,4));
            int mounth = Integer.parseInt(date.substring(4,6));
            int day = Integer.parseInt(date.substring(6,8));
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, mounth-1);
            c.set(Calendar.DATE, day);
            int week = c.get(Calendar.DAY_OF_WEEK);
            String weekday = "";
            switch (week) {
                case 1:
                    weekday = "天";
                    break;
                case 2:
                    weekday = "一";
                    break;
                case 3:
                    weekday = "二";
                    break;
                case 4:
                    weekday = "三";
                    break;
                case 5:
                    weekday = "四";
                    break;
                case 6:
                    weekday = "五";
                    break;
                case 7:
                    weekday = "六";
                    break;
                default:break;
            }
            return getContext().getResources().getString(R.string.date_detail, mounth, day, weekday);
        }
    }
}
