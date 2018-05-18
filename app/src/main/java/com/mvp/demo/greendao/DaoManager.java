package com.mvp.demo.greendao;

import com.mvp.demo.Constant;
import com.mvp.demo.InitApp;

/**
 * Created by luotao
 * 2018/1/18
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class DaoManager {
    private static volatile DaoManager instance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DaoManager() {
        //创建表
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(InitApp.appContext, Constant.GREENDAO_USER_TABLE_NAME, null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    /**
     * 单列模式
     *
     * @return
     */
    public static DaoManager getInstance() {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new DaoManager();
                } else {
                    return instance;
                }
            }
        }
        return instance;
    }
}