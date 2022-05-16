package com.tryine.sdgq.common.home.bean;

import java.io.Serializable;

/**
 * 课程
 * @author: zhangshuaijun
 * @time: 2021-12-15 16:40
 */
public class CourseChapterBean implements Serializable {

    private String id;//线上课程明细id
    private String courseDetailId;//课程章节id
    private String courseDetailName;//课程章节名称
    private String startTime;//直播时间
    private String teacherName;//老师
    private String recordUrl;//回放地址
    private String liveId;//
    private String roomStatus;//
    private String isAttend;//是否上了课 0-否 1-是

    public String getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(String isAttend) {
        this.isAttend = isAttend;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseDetailId() {
        return courseDetailId;
    }

    public void setCourseDetailId(String courseDetailId) {
        this.courseDetailId = courseDetailId;
    }

    public String getCourseDetailName() {
        return courseDetailName;
    }

    public void setCourseDetailName(String courseDetailName) {
        this.courseDetailName = courseDetailName;
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
}
