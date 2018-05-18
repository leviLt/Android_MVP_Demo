package com.mvp.demo;

import android.support.annotation.NonNull;

import com.mvp.demo.entity.MediaChannel.MediaChannelBean;
import com.mvp.demo.entity.loadingBean.LoadingBean;
import com.mvp.demo.entity.loadingBean.LoadingEndBean;
import com.mvp.demo.interfaces.IOnItemLongClickListener;
import com.mvp.demo.module.media.model.MediaChannelViewBinder;
import com.mvp.demo.module.news.binder.NewsArticleImgViewBinder;
import com.mvp.demo.module.news.binder.NewsArticleTextViewBinder;
import com.mvp.demo.module.news.binder.NewsArticleVideoViewBinder;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;
import com.mvp.demo.module.view.LoadingEndViewBinder;
import com.mvp.demo.module.view.LoadingViewBinder;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by luotao
 * 2018/1/25
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class Register {
    /**
     * 注册新闻
     *
     * @param adapter
     */
    public static void registerNewsArticleItem(@NonNull MultiTypeAdapter adapter) {
        // 一个类型对应多个 ItemViewBinder
        adapter.register(MultiNewsArticleDataBean.class)
                .to(new NewsArticleImgViewBinder(),
                        new NewsArticleVideoViewBinder(),
                        new NewsArticleTextViewBinder())
                .withClassLinker(new ClassLinker<MultiNewsArticleDataBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<MultiNewsArticleDataBean, ?>> index(int position, @NonNull MultiNewsArticleDataBean item) {
                        if (item.isHas_video()) {
                            return NewsArticleVideoViewBinder.class;
                        }
                        if (null != item.getImage_list() && item.getImage_list().size() > 0) {
                            return NewsArticleImgViewBinder.class;
                        }
                        return NewsArticleTextViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 注册视频
     *
     * @param adapter
     */
    public static void registerVideoArticleItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(MultiNewsArticleDataBean.class, new NewsArticleVideoViewBinder());
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 注册订阅号
     *
     * @param adapter
     * @param listener
     */
    public static void registerMediaChannelItem(@NonNull MultiTypeAdapter adapter, IOnItemLongClickListener listener) {
        adapter.register(MediaChannelBean.class, new MediaChannelViewBinder(listener));
    }

}
