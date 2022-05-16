package com.tryine.sdgq.common.home.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-28 16:57
 */
public class TeacherBean implements Serializable {

    private String id;//
    private String userId;//
    private String name;//老师姓名
    private String headImg;//老师头像
    private String campusId;//所属校区id
    private String campusName;//所属校区名称
    private String couresIdList;//课程id列表
    private String couresName;//课程名称
    private String telephone;//联系电话
    private String educationLayer;//教育经验
    private String classLayer;//课时要求
    private String couresCatsName;
    private String star;

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getCouresCatsName() {
        return couresCatsName;
    }

    public void setCouresCatsName(String couresCatsName) {
        this.couresCatsName = couresCatsName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCouresIdList() {
        return couresIdList;
    }

    public void setCouresIdList(String couresIdList) {
        this.couresIdList = couresIdList;
    }

    public String getCouresName() {
        return couresName;
    }

    public void setCouresName(String couresName) {
        this.couresName = couresName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEducationLayer() {
        return educationLayer;
    }

    public void setEducationLayer(String educationLayer) {
        this.educationLayer = educationLayer;
    }

    public String getClassLayer() {
        return classLayer;
    }

    public void setClassLayer(String classLayer) {
        this.classLayer = classLayer;
    }
}
