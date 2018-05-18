package com.mvp.demo;

import android.app.Application;
import android.content.Context;

import com.mvp.demo.DaoManagerUtils.DaoManager;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.Logger;


/**
 * Created by luotao
 * 2018/1/18
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class InitApp extends Application {
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化全局context
        appContext = getApplicationContext();
        //初始化Logger
        Logger.addLogAdapter(new LogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                //debug状态才打印log
                return BuildConfig.DEBUG;
            }

            @Override
            public void log(int priority, String tag, String message) {

            }
        });

        //初始化数据库
        DaoManager.init();
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getAppContext() {
        return appContext;
    }
}
