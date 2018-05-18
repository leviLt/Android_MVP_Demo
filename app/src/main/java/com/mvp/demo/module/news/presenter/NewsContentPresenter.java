package com.mvp.demo.module.news.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.mvp.demo.api.INewsApi;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;
import com.mvp.demo.module.news.model.NewsContentBean;
import com.mvp.demo.retrofit.RetrofitFactory;
import com.mvp.demo.utils.SettingUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by luotao
 * 2018/4/28
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */
public class NewsContentPresenter implements INewsContent.Presenter {
    private static final String TAG = "NewsContentPresenter";
    private INewsContent.View view;
    private String groupId;
    private String itemId;

    public NewsContentPresenter(INewsContent.View view) {
        this.view = view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onLoadData(MultiNewsArticleDataBean dataBean) {
        groupId = dataBean.getGroup_id() + "";
        itemId = dataBean.getItem_id() + "";
        final String url = dataBean.getDisplay_url();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try {
                    Response<ResponseBody> response = RetrofitFactory.getRetrofit().
                            create(INewsApi.class).getNewsContentRedirectUrl(url).execute();
                    // 获取重定向后的 URL 用于拼凑API
                    if (response.isSuccessful()) {
                        String httpUrl = response.raw().request().url().toString();
                        if (!TextUtils.isEmpty(httpUrl) && httpUrl.contains("toutiao")) {
                            String api = httpUrl + "info/";
                            e.onNext(api);
                        } else {
                            e.onError(new Throwable());
                        }
                    } else {
                        e.onError(new Throwable());
                    }
                } catch (Exception ex) {
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .switchMap(new Function<String, ObservableSource<NewsContentBean>>() {
                    @Override
                    public ObservableSource<NewsContentBean> apply(String s) throws Exception {
                        return RetrofitFactory.getRetrofit().create(INewsApi.class).getNewsContent(s);
                    }
                })
                .map(new Function<NewsContentBean, String>() {
                    @Override
                    public String apply(NewsContentBean newsContentBean) throws Exception {
                        return getHTML(newsContentBean);
                    }
                })
                .compose(view.<String>bindToLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        view.onSetWebView(s, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onSetWebView(null, false);
                    }

                    @Override
                    public void onComplete() {
                        doShowNetError();
                    }
                });
    }

    @Override
    public void doRefresh() {

    }

    @Override
    public void doShowNetError() {
        view.onHideLoading();
        view.onShowNetError();
    }


    private String getHTML(NewsContentBean bean) {
        String title = bean.getData().getTitle();
        String content = bean.getData().getContent();
        if (content != null) {

            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/toutiao_light.css\" type=\"text/css\">";
            if (SettingUtils.getInstance().getIsNightMode()) {
                css = css.replace("toutiao_light", "toutiao_dark");
            }

            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">" +
                    css +
                    "<body>\n" +
                    "<article class=\"article-container\">\n" +
                    "    <div class=\"article__content article-content\">" +
                    "<h1 class=\"article-title\">" +
                    title +
                    "</h1>" +
                    content +
                    "    </div>\n" +
                    "</article>\n" +
                    "</body>\n" +
                    "</html>";

            return html;
        } else {
            return null;
        }
    }
}
