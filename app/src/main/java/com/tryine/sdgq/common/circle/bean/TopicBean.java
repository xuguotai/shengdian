package com.tryine.sdgq.common.circle.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-16 14:13
 */
public class TopicBean implements Serializable {

    private String id;//话题id
    private String name;//话题名称
    private String imgUrl;//话题图片
    private String topicDesc;//话题描述
    private String startTime;//开始日期
    private String endTime;//结束日期
    private String partakeCount;//参与人数

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

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
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

    public String getPartakeCount() {
        return partakeCount;
    }

    public void setPartakeCount(String partakeCount) {
        this.partakeCount = partakeCount;
    }
}
