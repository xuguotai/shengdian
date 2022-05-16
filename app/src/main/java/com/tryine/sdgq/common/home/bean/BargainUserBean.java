package com.tryine.sdgq.common.home.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-18 10:13
 */
public class BargainUserBean {
    private String id;//记录id
    private String helpUserId;//帮忙砍价用户ID
    private String helpUserName;// 帮忙砍价用户名称
    private String helpHeadImg;//帮忙砍价用户头像
    private String helpPrice;//帮砍金额
    private String createTime;

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

    public String getHelpUserId() {
        return helpUserId;
    }

    public void setHelpUserId(String helpUserId) {
        this.helpUserId = helpUserId;
    }

    public String getHelpUserName() {
        return helpUserName;
    }

    public void setHelpUserName(String helpUserName) {
        this.helpUserName = helpUserName;
    }

    public String getHelpHeadImg() {
        return helpHeadImg;
    }

    public void setHelpHeadImg(String helpHeadImg) {
        this.helpHeadImg = helpHeadImg;
    }

    public String getHelpPrice() {
        return helpPrice;
    }

    public void setHelpPrice(String helpPrice) {
        this.helpPrice = helpPrice;
    }
}
