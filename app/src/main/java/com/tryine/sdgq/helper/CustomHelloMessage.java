package com.tryine.sdgq.helper;

import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tryine.sdgq.Application;
import com.tryine.sdgq.R;

/**
 * 自定义消息的bean实体，用来与json的相互转化
 */
public class CustomHelloMessage {
    public String cmd = "";
    public String businessID = TUIKitConstants.BUSINESS_ID_CUSTOM_HELLO;
    public String text = Application.getApplication().getString(R.string.test_custom_action);
    public String link = "https://cloud.tencent.com/document/product/269/3794";
    public String sendUserId = "";

    public int version = TUIKitConstants.JSON_VERSION_UNKNOWN;
}
