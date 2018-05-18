package com.mvp.demo.interfaces;

/**
 * Created by luotao
 * 2018/3/23
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public interface IOnDragVHListener {
    /**
     * item 选中触发
     */
    void onItemSelected();

    /**
     * item拖拽结束或者滑动结束触发
     */
    void onItemFinish();
}
