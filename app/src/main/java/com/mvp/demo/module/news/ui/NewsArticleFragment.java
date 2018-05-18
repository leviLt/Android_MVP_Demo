package com.mvp.demo.module.news.ui;

import android.os.Bundle;
import android.view.View;

import com.mvp.demo.Register;
import com.mvp.demo.base.BaseListFragment;
import com.mvp.demo.entity.loadingBean.LoadingBean;
import com.mvp.demo.module.news.presenter.INewsArticle;
import com.mvp.demo.module.news.presenter.NewsArticlePresenter;
import com.mvp.demo.utils.DiffCallBack;
import com.mvp.demo.utils.OnLoadingMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * @author 罗涛
 */
public class NewsArticleFragment extends BaseListFragment<INewsArticle.Presenter> implements INewsArticle.View {
    public static final String TAG = "NewsArticleFragment";
    private String categoryId;

    public NewsArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsArticleFragment.
     */
    public static NewsArticleFragment newInstance(String categoryId) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG, categoryId);
        NewsArticleFragment fragment = new NewsArticleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() throws NullPointerException {
        categoryId = getArguments().getString(TAG);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAdapter = new MultiTypeAdapter(mOldItems);
        Register.registerNewsArticleItem(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new OnLoadingMoreListener() {
            @Override
            public void onLoadMore() {
                if (isCanLoadMore) {
                    isCanLoadMore = false;
                    presenter.doLoadMoreData();
                }
            }
        });
    }

    @Override
    public void onSetAdapter(List<?> list) {
        Items items = new Items(list);
        items.add(new LoadingBean());
        DiffCallBack.create(mOldItems, items, mAdapter);
        mOldItems.clear();
        mOldItems.addAll(items);
        isCanLoadMore = true;
        /*
         * https://medium.com/@hanru.yeh/recyclerview-and-appbarlayout-behavior-changed-in-v26-0-x-d9eb4de78fc0
         * support libraries v26 增加了 RV 惯性滑动，当 root layout 使用了 AppBarLayout Behavior 就会自动生效
         * 因此需要手动停止滑动
         */
        mRecyclerView.stopScroll();
    }

    @Override
    public void setPresenter(INewsArticle.Presenter presenter) {
        if (presenter == null) {
            this.presenter = new NewsArticlePresenter(this);
        }
    }

    @Override
    public void onLoadData() {
        onShowLoading();
        this.presenter.doLoadData(categoryId);
    }

    @Override
    public void fetchData() {
        super.fetchData();
        onLoadData();
    }
}
