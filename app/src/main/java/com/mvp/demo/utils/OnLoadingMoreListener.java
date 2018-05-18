package com.mvp.demo.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by luotao
 * 2018/3/19
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public abstract class OnLoadingMoreListener extends RecyclerView.OnScrollListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private int itemCount, lastPosition, lastItemCount;

    public abstract void onLoadMore();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mLayoutManager = recyclerView.getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            itemCount = mLayoutManager.getItemCount();
            lastPosition = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
        } else {
            Log.e("OnLoadMoreListener", "The OnLoadMoreListener only support LinearLayoutManager");
            return;
        }
        if (lastItemCount != itemCount && lastPosition == itemCount - 1) {
            lastItemCount = itemCount;
            this.onLoadMore();
        }
    }
}
