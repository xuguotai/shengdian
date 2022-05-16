package com.tryine.sdgq.common.mine.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2022-02-08 13:13
 */
public class CardBean implements Serializable {


    private String id;//
    private String userId;//用户id
    private String experienceId;//体验卡表id
    private String couresIdList;//课程id列表
    private String couresName;//课程名称
    private String experienceLayer;//体验期限
    private String experienceClass;//体验课时
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String  status;//状态 0:未开始 1:使用中2：暂停使用3：已使用 4、转赠好友
    private String mobile;//转送手机号码
    private String updateTime;
    private String userExperienceId;//转赠用户表id

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(String experienceId) {
        this.experienceId = experienceId;
    }

    public String getCouresIdList() {
        return couresIdList;
    }

    public void setCouresIdList(String couresIdList) {
        this.couresIdList = couresIdList;
    }

    public String getCouresName() {
        return couresName;
    }

    public void setCouresName(String couresName) {
        this.couresName = couresName;
    }

    public String getExperienceLayer() {
        return experienceLayer;
    }

    public void setExperienceLayer(String experienceLayer) {
        this.experienceLayer = experienceLayer;
    }

    public String getExperienceClass() {
        return experienceClass;
    }

    public void setExperienceClass(String experienceClass) {
        this.experienceClass = experienceClass;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserExperienceId() {
        return userExperienceId;
    }

    public void setUserExperienceId(String userExperienceId) {
        this.userExperienceId = userExperienceId;
    }
}
