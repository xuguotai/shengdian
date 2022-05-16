package com.tryine.sdgq.common.circle.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-16 14:13
 */
public class CircleBean {

    private String id;//内容ID
    private String userId;//用户id
    private String userName;//用户名称
    private String avatar;//用户头像
    private String level;//用户等级
    private String topicId;//话题id
    private String topicName;//话题名称
    private String title;//标题
    private String content;//内容
    private String latLong;//经纬度
    private String address;//详细地址
    private String contentType;//上传类型 0-图片 1-视频
    private String contentUrl;// 图片或视频地址
    private String giveCount;//点赞数量
    private String distance;//距离km
    private String coverUrl;
    private String isGive;//是否点赞 0-否 1-是
    private int isVip;//是否vip

    private String courseType;//课程类型 0-直播大课 1-一对一辅导
    private String name;//课程名称
    private String startTime;//直播时间
    private String teacherName;//老师名称

    public String getIsGive() {
        return isGive;
    }

    public void setIsGive(String isGive) {
        this.isGive = isGive;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(String giveCount) {
        this.giveCount = giveCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
