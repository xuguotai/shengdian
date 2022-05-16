package com.tryine.sdgq.common.mine.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-11 15:37
 */
public class FansBean {

    private String id;//关注id
    private String userId;//用户id
    private String userName;//用户名称
    private String avatar;//用户头像
    private String isMutual;//是否相互关注 0-否 1-是
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getIsMutual() {
        return isMutual;
    }

    public void setIsMutual(String isMutual) {
        this.isMutual = isMutual;
    }
}
