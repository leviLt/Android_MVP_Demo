package com.mvp.demo.module.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvp.demo.R;
import com.mvp.demo.entity.loadingBean.LoadingBean;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by luotao
 * 2018/3/19
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class LoadingViewBinder extends ItemViewBinder<LoadingBean, LoadingViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_loading, parent, false);
//        LottieAnimationView lottieAnimationView = view.findViewById(R.id.animation_view);
//        RecyclerView recyclerView = (RecyclerView) parent;
//        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        int itemCount = layoutManager.getItemCount();
//        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//        //是否是最后一个显示
//        if (lastVisibleItemPosition == itemCount) {
//            if (!lottieAnimationView.isAnimating()) {
//                lottieAnimationView.playAnimation();
//            }
//        } else {
//            lottieAnimationView.cancelAnimation();
//        }
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LoadingBean item) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
