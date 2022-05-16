package com.tryine.sdgq.common.mine.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-11 14:34
 */
public class PayRecordBean {

    private String id;//记录id
    private String userId;//用户id
    private String beanType;// 豆子类型 0-金豆 1-银豆
    private String beanCount;//豆子数量
    private String beanCountVal;//豆子数量描述
    private String payType;//
    private String payTypeVal;//支付类型描述
    private String type;//类型 0-获得 1-消耗
    private String isTransfer;//是否转赠 0-否 1-是
    private String receUserId;//转赠接收用户id
    private String createTime;//创建时间
    private String remake;//转增原因

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
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

    public String getBeanType() {
        return beanType;
    }

    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public String getBeanCount() {
        return beanCount;
    }

    public void setBeanCount(String beanCount) {
        this.beanCount = beanCount;
    }

    public String getBeanCountVal() {
        return beanCountVal;
    }

    public void setBeanCountVal(String beanCountVal) {
        this.beanCountVal = beanCountVal;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeVal() {
        return payTypeVal;
    }

    public void setPayTypeVal(String payTypeVal) {
        this.payTypeVal = payTypeVal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(String isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getReceUserId() {
        return receUserId;
    }

    public void setReceUserId(String receUserId) {
        this.receUserId = receUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
