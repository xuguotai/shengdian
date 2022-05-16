package com.tryine.sdgq.common.mall.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-06 14:57
 */
public class OrderGoodsBean implements Serializable {

    private String detailId;
    private String goodsId;// 商品id
    private String goodsName;// 商品名称
    private String goodsImgUrl;// 商品图片
    private int count;// 购买数量
    private String unitPrice;// 商品单价
    private String detailPayPrice;// 实付金额
    private String detailOrderNo;// 订单编号

    private String status;
    private String statusVal;//订单状态描述
    private String createTime;//下单时间
    private String autoCancelTime;//自动取消时间
    private String longAutoCancelTime;//自动取消时间时间戳
    private String pickAddress;//自提校区
    private String receiptId;//自提校区id
    private String receiptAddress;//收货地址
    private int beanType;// 0金豆 1银豆

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getLongAutoCancelTime() {
        return longAutoCancelTime;
    }

    public void setLongAutoCancelTime(String longAutoCancelTime) {
        this.longAutoCancelTime = longAutoCancelTime;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDetailPayPrice() {
        return detailPayPrice;
    }

    public void setDetailPayPrice(String detailPayPrice) {
        this.detailPayPrice = detailPayPrice;
    }

    public String getDetailOrderNo() {
        return detailOrderNo;
    }

    public void setDetailOrderNo(String detailOrderNo) {
        this.detailOrderNo = detailOrderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(String statusVal) {
        this.statusVal = statusVal;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAutoCancelTime() {
        return autoCancelTime;
    }

    public void setAutoCancelTime(String autoCancelTime) {
        this.autoCancelTime = autoCancelTime;
    }
}
