package com.tryine.sdgq.common.mine.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-24 15:37
 */
public class MessageBean implements Serializable {

    private String id;//
    private String userId;//
    private String content;//消息内容
    private String resourcesUserId;//资源所属用户id
    private String resourcesId;//资源id
    private String resourcesType;//消息类型 0-系统消息 1-点赞消息 2-分享消息 3-评论消息 4-打赏消息
    private String isRead;// 是否已读 0-否 1-是
    private String createDate;//
    private String createTime;//

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResourcesUserId() {
        return resourcesUserId;
    }

    public void setResourcesUserId(String resourcesUserId) {
        this.resourcesUserId = resourcesUserId;
    }

    public String getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(String resourcesId) {
        this.resourcesId = resourcesId;
    }

    public String getResourcesType() {
        return resourcesType;
    }

    public void setResourcesType(String resourcesType) {
        this.resourcesType = resourcesType;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
