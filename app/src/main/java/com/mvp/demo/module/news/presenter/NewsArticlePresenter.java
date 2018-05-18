package com.mvp.demo.module.news.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mvp.demo.InitApp;
import com.mvp.demo.api.IMobileNewsApi;
import com.mvp.demo.module.news.model.MultiNewsArticleBean;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;
import com.mvp.demo.retrofit.RetrofitFactory;
import com.mvp.demo.utils.NetWorkUtil;
import com.mvp.demo.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luotao
 * 2018/1/24
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class NewsArticlePresenter implements INewsArticle.Presenter {
    private static final String TAG = "NewsArticlePresenter";
    private static final int MAX_VALUE = 150;
    private INewsArticle.View mView;
    private String time;
    private String category;
    private Gson mGson = new Gson();
    private Random mRandom = new Random();

    private List<MultiNewsArticleDataBean> dataList = new ArrayList<>();

    public NewsArticlePresenter(INewsArticle.View view) {
        this.mView = view;
        this.time = TimeUtils.getCurrentTimeStamp();
    }

    @Override
    public void doRefresh() {
        if (dataList.size() != 0) {
            dataList.clear();
            time = TimeUtils.getCurrentTimeStamp();
        }
        mView.onShowLoading();
        doLoadData();
    }

    @Override
    public void doShowNetError() {
        mView.onHideLoading();
        mView.onShowNetError();
    }

    @Override
    public void doLoadData(String... category) {
        try {
            if (this.category == null) {
                this.category = category[0];
            }
        } catch (Exception e) {

        }
        // 释放内存
        if (dataList.size() >= MAX_VALUE) {
            dataList.clear();
        }
        if (NetWorkUtil.isWifiProxy(InitApp.appContext)) {
            doShowNetError();
            return;
        }
        getRandom().subscribeOn(Schedulers.io())
                .switchMap(new Function<MultiNewsArticleBean, ObservableSource<MultiNewsArticleDataBean>>() {
                    @Override
                    public ObservableSource<MultiNewsArticleDataBean> apply(MultiNewsArticleBean multiNewsArticleBean) throws Exception {
                        List<MultiNewsArticleDataBean> dataBeanList = new ArrayList<>();
                        for (MultiNewsArticleBean.DataBean dataBean : multiNewsArticleBean.getData()) {
                            dataBeanList.add(mGson.fromJson(dataBean.getContent(), MultiNewsArticleDataBean.class));
                        }
                        return Observable.fromIterable(dataBeanList);
                    }
                })
                .filter(new Predicate<MultiNewsArticleDataBean>() {
                    @Override
                    public boolean test(MultiNewsArticleDataBean dataBean) throws Exception {
                        time = dataBean.getBehot_time();
                        if (TextUtils.isEmpty(dataBean.getSource())) {
                            return false;
                        }
                        try {
                            // 过滤头条问答新闻
                            if (dataBean.getSource().contains("头条问答")
                                    || dataBean.getTag().contains("ad")
                                    || dataBean.getSource().contains("悟空问答")) {
                                return false;
                            }
                            // 过滤头条问答新闻
                            if (dataBean.getRead_count() == 0 || TextUtils.isEmpty(dataBean.getMedia_name())) {
                                String title = dataBean.getTitle();
                                if (title.lastIndexOf("？") == title.length() - 1) {
                                    return false;
                                }
                            }
                        } catch (NullPointerException e) {

                        }
                        // 过滤重复新闻(与上次刷新的数据比较)
                        for (MultiNewsArticleDataBean bean : dataList) {
                            if (bean.getTitle().equals(dataBean.getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .map(new Function<List<MultiNewsArticleDataBean>, List<MultiNewsArticleDataBean>>() {
                    @Override
                    public List<MultiNewsArticleDataBean> apply(List<MultiNewsArticleDataBean> list) throws Exception {
                        // 过滤重复新闻(与本次刷新的数据比较,因为使用了2个请求,数据会有重复)
                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int j = list.size() - 1; j > i; j--) {
                                if (list.get(j).getTitle().equals(list.get(i).getTitle())) {
                                    list.remove(j);
                                }
                            }
                        }
                        return list;
                    }
                })
                .compose(mView.<List<MultiNewsArticleDataBean>>bindToLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MultiNewsArticleDataBean>>() {
                    @Override
                    public void accept(List<MultiNewsArticleDataBean> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            doSetAdapter(list);
                        } else {
                            doShowNoMore();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        doShowNetError();
                    }
                });
    }


    @Override
    public void doLoadMoreData() {
        doLoadData();
    }

    @Override
    public void doSetAdapter(List<MultiNewsArticleDataBean> dataBeen) {
        dataList.addAll(dataBeen);
        mView.onHideLoading();
        mView.onSetAdapter(dataList);
    }

    @Override
    public void doShowNoMore() {
        mView.onHideLoading();
        mView.onShowNoMore();
    }

    /**
     * 获取随机文章
     *
     * @return
     */
    private Observable<MultiNewsArticleBean> getRandom() {
        int i = mRandom.nextInt(10);
        if (i % 2 == 0) {
            return RetrofitFactory.getRetrofit().create(IMobileNewsApi.class)
                    .getNewsArticle(this.category, this.time);
        } else {
            return RetrofitFactory.getRetrofit().create(IMobileNewsApi.class)
                    .getNewsArticle2(this.category, this.time);
        }
    }
}
