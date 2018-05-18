package com.mvp.demo.DaoManagerUtils;

import android.database.sqlite.SQLiteDatabase;

import com.mvp.demo.Constant;
import com.mvp.demo.InitApp;
import com.mvp.demo.greendao.DaoMaster;
import com.mvp.demo.greendao.DaoSession;

/**
 * Created by luotao
 * 2018/1/18
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class DaoManager {
    /**
     * volatile 多线程访问
     */
    private static volatile DaoManager instance;
    private static DaoMaster sDaoMaster;
    private static DaoSession sDaoSession;
    private static DaoMaster.DevOpenHelper sDevOpenHelper;
    private static SQLiteDatabase db;

    /**
     * 构造方法私有化
     */
    private DaoManager() {

    }

    /**
     * 初始化
     */
    public static void init() {
        //创建表
        sDevOpenHelper = new DaoMaster.DevOpenHelper(InitApp.appContext, Constant.GREENDAO_DATABASE_NAME, null);
        sDaoMaster = new DaoMaster(sDevOpenHelper.getWritableDatabase());
        sDaoSession = sDaoMaster.newSession();
    }

    public DaoMaster getDaoMaster() {
        if (sDaoMaster == null) {
            //创建表
            sDevOpenHelper = new DaoMaster.DevOpenHelper(InitApp.appContext, Constant.GREENDAO_DATABASE_NAME, null);
            sDaoMaster = new DaoMaster(sDevOpenHelper.getWritableDatabase());
        }
        return sDaoMaster;
    }

    public DaoSession getDaoSession() {
        if (sDaoSession == null) {
            if (sDaoMaster != null) {
                sDaoSession = sDaoMaster.newSession();
            } else {
                sDaoMaster = getDaoMaster();
                sDaoSession = sDaoMaster.newSession();
            }
        }
        return sDaoSession;
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

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeHelper();
        closeDaoSession();
    }

    private void closeDaoSession() {
        if (null != sDaoSession) {
            sDaoSession.clear();
            sDaoSession = null;
        }
    }

    private void closeHelper() {
        if (sDevOpenHelper != null) {
            sDevOpenHelper.close();
            sDevOpenHelper = null;
        }
    }
}