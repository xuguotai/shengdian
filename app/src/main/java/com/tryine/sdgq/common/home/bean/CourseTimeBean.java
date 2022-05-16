package com.tryine.sdgq.common.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 课程
 * @author: zhangshuaijun
 * @time: 2021-12-15 16:40
 */
public class CourseTimeBean implements Serializable {

    String time;

    List<CourseBean> piratesTeacherVoList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<CourseBean> getPiratesTeacherVoList() {
        return piratesTeacherVoList;
    }

    public void setPiratesTeacherVoList(List<CourseBean> piratesTeacherVoList) {
        this.piratesTeacherVoList = piratesTeacherVoList;
    }
}
