package com.mvp.demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by Scorpio on 2018/1/18.
 * QQ:751423471
 * phone:13982250340
 */

public abstract class BaseFragment<T extends IBasePresenter> extends RxFragment implements IBaseView<T> {
    protected T presenter;
    protected BaseActivity mContext;

    /**
     * 绑定布局ID
     *
     * @return 布局id
     */
    protected abstract int attachLayoutId();

    /**
     * 初始化视图控件
     *
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 初始化数据
     *
     * @throws NullPointerException
     */
    protected abstract void initData() throws NullPointerException;

    /**
     * 初始化toolbar
     *
     * @param toolbar
     * @param title
     * @param homeAsUpEnabled
     */
    protected void initToolbar(Toolbar toolbar, String title, boolean homeAsUpEnabled) {
        ((BaseActivity) getActivity()).initToolbar(toolbar, title, homeAsUpEnabled);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (BaseActivity) getActivity();
        setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(attachLayoutId(), container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return bindUntilEvent(FragmentEvent.DESTROY);
    }
}
