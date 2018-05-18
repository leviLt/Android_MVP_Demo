package com.mvp.demo.module.news.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mvp.demo.Constant;
import com.mvp.demo.DaoManagerUtils.ChannelDaoManager;
import com.mvp.demo.R;
import com.mvp.demo.adapter.NewsChannelAdapter;
import com.mvp.demo.base.BaseActivity;
import com.mvp.demo.entity.ChannelBean.NewsChannelBean;
import com.mvp.demo.utils.RxBus;
import com.mvp.demo.utils.ToastUtils;
import com.mvp.demo.widget.helper.ItemDragHelperCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 罗涛
 */
public class ChannelActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mMRecyclerView;
    private NewsChannelAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化列表数据
     */
    private void initData() {
        List<NewsChannelBean> enableItems = ChannelDaoManager.getInstance().queryChannelEnable(Constant.NEWS_CHANNEL_ENABLE);
        List<NewsChannelBean> disableItems = ChannelDaoManager.getInstance().queryChannelEnable(Constant.NEWS_CHANNEL_DISABLE);
        final GridLayoutManager manager = new GridLayoutManager(this, 4);
        mMRecyclerView.setLayoutManager(manager);

        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack();
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mMRecyclerView);

        mAdapter = new NewsChannelAdapter(this, helper, enableItems, disableItems);
        mAdapter.setOnChannelItemClickListener(new NewsChannelAdapter.OnChannelItemClickListener() {
            @Override
            public void onChannelItemClick(View v, int position) {
                ToastUtils.showToast(mContext, position + "");
            }
        });

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                return itemViewType == NewsChannelAdapter.TYPE_MY || itemViewType == NewsChannelAdapter.TYPE_OTHER
                        ? 1 : 4;
            }
        });

        mMRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化ToolBar
     */
    private void initView() {
        initToolbar(mToolbar, getString(R.string.title_item_drag), true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onSaveData();
    }

    /**
     * 保存channel的改变
     */
    @SuppressLint("CheckResult")
    private void onSaveData() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                List<NewsChannelBean> oldItems = ChannelDaoManager.getInstance().queryChannelEnable(Constant.NEWS_CHANNEL_ENABLE);
                e.onNext(!compare(oldItems, mAdapter.getMyChannelItems()));
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            List<NewsChannelBean> enableItems = mAdapter.getMyChannelItems();
                            List<NewsChannelBean> disableItems = mAdapter.getOtnerChannelItems();
                            ChannelDaoManager.getInstance().deleteAll(NewsChannelBean.class);
                            for (int i = 0; i < enableItems.size(); i++) {
                                NewsChannelBean bean = enableItems.get(i);
                                bean.setIsEnable(Constant.NEWS_CHANNEL_ENABLE);
                                bean.setId((long) i);
                                ChannelDaoManager.getInstance().insertObject(bean);
                            }
                            for (int i = 0; i < disableItems.size(); i++) {
                                NewsChannelBean bean = disableItems.get(i);
                                bean.setIsEnable(Constant.NEWS_CHANNEL_DISABLE);
                                bean.setId((long) (enableItems.size() + i));
                                ChannelDaoManager.getInstance().insertObject(bean);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isRefresh) throws Exception {
                        RxBus.getInstance().post(NewsFragment.TAG, isRefresh);
                    }
                });
    }

    /**
     * 比较两个集合是否相等
     *
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public synchronized <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }
}
