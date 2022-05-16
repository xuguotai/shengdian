package com.tryine.sdgq.common.live.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-04 10:28
 */
public class LiveCourseDetailBean {

    private String id;//课程章节id
    private String courseId;//直播课程ID
    private String name;//章节名称
    private String goldBean;// 所需金豆
    private String startTime;//开课时间
    private String liveId;//直播id
    private String roomStatus;//直播状态 0-未开始 1-直播中 2-离开 3-已结束 4-封禁
    private String lookCount;// 学习人数
    private String recordUrl;//回放地址
    private String isBuy;//是否购买 0-否 1-是
    private String GroupId;//房间id
    private String isLive;//是否已直播 0-否 1-是

    public String getIsLive() {
        return isLive;
    }

    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoldBean() {
        return goldBean;
    }

    public void setGoldBean(String goldBean) {
        this.goldBean = goldBean;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public String getLookCount() {
        return lookCount;
    }

    public void setLookCount(String lookCount) {
        this.lookCount = lookCount;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
}
