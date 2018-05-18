package com.mvp.demo.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mvp.demo.InitApp;
import com.mvp.demo.R;

/**
 * Created by luotao
 * 2018/3/9
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class SettingUtils {
    private SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(InitApp.getAppContext());

    public static SettingUtils getInstance() {
        return SettingsUtilInstance.instance;
    }

    public boolean getIsNightMode() {
        return setting.getBoolean("switch_nightMode", false);
    }

    public int getColor() {
        return R.color.viewBackground;
    }

    private static final class SettingsUtilInstance {
        private static final SettingUtils instance = new SettingUtils();
    }

    /**
     * 获取是否开启无图模式
     */
    public boolean getIsNoPhotoMode() {
        return setting.getBoolean("switch_noPhotoMode", false) && NetWorkUtil.isMobileConnected(InitApp.getAppContext());
    }

    /**
     * 获取字体大小
     */
    public int getTextSize() {
        return setting.getInt("textsize", 16);
    }

}
