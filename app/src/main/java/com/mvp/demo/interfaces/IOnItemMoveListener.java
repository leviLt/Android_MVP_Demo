package com.mvp.demo.interfaces;

/**
 * Created by luotao
 * 2018/3/23
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public interface IOnItemMoveListener {
    /**
     * recyclerView拖动时触发
     *
     * @param fromPosition 拖动起始位置
     * @param toPosition   拖动到哪个位置
     */
    void onItemMove(int fromPosition, int toPosition);
}
