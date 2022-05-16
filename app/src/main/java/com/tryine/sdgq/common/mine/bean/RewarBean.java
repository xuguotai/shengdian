package com.tryine.sdgq.common.mine.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-11 13:31
 */
public class RewarBean {

    private String id;//记录id
    private String userName;//用户昵称
    private String presentType;//类型 0:金豆礼物 1:银豆礼物
    private String beanCount;//礼物价格
    private String createTime;//时间
    private String imgUrl;//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPresentType() {
        return presentType;
    }

    public void setPresentType(String presentType) {
        this.presentType = presentType;
    }

    public String getBeanCount() {
        return beanCount;
    }

    public void setBeanCount(String beanCount) {
        this.beanCount = beanCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
