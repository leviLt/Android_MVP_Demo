package com.mvp.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luotao
 * 2018/1/24
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class TimeUtils {
    /**
     * string 转换成 date
     *
     * @param time
     * @return
     */
    public static Date stringConvertDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据时间撮 返回发布的时间距离现在的时间
     *
     * @param timeStamp
     * @return
     */
    public static String getTimesStampAgo(String timeStamp) {
        long time = Long.parseLong(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String format = sdf.format(time * 1000L);
        Date date = null;
        try {
            date = sdf.parse(format);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getTimeAgo(date);
    }

    /**
     * 返回发布时间距离当前时间
     *
     * @return
     */
    public static String getTimeAgo(Date creatTime) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        if (creatTime != null) {
            long agoTimeInMin = ((new Date(System.currentTimeMillis())).getTime() - creatTime.getTime()) / 1000 / 60;
            if (agoTimeInMin <= 1) {
                return "刚刚";
            } else if (agoTimeInMin <= 60) {
                return agoTimeInMin + "分钟之前";
            } else if (agoTimeInMin <= 24 * 60) {
                return agoTimeInMin / 60 + "小时前";
            } else if (agoTimeInMin <= 60 * 24 * 2) {
                return agoTimeInMin / (60 * 24) + "天前";
            } else {
                return format.format(creatTime);
            }
        } else {
            return format.format(new Date(0));
        }
    }

    /**
     * 获取当前时间撮
     *
     * @return
     */
    public static String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}
