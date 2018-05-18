package com.mvp.demo.module.video.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvp.demo.InitApp;
import com.mvp.demo.R;
import com.mvp.demo.widget.BasePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author 罗涛
 */
public class VideosFragment extends Fragment {

    private static VideosFragment instance;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    Unbinder unbinder;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    /**
     * 频道id
     */
    private String categoryId[] = InitApp.getAppContext().getResources().getStringArray(R.array.mobile_video_id);
    /**
     * 频道name
     */
    private String categoryName[] = InitApp.getAppContext().getResources().getStringArray(R.array.mobile_video_name);

    @SuppressLint("ValidFragment")
    private VideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VideosFragment.
     */
    public static VideosFragment newInstance() {
        if (instance == null) {
            instance = new VideosFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        initTabs();
        BasePagerAdapter adapter = new BasePagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(categoryId.length);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initTabs() {
        if (mFragments != null) {
            mFragments.clear();
        } else {
            mFragments = new ArrayList<>();
        }

        if (mTitles != null) {
            mTitles.clear();
        } else {
            mTitles = new ArrayList<>();
        }
        mTitles.addAll(Arrays.asList(categoryName));
        for (String aCategoryId : categoryId) {
            mFragments.add(VideoArticleFragment.newInstance(aCategoryId));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
