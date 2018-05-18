package com.mvp.demo.module.news.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.mvp.demo.Constant;
import com.mvp.demo.R;
import com.mvp.demo.base.BaseFragment;
import com.mvp.demo.module.news.model.MultiNewsArticleDataBean;
import com.mvp.demo.module.news.presenter.INewsContent;
import com.mvp.demo.module.news.presenter.NewsContentPresenter;
import com.mvp.demo.utils.ImageLoader;
import com.mvp.demo.utils.SettingUtils;
import com.mvp.demo.widget.helper.AppBarStateChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NewsContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsContentFragment extends BaseFragment<INewsContent.Presenter> implements INewsContent.View {
    private static final String DATA_BEAN = "dataBean";
    private static final String IMG_URL = "imgUrl";
    // 新闻链接 标题 头条号 文章号 媒体名
    private String shareUrl;
    private String shareTitle;
    private String mediaUrl;
    private String mediaId;
    private String mediaName;
    private String imgUrl;
    private boolean isHasImage;
    private MultiNewsArticleDataBean bean;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.pb_progress)
    ContentLoadingProgressBar mPbProgress;
    @BindView(R.id.news_content_main)
    CoordinatorLayout mNewsContentMain;
    Unbinder unbinder;
    @BindView(R.id.iv_image)
    ImageView mIvImage;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;


    public NewsContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param databean Parameter 1.
     * @return A new instance of fragment NewsContentFragment.
     */
    public static NewsContentFragment newInstance(Parcelable databean, String url) {
        NewsContentFragment fragment = new NewsContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(DATA_BEAN, databean);
        args.putString(IMG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int attachLayoutId() {
        if (getArguments() != null) {
            bean = getArguments().getParcelable(DATA_BEAN);
            imgUrl = getArguments().getString(IMG_URL);
        }
        return TextUtils.isEmpty(imgUrl) ? R.layout.fragment_news_content_text : R.layout.fragment_news_content_img;
    }

    @Override
    protected void initView(View view) {
        //butterKnife 绑定view
        unbinder = ButterKnife.bind(this, view);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebClient() {
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        // 缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        settings.setBuiltInZoomControls(false);
        // 缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启DOM storage API功能
        settings.setDomStorageEnabled(true);
        // 开启application Cache功能
        settings.setAppCacheEnabled(true);
        // 判断是否为无图模式
        settings.setBlockNetworkImage(SettingUtils.getInstance().getIsNoPhotoMode());
        // 不调用第三方浏览器即可进行页面反应
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                onHideLoading();
                super.onPageFinished(view, url);
            }
        });

        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
                    mWebview.goBack();
                    return true;
                }
                return false;
            }
        });

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 90) {
                    onHideLoading();
                } else {
                    onShowLoading();
                }
            }
        });
    }

    @Override
    protected void initData() throws NullPointerException {
        try {
            //            Log.d(TAG, "initData: " + bean.toString());
            presenter.onLoadData(bean);
            shareUrl = !TextUtils.isEmpty(bean.getShare_url()) ? bean.getShare_url() : bean.getDisplay_url();
            shareTitle = bean.getTitle();
            mediaName = bean.getMedia_name();
            mediaUrl = "http://toutiao.com/m" + bean.getMedia_info().getMedia_id();
            mediaId = bean.getMedia_info().getMedia_id();
        } catch (Exception e) {
//            ErrorAction.print(e);
        }

        if (isHasImage) {
            ImageLoader.loadCenterCrop(getActivity(), getArguments().getString(IMG_URL), mIvImage, R.mipmap.error_image, R.mipmap.error_image);
            mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                @Override
                public void onStateChanged(AppBarLayout appBarLayout, State state) {
                    if (state == State.EXPANDED) {
                        // 展开状态
                        mCollapsingToolbar.setTitle("");
                        mToolbar.setBackgroundColor(Color.TRANSPARENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        }
                    } else if (state == State.COLLAPSED) {
                        // 折叠状态

                    } else {
                        // 中间状态
                        mCollapsingToolbar.setTitle(mediaName);
                        mToolbar.setBackgroundColor(SettingUtils.getInstance().getColor());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        }
                    }
                }
            });
        } else {
            mToolbar.setTitle(mediaName);
        }
    }

    @Override
    public void onSetWebView(String url, boolean flag) {
        // 是否为头条的网站
        if (flag) {
            mWebview.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        } else {
            /*
               ScrollView 嵌套 WebView, 导致部分网页无法正常加载
               如:https://temai.snssdk.com/article/feed/index/?id=11754971
               最佳做法是去掉 ScrollView, 或使用 NestedScrollWebView
             */
            if (shareUrl.contains("temai.snssdk.com")) {
                mWebview.getSettings().setUserAgentString(Constant.USER_AGENT_PC);
            }
            mWebview.loadUrl(shareUrl);
        }
    }

    @Override
    public void onShowLoading() {
        mPbProgress.show();
    }

    @Override
    public void onHideLoading() {
        mPbProgress.hide();
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onShowNetError() {
        Snackbar.make(mScrollView, R.string.network_error, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void setPresenter(INewsContent.Presenter presenter) {
        if (presenter == null) {
            this.presenter = new NewsContentPresenter(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
