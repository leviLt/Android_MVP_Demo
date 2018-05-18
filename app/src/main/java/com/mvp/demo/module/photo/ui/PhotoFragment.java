package com.mvp.demo.module.photo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvp.demo.InitApp;
import com.mvp.demo.R;
import com.mvp.demo.widget.BasePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author 罗涛
 */
public class PhotoFragment extends Fragment {
    private static PhotoFragment instance;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    Unbinder unbinder;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    /**
     * 页面个数
     */
    public static final int pageSize = InitApp.getAppContext().getResources().getStringArray(R.array.photo_id).length;
    private String categoryId[] = InitApp.getAppContext().getResources().getStringArray(R.array.photo_id);
    private String categoryName[] = InitApp.getAppContext().getResources().getStringArray(R.array.photo_name);

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhotoFragment.
     */
    public static PhotoFragment newInstance() {
        if (instance == null) {
            instance = new PhotoFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    /**
     * 初始化photoFragment的View
     */
    private void initViews() {
        initTabs();
        mViewPager.setOffscreenPageLimit(pageSize);
        mViewPager.setCurrentItem(0);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        BasePagerAdapter adapter = new BasePagerAdapter(fragmentManager, mFragments, mTitles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 初始化fragment、tab
     */
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
        for (int i = 0; i < categoryId.length; i++) {
            mFragments.add(PhotoArticleFragment.newInstance(categoryId[i]));
            mTitles.add(categoryName[i]);
        }
    }

    /**
     * 初始化页面数据
     */
    private void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
