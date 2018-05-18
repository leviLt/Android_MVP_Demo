package com.mvp.demo.DaoManagerUtils;

import com.mvp.demo.Constant;
import com.mvp.demo.InitApp;
import com.mvp.demo.R;
import com.mvp.demo.entity.ChannelBean.NewsChannelBean;
import com.mvp.demo.greendao.NewsChannelBeanDao;

import java.util.List;

/**
 * Created by luotao
 * 2018/1/18
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */
public class ChannelDaoManager extends BaseDao<NewsChannelBean> {
    private static volatile ChannelDaoManager instance;
    private NewsChannelBeanDao mNewsChannelBeanDao;

    private ChannelDaoManager() {
        super();
    }

    public NewsChannelBeanDao getNewsChannelDao() {
        mNewsChannelBeanDao = DaoManager.getInstance().getDaoSession().getNewsChannelBeanDao();
        return mNewsChannelBeanDao;
    }

    /**
     * 初始化channel
     */
    public void addInitData() {
        String[] categoryId = InitApp.appContext.getResources().getStringArray(R.array.mobile_news_id);
        String[] categoryName = InitApp.appContext.getResources().getStringArray(R.array.mobile_news_name);
        for (int i = 0; i < 8; i++) {
            add(categoryId[i], categoryName[i], Constant.NEWS_CHANNEL_ENABLE, i);
        }
        for (int i = 8; i < categoryId.length; i++) {
            add(categoryId[i], categoryName[i], Constant.NEWS_CHANNEL_DISABLE, 8 + i);
        }
    }

    /**
     * 添加数据
     *
     * @param categoryId
     * @param categoryName
     * @param id
     * @param isEnable
     */
    private void add(String categoryId, String categoryName, int isEnable, int id) {
        NewsChannelBean newsChannelBean = new NewsChannelBean();
        newsChannelBean.setChannelId(categoryId);
        newsChannelBean.setChannelName(categoryName);
        newsChannelBean.setIsEnable(isEnable);
        newsChannelBean.setId((long) id);
        insertObject(newsChannelBean);
    }

    public static ChannelDaoManager getInstance() {
        if (instance == null) {
            synchronized (ChannelDaoManager.class) {
                if (instance == null) {
                    instance = new ChannelDaoManager();
                }
            }
        }
        return instance;
    }

    /**
     * 查询选中的标签
     *
     * @param enable
     * @return
     */
    public List<NewsChannelBean> queryChannelEnable(int enable) {
        return mDaoSession.getNewsChannelBeanDao().queryBuilder()
                .where(NewsChannelBeanDao.Properties.IsEnable.eq(enable)).list();
    }

    /**
     * 查询没有选中的标签
     *
     * @param disable
     * @return
     */
    public List<NewsChannelBean> queryChannelDisable(int disable) {
        return mDaoSession.getNewsChannelBeanDao().queryBuilder()
                .where(NewsChannelBeanDao.Properties.IsEnable.eq(disable)).list();
    }

}
