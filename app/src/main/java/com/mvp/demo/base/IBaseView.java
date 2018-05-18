package com.mvp.demo.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * @author Scorpio
 * @date 2018/1/18
 * QQ:751423471
 * phone:13982250340
 */

public interface IBaseView<T> {
    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载动画
     */
    void onHideLoading();

    /**
     * 显示网络加载失败
     */
    void onShowNetError();

    /**
     * 设置presenter
     *
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

}
