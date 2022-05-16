package com.tryine.sdgq.common.mine.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-04-02 16:38
 */
public class KtzlBean {

    private String id;
    private String teacherId;//
    private String userName;// 用户昵称
    private String avatar;//用户头像
    private String couresCatsName;//课程分类名称
    private String courseId;//课程id
    private String couresName;//课程名称
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String uploadStatus;//资料状态 0-未上传 1-已上传

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getCouresCatsName() {
        return couresCatsName;
    }

    public void setCouresCatsName(String couresCatsName) {
        this.couresCatsName = couresCatsName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCouresName() {
        return couresName;
    }

    public void setCouresName(String couresName) {
        this.couresName = couresName;
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

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
