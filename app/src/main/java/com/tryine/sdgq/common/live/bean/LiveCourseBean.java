package com.tryine.sdgq.common.live.bean;

import java.util.List;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-04 10:28
 */
public class LiveCourseBean {


    private String id;//课程id
    private String name;//直播课名称
    private String courseType;//课程类型 0-直播大课 1-一对一辅导
    private String teacherId;//老师ID
    private String userId;//老师ID
    private String teacherName;//老师姓名
    private String teacherHeadImg;//老师头像
    private String course;//教学课程
    private String experience;//教育经验
    private String title;//职称称谓
    private String teacherRemark;//老师介绍
    private String typeId;//课程分类id
    private String goldBean;//所需金豆
    private String remark;//课程介绍
    private String imgUrl;//海报
    private String startTime;//开课时间
    private String signUpCount;//报名人数
    private String isBuy;//是否购买 0-否 -是
    private int teacherType;//老师类型 0-线上老师 1-线下老师 2-线上线下老师
    private String isSatisfy;//是否满员 0-否 1-是
    private String offlineTeacherId;//线下老师id

    List<LiveCourseDetailBean> detailVoList;
    List<LiveCourseDateBean> detailOneVoList;

    public List<LiveCourseDateBean> getDetailOneVoList() {
        return detailOneVoList;
    }

    public void setDetailOneVoList(List<LiveCourseDateBean> detailOneVoList) {
        this.detailOneVoList = detailOneVoList;
    }

    public String getIsSatisfy() {
        return isSatisfy;
    }

    public void setIsSatisfy(String isSatisfy) {
        this.isSatisfy = isSatisfy;
    }

    public String getOfflineTeacherId() {
        return offlineTeacherId;
    }

    public void setOfflineTeacherId(String offlineTeacherId) {
        this.offlineTeacherId = offlineTeacherId;
    }

    public int getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(int teacherType) {
        this.teacherType = teacherType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<LiveCourseDetailBean> getDetailVoList() {
        return detailVoList;
    }

    public void setDetailVoList(List<LiveCourseDetailBean> detailVoList) {
        this.detailVoList = detailVoList;
    }

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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherRemark() {
        return teacherRemark;
    }

    public void setTeacherRemark(String teacherRemark) {
        this.teacherRemark = teacherRemark;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSignUpCount() {
        return signUpCount;
    }

    public void setSignUpCount(String signUpCount) {
        this.signUpCount = signUpCount;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }
}
