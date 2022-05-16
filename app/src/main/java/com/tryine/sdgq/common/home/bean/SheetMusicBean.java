package com.tryine.sdgq.common.home.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-09 14:04
 */
public class SheetMusicBean {


    private String id;//琴谱id
    private String name;//琴谱名称
    private String imgUrl;//琴谱图片
    private String typeId;//琴谱类型id
    private String silverBean;//所需银豆
    private String scoreDesc;//琴谱描述
    private String isFree;//是否免费 0-否 1-是
    private String isUnlock;//是否解锁 0-否 1-是
    private String createTime;
    private String coverUrl;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIsUnlock() {
        return isUnlock;
    }

    public void setIsUnlock(String isUnlock) {
        this.isUnlock = isUnlock;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getSilverBean() {
        return silverBean;
    }

    public void setSilverBean(String silverBean) {
        this.silverBean = silverBean;
    }

    public String getScoreDesc() {
        return scoreDesc;
    }

    public void setScoreDesc(String scoreDesc) {
        this.scoreDesc = scoreDesc;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }
}
