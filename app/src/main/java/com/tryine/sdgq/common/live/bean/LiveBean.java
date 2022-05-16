package com.tryine.sdgq.common.live.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-04 10:28
 */
public class LiveBean {


    private String id;//课程id
    private String liveStatus;//直播状态 0-未开始 1-直播中 2-离开 3-已结束 4-封禁直播课名称
    private String name;//直播课名称
    private String courseType;//课程类型 0-直播大课 1-一对一辅导
    private String teacherId;//老师ID
    private String teacherName;//老师姓名
    private String teacherHeadImg;//老师头像
    private String goldBean;//所需金豆
    private String remark;//课程介绍
    private String imgUrl;//海报
    private String collectCount;//收藏人数
    private String isBuy;//是否购买 0-否 1-是
    private String isSatisfy;//是否满员 0-否 1-是
    private String startTimeDesc;
    private String detailGoldBean;
    private String courseDetailId;
    private String courseId;
    private String liveId;

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getDetailGoldBean() {
        return detailGoldBean;
    }

    public void setDetailGoldBean(String detailGoldBean) {
        this.detailGoldBean = detailGoldBean;
    }

    public String getCourseDetailId() {
        return courseDetailId;
    }

    public void setCourseDetailId(String courseDetailId) {
        this.courseDetailId = courseDetailId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getIsSatisfy() {
        return isSatisfy;
    }

    public void setIsSatisfy(String isSatisfy) {
        this.isSatisfy = isSatisfy;
    }

    public String getStartTimeDesc() {
        return startTimeDesc;
    }

    public void setStartTimeDesc(String startTimeDesc) {
        this.startTimeDesc = startTimeDesc;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherHeadImg() {
        return teacherHeadImg;
    }

    public void setTeacherHeadImg(String teacherHeadImg) {
        this.teacherHeadImg = teacherHeadImg;
    }

    public String getGoldBean() {
        return goldBean;
    }

    public void setGoldBean(String goldBean) {
        this.goldBean = goldBean;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }
}
