package com.mvp.demo.DaoManagerUtils;

import com.mvp.demo.entity.MediaChannel.MediaChannelBean;

/**
 * Created by luotao
 * 2018/4/27
 * emil:luotaosc@foxmail.com
 * qq:751423471
 * @author 罗涛
 */
public class MediaChannelDaoManager extends BaseDao<MediaChannelBean> {
    /**
     * danManager实例
     */
    private static MediaChannelDaoManager instance;

    /**
     * 单例模式
     *
     * @return manager
     */
    public static MediaChannelDaoManager getInstance() {
        if (instance == null) {
            synchronized (MediaChannelDaoManager.class) {
                if (instance == null) {
                    instance = new MediaChannelDaoManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化订阅号数据
     */
    public void initData() {
        add("4377795668", "新华网", "http://p2.pstatp.com/large/3658/7378365093", "news",
                "", "传播中国，报道世界；权威声音，亲切表达。", "http://toutiao.com/m4377795668/");
        add("52445544609", "互联网的这点事", "http://p3.pstatp.com/large/ef300164e786ff295da", "news",
                "", "每天为你速递最新、最鲜、最有料的互联网科技资讯！", "http://toutiao.com/m52445544609/");
    }

    /**
     * 添加数据
     *
     * @param mediaChannelId
     * @param name
     * @param avater
     * @param type
     * @param followCount
     * @param descText
     * @param url
     */
    private void add(String mediaChannelId, String name, String avater, String type, String followCount, String descText, String url) {
        MediaChannelBean mediaChannelBean = new MediaChannelBean();
        mediaChannelBean.setChannelId(mediaChannelId);
        mediaChannelBean.setName(name);
        mediaChannelBean.setAvatar(avater);
        mediaChannelBean.setType(type);
        mediaChannelBean.setFollowCount(followCount);
        mediaChannelBean.setDescText(descText);
        mediaChannelBean.setUrl(url);
        insertObject(mediaChannelBean);
    }
}
