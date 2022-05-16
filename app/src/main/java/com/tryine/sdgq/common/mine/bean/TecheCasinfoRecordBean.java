package com.tryine.sdgq.common.mine.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2022-02-19 13:40
 */
public class TecheCasinfoRecordBean implements Serializable {

    private String id;
    private String userId;
    private String teacherCourseId;
    private String couresName;
    private String teacherName;
    private String startDate;
    private String phone;
    private String imgUrl;

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

    public String getTeacherCourseId() {
        return teacherCourseId;
    }

    public void setTeacherCourseId(String teacherCourseId) {
        this.teacherCourseId = teacherCourseId;
    }

    public String getCouresName() {
        return couresName;
    }

    public void setCouresName(String couresName) {
        this.couresName = couresName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
