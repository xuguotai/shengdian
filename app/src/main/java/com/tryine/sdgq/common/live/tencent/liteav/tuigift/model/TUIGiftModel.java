package com.tryine.sdgq.common.live.tencent.liteav.tuigift.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 礼物信息Model
 */
public class TUIGiftModel {

    public String giftId;            //礼物id
    public String normalImageUrl;    //非选中图片
    public String selectedImageUrl;  //选中图片
    public String animationUrl;      //礼物全屏动画url
    public String giveDesc;          //赠送文案
    public String price;          //价格
    public String sdType;          //类型 0:金豆礼物 1:银豆礼物

    public Map<String, String> extInfo = new HashMap<>();  //礼物附加信息

    /**
     * 拷贝礼物基础属性
     *
     * @return 礼物信息
     */
    public TUIGiftModel copy() {
        TUIGiftModel giftModel = new TUIGiftModel();
        giftModel.giftId = this.giftId;
        giftModel.normalImageUrl = this.normalImageUrl;
        giftModel.animationUrl = this.animationUrl;
        giftModel.giveDesc = this.giveDesc;
        giftModel.selectedImageUrl = this.selectedImageUrl;
        giftModel.extInfo = this.extInfo;

        return giftModel;
    }

    @Override
    public String toString() {
        return "TUIGiftModel{" +
                "giftId='" + giftId + '\'' +
                ", normalImageUrl='" + normalImageUrl + '\'' +
                ", selectedImageUrl='" + selectedImageUrl + '\'' +
                ", animationUrl='" + animationUrl + '\'' +
                ", giveDesc='" + giveDesc + '\'' +
                ", price='" + price + '\'' +
                ", sdType='" + sdType + '\'' +
                ", extInfo=" + extInfo +
                '}';
    }
}
