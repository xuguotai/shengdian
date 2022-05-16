package com.tryine.sdgq.common.home.bean;

import java.util.List;

/**
 * 砍价
 *
 * @author: zhangshuaijun
 * @time: 2021-11-22 15:01
 */
public class BargainBean {

    private String id;//砍价活动id
    private String name;//课程名称
    private String imgUrl;//封面图片
    private String type;// 课程类型 0:直播课 1:线下课程
    private String price;//商品价格
    private String appointPrice;//指定金额
    private String bargainCount;//已砍人数
    private String startTime;//活动开始时间
    private String endTime;//活动结束时间

    private String orderNo;//订单编号
    private String userId;//用户ID
    private String bargainId;//砍价ID
    private String bargainName;//砍价课程
    private String bargainImgUrl;//砍价课程封面
    private String bargainType;//砍价课程类型 0-线上课 1-线下课
    private String bargainEndTime;//活动结束时间
    private String costPrice;//商品原价
    private String rulingPrice;//已砍价格
    private String currentPrice;//当前价(砍完后价格)
    private String status;//订单状态  1:砍价中 2:待付款 3:待核销 4:已完成 5:已过期
    private String payPrice;//实际支付价格
    private String payType;//支付方式：0-金豆支付 1-线下支付
    private String payTime;//支付时间
    private String isPay;//是否支付：0.未支付 1.已支付
    private String checkCode;//核销码
    private String checkUserId;//核销用户id
    private String finishTime;//完成时间
    private String cancelTime;//过期时间
    private String createBy;//
    private String createTime;//创建时间
    private String helpCount;//帮砍人数

    List<String> userHeadImgList;
    List<String> helpHeadImgList;

    public String getHelpCount() {
        return helpCount;
    }

    public void setHelpCount(String helpCount) {
        this.helpCount = helpCount;
    }

    public List<String> getHelpHeadImgList() {
        return helpHeadImgList;
    }

    public void setHelpHeadImgList(List<String> helpHeadImgList) {
        this.helpHeadImgList = helpHeadImgList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBargainId() {
        return bargainId;
    }

    public void setBargainId(String bargainId) {
        this.bargainId = bargainId;
    }

    public String getBargainName() {
        return bargainName;
    }

    public void setBargainName(String bargainName) {
        this.bargainName = bargainName;
    }

    public String getBargainImgUrl() {
        return bargainImgUrl;
    }

    public void setBargainImgUrl(String bargainImgUrl) {
        this.bargainImgUrl = bargainImgUrl;
    }

    public String getBargainType() {
        return bargainType;
    }

    public void setBargainType(String bargainType) {
        this.bargainType = bargainType;
    }

    public String getBargainEndTime() {
        return bargainEndTime;
    }

    public void setBargainEndTime(String bargainEndTime) {
        this.bargainEndTime = bargainEndTime;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getRulingPrice() {
        return rulingPrice;
    }

    public void setRulingPrice(String rulingPrice) {
        this.rulingPrice = rulingPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAppointPrice() {
        return appointPrice;
    }

    public void setAppointPrice(String appointPrice) {
        this.appointPrice = appointPrice;
    }

    public String getBargainCount() {
        return bargainCount;
    }

    public void setBargainCount(String bargainCount) {
        this.bargainCount = bargainCount;
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

    public List<String> getUserHeadImgList() {
        return userHeadImgList;
    }

    public void setUserHeadImgList(List<String> userHeadImgList) {
        this.userHeadImgList = userHeadImgList;
    }


}
