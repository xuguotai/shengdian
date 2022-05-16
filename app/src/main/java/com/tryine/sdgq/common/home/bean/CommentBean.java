package com.tryine.sdgq.common.home.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-29 15:36
 */
public class CommentBean {

    private String id;//
    private String content;//内容
    private String star;//星级
    private String topStatus;//是否匿名
    private String userId;//用户id
    private String avatar;//头像
    private String userName;//昵称
    private String hoursNum;//数量
    private String createTime;//时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(String topStatus) {
        this.topStatus = topStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHoursNum() {
        return hoursNum;
    }

    public void setHoursNum(String hoursNum) {
        this.hoursNum = hoursNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
