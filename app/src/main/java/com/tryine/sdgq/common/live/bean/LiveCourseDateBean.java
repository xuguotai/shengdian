package com.tryine.sdgq.common.live.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-04 10:28
 */
public class LiveCourseDateBean {


    private String id;//课程章节id
    private String startTime;//开始时间
    private String endTime;//课程章节id
    private String isSub;//是否被预约 0-否 1-是

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsSub() {
        return isSub;
    }

    public void setIsSub(String isSub) {
        this.isSub = isSub;
    }
}
