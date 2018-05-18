package com.mvp.demo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by luotao
 * 2018/1/18
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */
@Entity
public class NewsConfig {
    @Id
    private long id;
    private String label;
    private String labelType;
    @Generated(hash = 1096529839)
    public NewsConfig(long id, String label, String labelType) {
        this.id = id;
        this.label = label;
        this.labelType = labelType;
    }
    @Generated(hash = 2027964477)
    public NewsConfig() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getLabelType() {
        return this.labelType;
    }
    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }
}
