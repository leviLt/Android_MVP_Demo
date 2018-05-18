package com.mvp.demo.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by luotao
 * 2018/1/25
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class NewsArticleTextViewBinder extends ItemViewBinder<MultiNewsArticleDataBean,NewsArticleTextViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return null;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MultiNewsArticleDataBean item) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
