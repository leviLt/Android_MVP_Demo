package com.mvp.demo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.mvp.demo.InitApp;
import com.orhanobut.logger.Logger;


/**
 * Created by luotao
 * 2018/3/2
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class LogUtils {
    /**
     * 打印错误信息
     *
     * @param tag
     * @param content
     */
    public static void printLogError(String tag, String content) {
        if (getApkIsDebug(InitApp.getAppContext())) {
            Logger.e(tag, content);
        }
        Logger.e(content);
    }

    /**
     * 判断是否是debug模式
     *
     * @param context
     * @return
     */
    private static boolean getApkIsDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
