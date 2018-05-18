package com.mvp.demo.module.news.presenter;

import com.mvp.demo.base.IBasePresenter;
import com.mvp.demo.base.IBaseView;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;

/**
 * Created by luotao
 * 2018/4/28
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */
public interface INewsContent {
    interface Presenter extends IBasePresenter {
        /**
         * 请求数据
         *
         * @param dataBean 数据
         */
        void onLoadData(MultiNewsArticleDataBean dataBean);
    }

    interface View extends IBaseView<Presenter> {
        /**
         * 加载webView
         *
         * @param url  网页链接
         * @param flag 标识
         */
        void onSetWebView(String url, boolean flag);
    }
}
