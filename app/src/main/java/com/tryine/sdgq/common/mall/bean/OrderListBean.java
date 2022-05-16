package com.tryine.sdgq.common.mall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-27 15:07
 */
public class OrderListBean implements Serializable {

    private String id;
    private String orderNo;//订单编号
    private String totalNum;//总件数
    private String totalPayPrice;//实际支付总费用
    private String status;
    private String statusVal;//订单状态描述
    private String createTime;//下单时间
    private String autoCancelTime;//自动取消时间
    private String longAutoCancelTime;//自动取消时间时间戳
    private String detailOrderNo;
    private String receiptId;//自提校区id
    private String pickAddress;//自提校区
    private String receiptAddress;//收货地址

    List<OrderGoodsBean> piratesOrderDetailListVoList;

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
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

    public String getDetailOrderNo() {
        return detailOrderNo;
    }

    public void setDetailOrderNo(String detailOrderNo) {
        this.detailOrderNo = detailOrderNo;
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

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalPayPrice() {
        return totalPayPrice;
    }

    public void setTotalPayPrice(String totalPayPrice) {
        this.totalPayPrice = totalPayPrice;
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

    public String getLongAutoCancelTime() {
        return longAutoCancelTime;
    }

    public void setLongAutoCancelTime(String longAutoCancelTime) {
        this.longAutoCancelTime = longAutoCancelTime;
    }

    public List<OrderGoodsBean> getPiratesOrderDetailListVoList() {
        return piratesOrderDetailListVoList;
    }

    public void setPiratesOrderDetailListVoList(List<OrderGoodsBean> piratesOrderDetailListVoList) {
        this.piratesOrderDetailListVoList = piratesOrderDetailListVoList;
    }



}
