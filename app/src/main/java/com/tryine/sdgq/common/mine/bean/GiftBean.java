package com.tryine.sdgq.common.mine.bean;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-10 13:27
 */
public class GiftBean {

    private String giftId;        //礼物Id
    private String giftImageUrl;  //礼物图片Url
    private String lottieUrl;     //礼物动画Url
    private String title;         //礼物名称
    private String sdType;         //礼物名称
    private String price;         //价格

    public String getSdType() {
        return sdType;
    }

    public void setSdType(String sdType) {
        this.sdType = sdType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftImageUrl() {
        return giftImageUrl;
    }

    public void setGiftImageUrl(String giftImageUrl) {
        this.giftImageUrl = giftImageUrl;
    }

    public String getLottieUrl() {
        return lottieUrl;
    }

    public void setLottieUrl(String lottieUrl) {
        this.lottieUrl = lottieUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
