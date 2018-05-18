package com.mvp.demo.greendao;

/**
 * Created by luotao
 * 2018/1/18
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */
public class EntityManager<T> {
    private static volatile EntityManager instance;
    private UserDao mUserDao;
    private NewsConfigDao mNewsConfigDao;

    private EntityManager() {
    }

    public UserDao getEntityDao() {
        mUserDao = DaoManager.getInstance().getDaoSession().getUserDao();
        return mUserDao;
    }

    public NewsConfigDao getNewsConfigDao() {
        mNewsConfigDao = DaoManager.getInstance().getDaoSession().getNewsConfigDao();
        return mNewsConfigDao;
    }

    public static EntityManager getInstance() {
        if (instance == null) {
            synchronized (EntityManager.class) {
                if (instance == null) {
                    instance = new EntityManager();
                }
            }
        }
        return instance;
    }
}
