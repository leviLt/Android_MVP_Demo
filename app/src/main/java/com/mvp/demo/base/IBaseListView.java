package com.mvp.demo.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

/**
 * Created by luotao
 * 2018/1/19
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public interface IBaseListView<T> extends IBaseView<T> {
    /**
     * 显示加载动画
     */
    @Override
    void onShowLoading();

    /**
     * 隐藏加载
     */
    @Override
    void onHideLoading();

    /**
     * 显示网络错误
     */
    @Override
    void onShowNetError();

    /**
     * 设置 presenter
     */
    @Override
    void setPresenter(T presenter);

    /**
     * 绑定生命周期
     */
    @Override
    <T> LifecycleTransformer<T> bindToLife();

    /**
     * 设置适配器
     */
    void onSetAdapter(List<?> list);

    /**
     * 加载完毕
     */
    void onShowNoMore();
}
