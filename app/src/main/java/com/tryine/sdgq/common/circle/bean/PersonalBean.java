package com.tryine.sdgq.common.circle.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-21 10:14
 */
public class PersonalBean implements Serializable {

    private String userId;//用户id
    private String userName;//用户昵称
    private String avatar;//头像
    private String userExplain;//个人简介
    private String focusCount;//关注数
    private String fansCount;//粉丝数
    private String isFocus;//是否关注 0否 1是
    private String isShowLabel;//是否显示兴趣标签 0-否 1-是
    private String isReceive;//是否接收未关注人私信 0-否 1-是
    private String level;//用户等级

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private List<LabelBean> labelList;

    public List<LabelBean> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<LabelBean> labelList) {
        this.labelList = labelList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserExplain() {
        return userExplain;
    }

    public void setUserExplain(String userExplain) {
        this.userExplain = userExplain;
    }

    public String getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(String focusCount) {
        this.focusCount = focusCount;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public String getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(String isFocus) {
        this.isFocus = isFocus;
    }

    public String getIsShowLabel() {
        return isShowLabel;
    }

    public void setIsShowLabel(String isShowLabel) {
        this.isShowLabel = isShowLabel;
    }

    public String getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(String isReceive) {
        this.isReceive = isReceive;
    }




}
