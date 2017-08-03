package com.snick.zzj.t_reader.utils;

/**
 * Created by zzj on 17-2-6.
 */

public class SourceUrl {
    public static final String DateFormat = "yyyyMMdd";

    public static final String NEWS_ID = "news_id";
    public static final String NEWS_HEADER_IMG_ID = "1000";

    /** api资源查询
     * https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90
     */

    public static final String welcomeImage = "http://news-at.zhihu.com/api/4/start-image/";
    public static final String version = "http://news-at.zhihu.com/api/4/version/android/2.3.0/";
    public static final String News = "http://news-at.zhihu.com/api/4/news/";//后接latest或者具体id
    public static final String oldNews = "http://news-at.zhihu.com/api/4/news/before/";//后接日期20170218
    public static final String extraInfo = "http://news-at.zhihu.com/api/4/story-extra/"; //后接具体id

    public static final String newsThemeList = "https://news-at.zhihu.com/api/4/themes";
}
