package com.tryine.sdgq.common.home.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-10 16:08
 */
public class BannerBean {

    private String id;//
    private String name;//标题
    private String imgUrl;//图片地址
    private String iconUrl;//图标地址
    private String type;//模块0、首页、1、圣典视频、2直播课 3、商城4、话题
    private String contextType;//模块0、首页、1、圣典视频、2直播课 3、商城4、话题 5、圣典琴谱
    private String contextId;
    private String coverUrl;
    private String title;
    private String videoTimeStr;
    private String giveCount;
    private String shareCount;

    public String getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(String giveCount) {
        this.giveCount = giveCount;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoTimeStr() {
        return videoTimeStr;
    }

    public void setVideoTimeStr(String videoTimeStr) {
        this.videoTimeStr = videoTimeStr;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
