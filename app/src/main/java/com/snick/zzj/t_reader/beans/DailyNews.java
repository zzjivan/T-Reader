package com.snick.zzj.t_reader.beans;

import java.util.List;

/**
 * Created by zzj on 17-2-6.
 */

public class DailyNews {
    private String date;
    private List<Story> stories;
    private List<TopStory> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStory> top_stories) {
        this.top_stories = top_stories;
    }

    @Override
    public boolean equals(Object obj) {
        return date.equals(obj);
    }
}
