package com.mvp.demo.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by luotao
 * 2018/1/23
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class BasePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public BasePagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> mTitles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        if (mFragments != null && mTitles != null) {
            return mFragments.size();
        }
        return 0;
    }

    public List<String> getTitles() {
        return mTitles;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    /**
     * 重新构建
     *
     * @param mFragments
     * @param mTitles
     */
    public void recreatItem(List<Fragment> mFragments, List<String> mTitles) {
        this.mTitles = mTitles;
        this.mFragments = mFragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
