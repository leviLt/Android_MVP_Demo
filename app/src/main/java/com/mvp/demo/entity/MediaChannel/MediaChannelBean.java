package com.mvp.demo.entity.MediaChannel;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * @author Meiji
 * @date 2017/4/7
 */
@Entity
public class MediaChannelBean implements Parcelable {


    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String channelId;
    private String name;
    private String avatar;
    private String type;
    private String followCount;
    private String descText;
    private String url;

    public MediaChannelBean() {
    }

    @Keep()
    public MediaChannelBean(Long id, String channelId, String name, String avatar,
                            String type, String followCount, String descText, String url) {
        this.id = id;
        this.channelId = channelId;
        this.name = name;
        this.avatar = avatar;
        this.type = type;
        this.followCount = followCount;
        this.descText = descText;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFollowCount() {
        return followCount;
    }

    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.channelId);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.type);
        dest.writeString(this.followCount);
        dest.writeString(this.descText);
        dest.writeString(this.url);
    }


    private MediaChannelBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.channelId = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.type = in.readString();
        this.followCount = in.readString();
        this.descText = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<MediaChannelBean> CREATOR = new Parcelable.Creator<MediaChannelBean>() {
        @Override
        public MediaChannelBean createFromParcel(Parcel source) {
            return new MediaChannelBean(source);
        }

        @Override
        public MediaChannelBean[] newArray(int size) {
            return new MediaChannelBean[size];
        }
    };
}
