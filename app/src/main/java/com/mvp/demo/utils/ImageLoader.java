package com.mvp.demo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

/**
 * Created by luotao
 * 2018/3/9
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class ImageLoader {
    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param defaultResId
     */
    public static void loadCenterCrop(Context context, String url, ImageView imageView, int defaultResId) {
        if (SettingUtils.getInstance().getIsNoPhotoMode() && NetWorkUtil.isMobileConnected(context)) {
            imageView.setImageResource(defaultResId);
        } else {
            Glide.with(context).load(url).crossFade().centerCrop().into(imageView);
        }
    }

    /**
     * 加载带异常图片处理
     *
     * @param context
     * @param url
     * @param imageView
     * @param defaultResId
     * @param errorResId
     */
    public static void loadCenterCrop(Context context, String url, ImageView imageView, int defaultResId, int errorResId) {
        if (SettingUtils.getInstance().getIsNoPhotoMode() && NetWorkUtil.isMobileConnected(context)) {
            imageView.setImageResource(defaultResId);
        } else {
            Glide.with(context).load(url).crossFade().centerCrop().error(errorResId).into(imageView);
        }
    }
    /**
     * 带监听处理
     */
    public static void loadCenterCrop(Context context, String url, ImageView view, RequestListener listener) {
        Glide.with(context).load(url).crossFade().centerCrop().listener(listener).into(view);
    }
}
