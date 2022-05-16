package com.tryine.sdgq.common.home.bean;

import java.io.Serializable;

/**
 * 校区
 * @author: zhangshuaijun
 * @time: 2021-12-15 17:11
 */
public class CampusBean implements Serializable {

    private String id;//
    private String name;//校区名称
    private String headmasterUrl;//校区头像
    private String province;//省
    private String city;//市
    private String area;//区/县
    private String addrDes;//详细地址
    private String lonLatTencent;//经纬度-腾讯
    private String lonLatAddress;//经纬度地址
    private String businessHours;//营业时间
    private String contact;// 联系方式
    private String headmasterName;//校长名字
    private String headmasterWeact;//校长微信
    private String courseId;//课程id
    private String courseName;//课程名称
    private String status;//
    private String coverUrl;//校区图片
    private String coverPath;//校区封面图
    private String remake;//
    private String distance;// 距离

    public String getHeadmasterUrl() {
        return headmasterUrl;
    }

    public void setHeadmasterUrl(String headmasterUrl) {
        this.headmasterUrl = headmasterUrl;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddrDes() {
        return addrDes;
    }

    public void setAddrDes(String addrDes) {
        this.addrDes = addrDes;
    }

    public String getLonLatTencent() {
        return lonLatTencent;
    }

    public void setLonLatTencent(String lonLatTencent) {
        this.lonLatTencent = lonLatTencent;
    }

    public String getLonLatAddress() {
        return lonLatAddress;
    }

    public void setLonLatAddress(String lonLatAddress) {
        this.lonLatAddress = lonLatAddress;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getHeadmasterName() {
        return headmasterName;
    }

    public void setHeadmasterName(String headmasterName) {
        this.headmasterName = headmasterName;
    }

    public String getHeadmasterWeact() {
        return headmasterWeact;
    }

    public void setHeadmasterWeact(String headmasterWeact) {
        this.headmasterWeact = headmasterWeact;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
