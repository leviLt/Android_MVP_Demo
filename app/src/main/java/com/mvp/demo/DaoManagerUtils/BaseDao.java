package com.mvp.demo.DaoManagerUtils;

import android.util.Log;

import com.mvp.demo.greendao.DaoMaster;
import com.mvp.demo.greendao.DaoSession;

import java.util.List;

/**
 * Created by luotao
 * 2018/3/20
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class BaseDao<T> {
    public static final String TAG = BaseDao.class.getName();
    public static final boolean DUBUG = true;
    public DaoManager mDaoManager;
    public DaoMaster mDaoMaster;
    public DaoSession mDaoSession;

    BaseDao() {
        mDaoManager = DaoManager.getInstance();
        mDaoMaster = mDaoManager.getDaoMaster();
        mDaoSession = mDaoManager.getDaoSession();
    }

    /**************************数据库操作********************************/
    /**
     * 插入单个对象
     *
     * @param objct
     * @return
     */
    public boolean insertObject(T objct) {
        boolean flag = false;
        try {
            flag = mDaoSession.insert(objct) != -1;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 插入过个对象
     *
     * @param objcts
     * @return
     */
    public boolean insertMultObject(final List<T> objcts) {
        boolean flag = false;
        if (objcts == null || objcts.isEmpty()) {
            return false;
        }
        try {
            mDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objcts) {
                        mDaoSession.insertOrReplace(objcts);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 对象的形式更新数据库
     *
     * @param object
     */
    public void update(T object) {
        if (object == null) {
            return;
        }
        try {
            mDaoSession.update(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量更新
     *
     * @param objects
     * @param cls
     */
    public void updateMultObject(final List<T> objects, Class cls) {
        if (objects == null || objects.isEmpty()) {
            return;
        }
        try {
            mDaoSession.getDao(cls).updateInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        mDaoSession.update(object);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除对象
     *
     * @param objct
     */
    public void delete(T objct) {
        if (objct == null) {
            return;
        }
        try {
            mDaoSession.delete(objct);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除
     *
     * @param objects
     * @param cls
     */
    public void deleteMultObjects(final List<T> objects, Class cls) {
        if (objects == null || objects.isEmpty()) {
            return;
        }
        try {
            mDaoSession.getDao(cls).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object :
                            objects) {
                        mDaoSession.delete(object);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有
     *
     * @param cls
     */
    public void deleteAll(Class cls) {
        mDaoSession.deleteAll(cls);
    }

    /**
     * 查询所有对象
     *
     * @param clss
     * @return
     */
    public List<T> queryAll(Class clss) {
        List objects = null;
        try {
            objects = mDaoSession.getDao(clss).loadAll();
        } catch (Exception e) {
            Log.e(TAG, "queryAll: " + e.toString());
        }
        return objects;
    }
    /***************************关闭数据库*************************/
    /**
     * 关闭数据库一般在onDestroy中使用
     */
    public void closeDataBase() {
        mDaoManager.closeDataBase();
    }
}
