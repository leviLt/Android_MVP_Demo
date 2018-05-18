package com.mvp.demo.base;

/**
 * Created by Scorpio on 2018/1/18.
 * QQ:751423471
 * phone:13982250340
 */

public interface IBasePresenter {
    /**
     * 刷新数据
     */
    void doRefresh();

    /**
     * 显示网络错误
     */
    void doShowNetError();
}
