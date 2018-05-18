package com.mvp.demo.utils;

import android.support.v7.util.DiffUtil;

import javax.annotation.Nullable;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by luotao
 * 2018/1/25
 * emil:luotaosc@foxmail.com
 * qq:751423471
 * @author 罗涛
 */

public class DiffCallBack extends DiffUtil.Callback {
    private final Items mOldItems, mNewItems;

    private DiffCallBack(Items oldItems, Items newItems) {
        mOldItems = oldItems;
        mNewItems = newItems;
    }

    public static void create(@Nullable Items oldItems, @Nullable Items newItems, @Nullable MultiTypeAdapter adapter) {
        DiffCallBack diffCallBack = new DiffCallBack(oldItems, newItems);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallBack, true);
        diffResult.dispatchUpdatesTo(adapter);
    }

    @Override
    public int getOldListSize() {
        return mOldItems != null ? mOldItems.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewItems != null ? mNewItems.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).equals(mNewItems.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldItems.get(oldItemPosition).hashCode() == mNewItems.get(newItemPosition).hashCode();
    }
}
