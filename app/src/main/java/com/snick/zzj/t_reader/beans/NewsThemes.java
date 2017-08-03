package com.snick.zzj.t_reader.beans;

import java.util.List;

/**
 * Created by zzj on 17-8-3.
 */

public class NewsThemes {
    private int limit;
    private List<String> subscribed;
    private List<Detail> others;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<String> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<String> subscribed) {
        this.subscribed = subscribed;
    }

    public List<Detail> getOthers() {
        return others;
    }

    public void setOthers(List<Detail> others) {
        this.others = others;
    }

    class Detail {
        private long color;
        private String thumbnail;
        private String description;
        private int id;
        private String name;

        public long getColor() {
            return color;
        }

        public void setColor(long color) {
            this.color = color;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
