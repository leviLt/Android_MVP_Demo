package com.mvp.demo.module.news.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mvp.demo.Constant;
import com.mvp.demo.DaoManagerUtils.ChannelDaoManager;
import com.mvp.demo.R;
import com.mvp.demo.entity.ChannelBean.NewsChannelBean;
import com.mvp.demo.utils.LogUtils;
import com.mvp.demo.utils.RxBus;
import com.mvp.demo.widget.BasePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author 罗涛
 */
public class NewsFragment extends Fragment {

    public static final String TAG = "NewsFragment";
    @SuppressLint("StaticFieldLeak")
    private static NewsFragment fragment;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.add_channel)
    AppCompatImageView mAddChannel;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    /*
     * butterknife in the fragment
     */
    Unbinder unbinder;
    @BindView(R.id.header_layout)
    LinearLayout mHeaderLayout;

    private BasePagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    /**
     * 存放Fragment和title  以便于改变title和Fragment的顺序
     */
    private Map<String, Fragment> map = new HashMap<>();


    private Observable<Boolean> mObservable;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        if (fragment == null) {
            fragment = new NewsFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        LogUtils.printLogError(TAG, "initView");
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mAddChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }


    @SuppressLint("CheckResult")
    private void initData() {
        //初始化Tab
        initTabs();

        mAdapter = new BasePagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(15);
        mViewPager.setAdapter(mAdapter);

        //注册刷新
        mObservable = RxBus.getInstance().register(NewsFragment.TAG);
        mObservable.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isRefresh) {
                if (isRefresh) {
                    initTabs();
                    mAdapter.recreatItem(mFragments, mTitles);
                }
            }
        });
    }

    private void initTabs() {
        if (mTitles != null) {
            mTitles.clear();
        } else {
            mTitles = new ArrayList<>();
        }
        if (mFragments != null) {
            mFragments.clear();
        } else {
            mFragments = new ArrayList<>();
        }
        List<NewsChannelBean> newsChannelBeans = ChannelDaoManager.getInstance().queryChannelEnable(Constant.NEWS_CHANNEL_ENABLE);
        if (newsChannelBeans == null || newsChannelBeans.size() <= 0) {
            ChannelDaoManager.getInstance().addInitData();
            newsChannelBeans = ChannelDaoManager.getInstance().queryChannelEnable(Constant.NEWS_CHANNEL_ENABLE);
        }
        if (newsChannelBeans.size() > 0) {
            for (NewsChannelBean bean : newsChannelBeans) {
                Fragment fragment = null;
                String channelId = bean.getChannelId();
                switch (channelId) {
                    case "essay_joke":
                        //                        if (map.containsKey(channelId)) {
                        //                            mFragments.add(map.get(channelId));
                        //                        } else {
                        //                            fragment = JokeContentView.newInstance();
                        //                            mFragments.add(fragment);
                        //                        }

                        break;
                    case "question_and_answer":
                        //                        if (map.containsKey(channelId)) {
                        //                            mFragments.add(map.get(channelId));
                        //                        } else {
                        //                            fragment = WendaArticleView.newInstance();
                        //                            mFragments.add(fragment);
                        //                        }

                        break;
                    default:
                        if (map.containsKey(channelId)) {
                            mFragments.add(map.get(channelId));
                        } else {
                            fragment = NewsArticleFragment.newInstance(channelId);
                            mFragments.add(fragment);
                        }


                        mTitles.add(bean.getChannelName());
                        if (fragment != null) {
                            map.put(channelId, fragment);
                        }
                        break;
                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        RxBus.getInstance().unregister(NewsFragment.TAG, mObservable);
        unbinder.unbind();
        if (fragment != null) {
            fragment = null;
        }
        super.onDestroyView();
    }
}
