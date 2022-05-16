package com.tryine.sdgq.common.circle.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-16 14:13
 */
public class CircleDetailBean {

    private String id;//内容id
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
    private String contentUrl;//图片或视频地址
    private String commentCount;//评论数
    private String rewardCount;//打赏人数
    private String collectCount;//收藏数
    private String giveCount;//点赞数量
    private String isFocus;//是否关注 0-否 1-是
    private String isGive;//是否点赞 0-否 1-是
    private String coverUrl;
    private String isCollect;
    private String createTime;
    private int isVip;//是否vip

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
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

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getRewardCount() {
        return rewardCount;
    }

    public void setRewardCount(String rewardCount) {
        this.rewardCount = rewardCount;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public String getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(String giveCount) {
        this.giveCount = giveCount;
    }

    public String getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(String isFocus) {
        this.isFocus = isFocus;
    }

    public String getIsGive() {
        return isGive;
    }

    public void setIsGive(String isGive) {
        this.isGive = isGive;
    }
}
