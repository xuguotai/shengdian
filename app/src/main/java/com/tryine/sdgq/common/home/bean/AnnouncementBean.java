package com.tryine.sdgq.common.home.bean;

import java.io.Serializable;

/**
 * 公告
 *
 * @author: zhangshuaijun
 * @time: 2021-12-22 16:35
 */
public class AnnouncementBean implements Serializable {

    private String id;
    private String name;//标题
    private String content;//公告内容
    private String imgUrl;//图片地址
    private String iconUrl;//图标地址
    private String createTime;//创建时间
    private String couresIdList;//课程id
    private String campusName;//校区

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCouresIdList() {
        return couresIdList;
    }

    public void setCouresIdList(String couresIdList) {
        this.couresIdList = couresIdList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
