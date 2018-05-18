package com.mvp.demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Scorpio on 2018/1/18.
 * QQ:751423471
 * phone:13982250340
 */

public abstract class LazyLoadFragment<T extends IBasePresenter> extends BaseFragment<T> {
    /**
     * 是否是初始化view
     */
    protected boolean isViewInitiated;
    /**
     * 是否在对用户显示
     */
    protected boolean isVisibleToUser;
    /**
     * 数据是否初始化
     */
    protected boolean isDataInitiated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    /**
     * 获取数据
     */
    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    /**
     * 准备抓取数据
     *
     * @param isForceUpdata
     * @return
     */
    public boolean prepareFetchData(boolean isForceUpdata) {
        if (isViewInitiated && isVisibleToUser && (!isDataInitiated || isForceUpdata)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }
}
