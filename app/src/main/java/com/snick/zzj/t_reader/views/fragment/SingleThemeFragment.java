package com.snick.zzj.t_reader.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.snick.zzj.t_reader.beans.ThemeNews;
import com.snick.zzj.t_reader.presenter.SingleThemePresenter;
import com.snick.zzj.t_reader.presenter.impl.SingleThemePresenterImpl;
import com.snick.zzj.t_reader.views.adapter.HeaderAndFooterWrapper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by zzj on 17-8-7.
 */

public class SingleThemeFragment extends Fragment implements SingleThemeView {
    private SingleThemePresenter singleThemePresenter = new SingleThemePresenterImpl(this);
    private RecyclerView listView;
    private NewsListAdapter newsListAdapter;
    private HeaderAndFooterWrapper newsListAdapterWrapper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String themeId = getArguments().getString("theme_id");
        singleThemePresenter.loadThemeNews(themeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                needLoadMore = isReachBottom(recyclerView);
//                //到达底部 && 已加载完成
//                //&& !LoadedDate.contains(date)
//                if(needLoadMore  && CachedNews.get(CachedNews.size()-1).getDate().equals(date)) {
//                    basePresenter.refreshViews(SourceUrl.oldNews, date);//api规则：获取5号的News，date则为6号，会+1
//                    needLoadMore = false;
//                    LoadedDate.add(date);
//                    date = String.valueOf(Integer.parseInt(date)-1);
//                }
//                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    @Override
    public void onNewsLoaded(ThemeNews themeNews) {
        if(newsListAdapterWrapper == null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(themeNews.getName());
            newsListAdapter = new NewsListAdapter(getActivity());
            newsListAdapter.addResource(themeNews);
            newsListAdapterWrapper = new HeaderAndFooterWrapper(newsListAdapter);

            View headerImg = LayoutInflater.from(getActivity()).inflate(R.layout.image_header, null);
            //在xml中设置match_parent无效？
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getActivity().getResources().getDimensionPixelOffset(R.dimen.top_news_header_height));
            headerImg.setLayoutParams(layoutParams);
            ImageView imageView = (ImageView) headerImg.findViewById(R.id.headerImage);
            TextView textView = (TextView) headerImg.findViewById(R.id.headerText);
            Picasso.with(getActivity()).load(themeNews.getImage()).into(imageView);
            textView.setText(themeNews.getDescription());

            View editor = LayoutInflater.from(getContext()).inflate(R.layout.editor_header, null);
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getActivity().getResources().getDimensionPixelOffset(R.dimen.editor_height));
            editor.setLayoutParams(layoutParams1);
            ImageView imageView1 = (ImageView) editor.findViewById(R.id.editor_image_1);
            Picasso.with(getActivity()).load(themeNews.getEditors().get(0).getAvatar()).into(imageView1);
            LinearLayout editorContainer = (LinearLayout) editor.findViewById(R.id.editor_image_container);
            int margin = getActivity().getResources().getDimensionPixelOffset(R.dimen.editor_img_margin);
            int size = getActivity().getResources().getDimensionPixelSize(R.dimen.editor_img_size);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(size,size);
            for(int i = 1; i < themeNews.getEditors().size(); i ++) {
                ImageView imageView2 = new ImageView(getActivity());
                layoutParams2.setMargins(margin,margin,margin,margin);
                imageView2.setLayoutParams(layoutParams2);
                Picasso.with(getActivity()).load(themeNews.getEditors().get(i).getAvatar()).into(imageView2);
                editorContainer.addView(imageView2);
            }

            newsListAdapterWrapper.addHeadView(headerImg);
            newsListAdapterWrapper.addHeadView(editor);
            listView.setAdapter(newsListAdapterWrapper);
        } else {
            newsListAdapter.addResource(themeNews);
            newsListAdapter.notifyDataSetChanged();
            newsListAdapterWrapper.notifyDataSetChanged();
        }
    }

    class NewsListAdapter extends RecyclerView.Adapter {

        private List<ThemeNews> resources = new ArrayList<>();
        private List<ThemeNews.ThemeStory> stories = new ArrayList<>();
        private Context context;

        public NewsListAdapter(Context context) {
            this.context = context;
        }

        public void addResource(ThemeNews resource) {
            //if(!resources.contains(resource))
                resources.add(resource);
            stories.addAll(resource.getStories());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            normalViewHolder.linearLayout.setTag(stories.get(position).getId());
            normalViewHolder.textView.setText(stories.get(position).getTitle());
            if(stories.get(position).getImages() != null && stories.get(position).getImages().size() > 0)
                Picasso.with(context).load(stories.get(position).getImages().get(0)).into(normalViewHolder.imageView);
            else
                normalViewHolder.imageView.setVisibility(GONE);
        }

        @Override
        public int getItemCount() {
            return stories.size();
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
//                        startNewsContent(String.valueOf(storyList.get(getAdapterPosition()-1).getId()),
//                                storyList.get(getAdapterPosition()-1).getImages().get(0));
                    }
                });
            }
        }
    }
}