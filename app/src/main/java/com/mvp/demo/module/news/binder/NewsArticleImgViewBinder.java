package com.mvp.demo.module.news.binder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.mvp.demo.IntentAction;
import com.mvp.demo.R;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;
import com.mvp.demo.module.news.ui.NewsContentActivity;
import com.mvp.demo.utils.ImageLoader;
import com.mvp.demo.utils.LogUtils;
import com.mvp.demo.utils.SettingUtils;
import com.mvp.demo.utils.TimeUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by luotao
 * 2018/1/25
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class NewsArticleImgViewBinder extends ItemViewBinder<MultiNewsArticleDataBean, NewsArticleImgViewBinder.ViewHolder> {
    public static final String TAG = NewsArticleImgViewBinder.class.getName();

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_news_article_img_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final MultiNewsArticleDataBean item) {
        final Context context = holder.itemView.getContext();
        try {
            String imgUrl = "http://p3.pstatp.com/";
            final List<MultiNewsArticleDataBean.ImageListBean> imageList = item.getImage_list();
            if (imageList != null && imageList.size() != 0) {
                String url = imageList.get(0).getUrl();
                ImageLoader.loadCenterCrop(context, url, holder.mIvDesc, R.color.viewBackground);
                if (!TextUtils.isEmpty(imageList.get(0).getUri())) {
                    imgUrl += imageList.get(0).getUri().replace("list", "large");
                }
            }

            if (item.getUser_info() != null) {
                String avatarUrl = item.getUser_info().getAvatar_url();
                ImageLoader.loadCenterCrop(context, avatarUrl, holder.mCivMedia, R.color.viewBackground);
            }
            String title = item.getTitle();
            String tvAbstract = item.getAbstractX();
            String tvSource = item.getSource();
            String tvConmentCount = item.getComment_count() + "评论";
            String tvDateTime = item.getBehot_time() + "";

            if (!TextUtils.isEmpty(tvDateTime)) {
                tvDateTime = TimeUtils.getTimesStampAgo(tvDateTime);
            }

            holder.mTvTitle.setText(title);
            holder.mTvTitle.setTextSize(SettingUtils.getInstance().getTextSize());
            holder.mTvDesc.setText(tvAbstract);
            holder.mTvPublishInfo.setText(tvSource + "-" + tvConmentCount + "-" + tvDateTime);
            holder.mIvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.mIvMore, Gravity.END, 0, R.style.MyPopupMenu);
                    popupMenu.inflate(R.menu.menu_share);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            if (itemId == R.id.action_share) {
                                IntentAction.send(context, item.getTitle() + "\n" + item.getShare_url());
                            }
                            return false;
                        }
                    });
                }
            });
            final String finalImgUrl = imgUrl;
            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            //进入详情页
                            NewsContentActivity.launch(item, finalImgUrl);
                        }
                    });

        } catch (Exception e) {
            LogUtils.printLogError(TAG, e.getMessage());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.civ_media)
        CircleImageView mCivMedia;
        //        @BindView(R.id.tv_publish_info)
        AppCompatTextView mTvPublishInfo;
        //        @BindView(R.id.iv_more)
        AppCompatImageView mIvMore;
        //        @BindView(R.id.iv_desc)
        AppCompatImageView mIvDesc;
        //        @BindView(R.id.tv_title)
        AppCompatTextView mTvTitle;
        //        @BindView(R.id.tv_desc)
        AppCompatTextView mTvDesc;

        ViewHolder(View itemView) {
            super(itemView);
            //            ButterKnife.bind(itemView);
            mCivMedia = itemView.findViewById(R.id.civ_media);
            mTvPublishInfo = itemView.findViewById(R.id.tv_publish_info);
            mIvMore = itemView.findViewById(R.id.iv_more);
            mIvDesc = itemView.findViewById(R.id.iv_desc);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
        }
    }
}
