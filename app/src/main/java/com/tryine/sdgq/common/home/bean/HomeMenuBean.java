package com.tryine.sdgq.common.home.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-11-22 15:01
 */
public class HomeMenuBean {

    String id;
    String name;//类型名称
    String title;
    String imgUrl;
    String piaonScoreId;
    int count;
    int imgId;


    String silverBean;//银豆
    String isFree;//是否免费 0-否 1-是
    String isUnlock;//是否解锁 0-否 1-是

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPiaonScoreId() {
        return piaonScoreId;
    }

    public void setPiaonScoreId(String piaonScoreId) {
        this.piaonScoreId = piaonScoreId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSilverBean() {
        return silverBean;
    }

    public void setSilverBean(String silverBean) {
        this.silverBean = silverBean;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getIsUnlock() {
        return isUnlock;
    }

    public void setIsUnlock(String isUnlock) {
        this.isUnlock = isUnlock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
