package com.mvp.demo.module.news.presenter;

import com.mvp.demo.base.IBaseListView;
import com.mvp.demo.base.IBasePresenter;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;

import java.util.List;

/**
 * Created by luotao
 * 2018/1/23
 * emil:luotaosc@foxmail.com
 * qq:751423471
 * @author 罗涛
 */

public interface INewsArticle {
    interface View extends IBaseListView<Presenter> {
        /**
         * 请求数据
         */
        void onLoadData();

        /**
         * 刷新
         */
        void onRefresh();
    }

    interface Presenter extends IBasePresenter {
        /**
         * 请求数据
         * @param category  频道id
         */
        void doLoadData(String... category);

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         * @param dataBeen  列表数据
         */
        void doSetAdapter(List<MultiNewsArticleDataBean> dataBeen);

        /**
         * 没有更多数据
         */
        void doShowNoMore();
    }
}
