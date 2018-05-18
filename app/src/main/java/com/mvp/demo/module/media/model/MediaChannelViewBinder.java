package com.mvp.demo.module.media.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mvp.demo.R;
import com.mvp.demo.entity.MediaChannel.MediaChannelBean;
import com.mvp.demo.interfaces.IOnItemLongClickListener;
import com.mvp.demo.utils.ImageLoader;
import com.mvp.demo.utils.LogUtils;
import com.mvp.demo.widget.CircleImageView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by luotao
 * 2018/4/26
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */
public class MediaChannelViewBinder extends ItemViewBinder<MediaChannelBean, MediaChannelViewBinder.ViewHolder> {
    public static final String TAG = MediaChannelViewBinder.class.getName();
    private IOnItemLongClickListener listener;

    public MediaChannelViewBinder(IOnItemLongClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_media_channel, parent, false);
        return new ViewHolder(view, listener);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MediaChannelBean item) {
        try {
            final Context context = holder.itemView.getContext();
            String url = item.getAvatar();
            ImageLoader.loadCenterCrop(context, url, holder.mCvAvatar, R.color.viewBackground);
            holder.mTvMediaName.setText(item.getName());
            holder.mTvDescText.setText(item.getDescText());

            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                            //MediaHomeActivity.launch(item.getChannelId());
                        }
                    });
        } catch (Exception e) {
            LogUtils.printLogError(TAG, e.getMessage());
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private IOnItemLongClickListener listener;
        CircleImageView mCvAvatar;
        TextView mTvMediaName;
        TextView mTvFollowCount;
        TextView mTvDescText;
        LinearLayout mContent;
        View mDivider;

        ViewHolder(View itemView, IOnItemLongClickListener listener) {
            super(itemView);
            mCvAvatar = itemView.findViewById(R.id.cv_avatar);
            mTvMediaName = itemView.findViewById(R.id.tv_mediaName);
            mTvFollowCount = itemView.findViewById(R.id.tv_followCount);
            mTvDescText = itemView.findViewById(R.id.tv_descText);
            mContent = itemView.findViewById(R.id.content);
            mDivider = itemView.findViewById(R.id.divider);
            itemView.setOnLongClickListener(this);
            this.listener = listener;
        }

        @Override
        public boolean onLongClick(View v) {
            if (this.listener != null) {
                this.listener.onLongClick(v, getAdapterPosition());
                return true;
            }
            return false;
        }
    }
}
