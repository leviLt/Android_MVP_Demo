package com.mvp.demo.widget.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.mvp.demo.interfaces.IOnDragVHListener;
import com.mvp.demo.interfaces.IOnItemMoveListener;

/**
 * Created by luotao
 * 2018/3/21
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class ItemDragHelperCallBack extends ItemTouchHelper.Callback {
    /**
     * 滑动的方式
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        // 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //不同类型之间不能相互移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //拖动回调监听
        if (recyclerView.getAdapter() instanceof IOnItemMoveListener) {
            IOnItemMoveListener listener = (IOnItemMoveListener) recyclerView.getAdapter();
            listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof IOnDragVHListener) {
                IOnDragVHListener listener = (IOnDragVHListener) viewHolder;
                listener.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof IOnDragVHListener) {
            IOnDragVHListener listener = (IOnDragVHListener) viewHolder;
            listener.onItemFinish();
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        //取消长按拖拽，自己控制
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        //不支持滑动功能
        return false;
    }
}
