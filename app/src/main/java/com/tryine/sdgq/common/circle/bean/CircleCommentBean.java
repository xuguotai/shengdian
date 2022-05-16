package com.tryine.sdgq.common.circle.bean;

import java.util.List;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-16 14:13
 */
public class CircleCommentBean {

    private String id;// 评论id
    private String userId;//用户id
    private String userName;//用户昵称
    private String avatar;//头像
    private String level;//等级
    private String content;//评论内容
    private String createTime;//非必须
    private String createTimeStr;//时间
    private String isTwoCommon;//是否有二级评论 0-否 1-是
    private String twoCouont;//二级评论条数
    private String replyUserName;//回复昵称
    private boolean isExpand = true;
    private int pageNum = 0;
    private int pages = 0;//回复列表总页数

    List<CircleCommentBean> commentVoList;

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getIsTwoCommon() {
        return isTwoCommon;
    }

    public void setIsTwoCommon(String isTwoCommon) {
        this.isTwoCommon = isTwoCommon;
    }

    public String getTwoCouont() {
        return twoCouont;
    }

    public void setTwoCouont(String twoCouont) {
        this.twoCouont = twoCouont;
    }

    public List<CircleCommentBean> getCommentVoList() {
        return commentVoList;
    }

    public void setCommentVoList(List<CircleCommentBean> commentVoList) {
        this.commentVoList = commentVoList;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}
