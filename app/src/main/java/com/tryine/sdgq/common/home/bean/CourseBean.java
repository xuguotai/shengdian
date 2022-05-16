package com.tryine.sdgq.common.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 课程
 * @author: zhangshuaijun
 * @time: 2021-12-15 16:40
 */
public class CourseBean implements Serializable {

    private String id;//
    private String name;//课程名称,老师名称
    private String campusId;//校区id
    private String catsList;//
    private String layer;//用卡期限
    private String sortId;//
    private String price;//开卡金额
    private String length;//上课时长
    private String imgUrl;//图片地址
    private String iconUrl;//图标地址
    private String dayNumber;//每日可约次数
    private String weeksNumber;//每周可约次数
    private String monthNumber;//每月可约次数
    private String rewardPrice;//奖励金豆数
    private String remake;
    private String detail;
    private String couresName;//课程名称
    private String startDate;//开课日期
    private String startTime;//开课时间
    private String endTime;//结束时间
    private String courseStatus;//预约状态
    private String courseStatusVal;//
    private String teacherHeadImg;//
    private String additionalName;//
    private int additionalAmount;//练琴费
    private int courSuspendedNum;//暂停次数
    private int suspendedSilverBean;//暂停每日银豆值
    private int suspendedGoldBean;//暂停每日金豆值
    private int suspendedMaxsum;//最大暂停天数
    private int isCost;//是否缴纳附加费 0否1是
    private int is_start;//0:未开始 1:学生已上课2：旷课3：待提交资料 4、待评价5、已完成6、取消预约',



    private String couresId;//课程id
    private String paidPrice;//实收金额
    private String status;//状态 0->暂停使用 1->使用中 2已失效
    private String createTime;//创建时间

    private String makeNum;//已预约课程数
    private String cancelNum;//本月可以取消预订数
    private String remainingNum;//本月剩余可以取消预订数

    private String userId;//用户id
    private String courseId;//课程id
    private String avatar;

    private String teacherId;//老师id
    private String teacherName;//老师姓名
    private String isAppraise;//是否评价 0:否 1:是
    private String isStart;//是否开始 0:未开始 1:已结束
    private String addrDes;
    private String campusName;
    private int classLayer;
    private String courseType;//课程类型 0-直播大课 1-一对一辅导

    List<CourseChapterBean> detailVoList;

    public int getIs_start() {
        return is_start;
    }

    public void setIs_start(int is_start) {
        this.is_start = is_start;
    }

    public int getIsCost() {
        return isCost;
    }

    public void setIsCost(int isCost) {
        this.isCost = isCost;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public int getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(int additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public int getSuspendedSilverBean() {
        return suspendedSilverBean;
    }

    public void setSuspendedSilverBean(int suspendedSilverBean) {
        this.suspendedSilverBean = suspendedSilverBean;
    }

    public int getSuspendedGoldBean() {
        return suspendedGoldBean;
    }

    public void setSuspendedGoldBean(int suspendedGoldBean) {
        this.suspendedGoldBean = suspendedGoldBean;
    }

    public int getSuspendedMaxsum() {
        return suspendedMaxsum;
    }

    public void setSuspendedMaxsum(int suspendedMaxsum) {
        this.suspendedMaxsum = suspendedMaxsum;
    }

    public int getCourSuspendedNum() {
        return courSuspendedNum;
    }

    public void setCourSuspendedNum(int courSuspendedNum) {
        this.courSuspendedNum = courSuspendedNum;
    }

    public int getClassLayer() {
        return classLayer;
    }

    public void setClassLayer(int classLayer) {
        this.classLayer = classLayer;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTeacherHeadImg() {
        return teacherHeadImg;
    }

    public void setTeacherHeadImg(String teacherHeadImg) {
        this.teacherHeadImg = teacherHeadImg;
    }

    public List<CourseChapterBean> getDetailVoList() {
        return detailVoList;
    }

    public void setDetailVoList(List<CourseChapterBean> detailVoList) {
        this.detailVoList = detailVoList;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }


    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddrDes() {
        return addrDes;
    }

    public void setAddrDes(String addrDes) {
        this.addrDes = addrDes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getIsAppraise() {
        return isAppraise;
    }

    public void setIsAppraise(String isAppraise) {
        this.isAppraise = isAppraise;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseStatusVal() {
        return courseStatusVal;
    }

    public void setCourseStatusVal(String courseStatusVal) {
        this.courseStatusVal = courseStatusVal;
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

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getCatsList() {
        return catsList;
    }

    public void setCatsList(String catsList) {
        this.catsList = catsList;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getWeeksNumber() {
        return weeksNumber;
    }

    public void setWeeksNumber(String weeksNumber) {
        this.weeksNumber = weeksNumber;
    }

    public String getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getRewardPrice() {
        return rewardPrice;
    }

    public void setRewardPrice(String rewardPrice) {
        this.rewardPrice = rewardPrice;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getCouresName() {
        return couresName;
    }

    public void setCouresName(String couresName) {
        this.couresName = couresName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public String getCouresId() {
        return couresId;
    }

    public void setCouresId(String couresId) {
        this.couresId = couresId;
    }

    public String getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(String paidPrice) {
        this.paidPrice = paidPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMakeNum() {
        return makeNum;
    }

    public void setMakeNum(String makeNum) {
        this.makeNum = makeNum;
    }

    public String getCancelNum() {
        return cancelNum;
    }

    public void setCancelNum(String cancelNum) {
        this.cancelNum = cancelNum;
    }

    public String getRemainingNum() {
        return remainingNum;
    }

    public void setRemainingNum(String remainingNum) {
        this.remainingNum = remainingNum;
    }
}
