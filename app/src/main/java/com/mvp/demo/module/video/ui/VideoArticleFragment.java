package com.mvp.demo.module.video.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link VideoArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author 罗涛
 */
public class VideoArticleFragment extends BaseListFragment<INewsArticle.Presenter> implements INewsArticle.View {
    private static final String ARG_PARAM1 = "param1";
    private String categoryId;

    public VideoArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryId Parameter 1.
     * @return A new instance of fragment VideoArticleFragment.
     */
    public static VideoArticleFragment newInstance(String categoryId) {
        VideoArticleFragment fragment = new VideoArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() throws NullPointerException {
        if (getArguments() != null) {
            categoryId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAdapter = new MultiTypeAdapter(mOldItems);
        Register.registerVideoArticleItem(mAdapter);
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
    public void fetchData() {
        super.fetchData();
        onLoadData();
    }

    @Override
    public void onLoadData() {
        onShowLoading();
        presenter.doLoadData(categoryId);
    }

    @Override
    public void onSetAdapter(final List<?> list) {
        Items items = new Items(list);
        items.add(new LoadingBean());
        DiffCallBack.create(mOldItems, items, mAdapter);
        mOldItems.clear();
        mOldItems.addAll(items);
        isCanLoadMore = true;
        mRecyclerView.stopScroll();
    }

    /**
     * API 跟新闻的一样 所以采用新闻的 presenter
     * @param presenter
     */
    @Override
    public void setPresenter(INewsArticle.Presenter presenter) {
        if (presenter == null) {
            this.presenter = new NewsArticlePresenter(this);
        }
    }
}
