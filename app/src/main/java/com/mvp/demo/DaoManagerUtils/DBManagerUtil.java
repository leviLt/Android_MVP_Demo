package com.mvp.demo.DaoManagerUtils;

/**
 * Created by luotao
 * 2018/3/20
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class DBManagerUtil {
    private static ChannelDaoManager instance;

    public static ChannelDaoManager getInstance() {
        if (instance == null) {
            instance = ChannelDaoManager.getInstance();
        }
        return instance;
    }
}
