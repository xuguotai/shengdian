package com.tryine.sdgq.common.home.bean;

import java.util.List;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-18 09:23
 */
public class BargainOrderDetailBean {

    private String id;//订单id
    private String orderNo;//订单编号
    private String bargainId;//砍价ID
    private String bargainName;//砍价课程
    private String bargainImgUrl;//砍价课程封面
    private String bargainType;//砍价课程类型 0-线上课 1-线下课
    private String bargainEndTime;//课程结束时间
    private String costPrice;//商品原价
    private String rulingPrice;//已砍价格
    private String currentPrice;//当前价(砍完后价格) - 展示价格
    private String bargainPrice;//还能再砍价格
    private String status;//订单状态  1:砍价中 2:待付款 3:待核销 4:已完成 5:已过期
    private String checkCode;//核销码



    private String orderId;//订单id
    private String userId;//用户id
    private String userName;//用户名称
    private String avatar;//

    private String imgUrl;//砍价商品图片
    private String price;//商品原价
    private String ruingPrice;//当前价（原价-已砍价格）
    private String helpPrice;//帮砍价格
    private String startTime;//活动开始时间
    private String endTime;// 活动结束时间
    private String andUrl;//安卓下载地址
    private String iosUrl;//苹果下载地址
    private String bargainFlag;// 砍价标识

    private List<BargainUserBean> recordVoList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRuingPrice() {
        return ruingPrice;
    }

    public void setRuingPrice(String ruingPrice) {
        this.ruingPrice = ruingPrice;
    }

    public String getHelpPrice() {
        return helpPrice;
    }

    public void setHelpPrice(String helpPrice) {
        this.helpPrice = helpPrice;
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

    public String getAndUrl() {
        return andUrl;
    }

    public void setAndUrl(String andUrl) {
        this.andUrl = andUrl;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getBargainFlag() {
        return bargainFlag;
    }

    public void setBargainFlag(String bargainFlag) {
        this.bargainFlag = bargainFlag;
    }

    public List<BargainUserBean> getRecordVoList() {
        return recordVoList;
    }

    public void setRecordVoList(List<BargainUserBean> recordVoList) {
        this.recordVoList = recordVoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(String bargainPrice) {
        this.bargainPrice = bargainPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
