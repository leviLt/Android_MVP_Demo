package com.mvp.demo.module.media.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mvp.demo.DaoManagerUtils.MediaChannelDaoManager;
import com.mvp.demo.R;
import com.mvp.demo.Register;
import com.mvp.demo.entity.MediaChannel.MediaChannelBean;
import com.mvp.demo.interfaces.IOnItemLongClickListener;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MediaChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author 罗涛
 */
public class MediaChannelFragment extends RxFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MediaChannelFragment";
    public static final String IS_FIRST_TIME = "IS_FIRST_TIME";
    @SuppressLint("StaticFieldLeak")
    private static MediaChannelFragment instance;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private MultiTypeAdapter mAdapter;
    private MediaChannelDaoManager dao;
    private List<MediaChannelBean> list;

    public MediaChannelFragment() {
        // Required empty public constructor
    }


    public static MediaChannelFragment newInstance() {
        if (instance == null) {
            instance = new MediaChannelFragment();
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
        View view = inflater.inflate(R.layout.fragment_media_channel, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }


    private void initView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRefreshLayout.setOnRefreshListener(this);
        IOnItemLongClickListener listener = new IOnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                final MediaChannelBean item = list.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("取消订阅\" " + item.getName() + " \"?");
                builder.setPositiveButton(R.string.button_enter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dao.delete(item);
                                setAdapterData();
                            }
                        }).start();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        };
        mAdapter = new MultiTypeAdapter();
        Register.registerMediaChannelItem(mAdapter, listener);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        SharedPreferences preferences = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean result = preferences.getBoolean(IS_FIRST_TIME, true);
        if (result) {
            if (dao == null) {
                dao = MediaChannelDaoManager.getInstance();
            }
            dao.initData();
            editor.putBoolean(IS_FIRST_TIME, false).apply();
        }
        setAdapterData();
    }

    /**
     * 设置适配器数据
     */
    @SuppressLint("CheckResult")
    private void setAdapterData() {
        Observable
                .create(new ObservableOnSubscribe<List<MediaChannelBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<MediaChannelBean>> e) {
                        if (dao==null){
                            dao=MediaChannelDaoManager.getInstance();
                        }
                        list = dao.queryAll(MediaChannelBean.class);
                        e.onNext(list);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<MediaChannelBean>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<MediaChannelBean>>() {
                    @Override
                    public void accept(List<MediaChannelBean> channelBeanList) throws Exception {
                        mAdapter.setItems(channelBeanList);
                        mAdapter.notifyDataSetChanged();
                        if (channelBeanList.size() == 0) {
                            mTvDesc.setVisibility(View.VISIBLE);
                        } else {
                            mTvDesc.setVisibility(View.GONE);
                        }
                    }
                });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        setAdapterData();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
