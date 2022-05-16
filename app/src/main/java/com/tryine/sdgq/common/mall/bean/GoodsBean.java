package com.tryine.sdgq.common.mall.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-15 13:22
 */
public class GoodsBean implements Serializable {

    private String id;//
    private String name;//商品名称
    private String videoUrl;// 商品视频
    private String videoCoverUrl;//视频封面图
    private String imgUrl;//商品图片
    private String coverPath;//产品封面图
    private String typeOneId;//商品一级分类id
    private String marketPrice;//市场价
    private String discountPrice;//折后价
    private String detail;//商品详情
    private String status;//状态 0:下架 1:上架
    private String salesVolume;// 销量

    private String goodsId;// 商品id
    private int quantity;// 添加到购物车数量
    private String price;// 添加到购物车的价格
    private String goodsFaceImage;// 商品主图
    private String goodsName;// 商品名称
    private String goodsTypeId;// 商品分类
    private String iscollection;// 是否收藏0否1是
    private int stock;// 库存
    private int beanType;// 0金豆 1银豆
    private int isInput;//是否显示输入 0-否 1-是

    private boolean checked;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIsInput() {
        return isInput;
    }

    public void setIsInput(int isInput) {
        this.isInput = isInput;
    }

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    public String getIscollection() {
        return iscollection;
    }

    public void setIscollection(String iscollection) {
        this.iscollection = iscollection;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoodsFaceImage() {
        return goodsFaceImage;
    }

    public void setGoodsFaceImage(String goodsFaceImage) {
        this.goodsFaceImage = goodsFaceImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoCoverUrl() {
        return videoCoverUrl;
    }

    public void setVideoCoverUrl(String videoCoverUrl) {
        this.videoCoverUrl = videoCoverUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getTypeOneId() {
        return typeOneId;
    }

    public void setTypeOneId(String typeOneId) {
        this.typeOneId = typeOneId;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }
}
