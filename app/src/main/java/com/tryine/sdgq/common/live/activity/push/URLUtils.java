package com.tryine.sdgq.common.live.activity.push;

import com.tryine.sdgq.common.live.tencent.liteav.basic.GenerateTestUserSig;


/**
 * MLVB 移动直播地址生成
 * 详情请参考：「https://cloud.tencent.com/document/product/454/7915」
 * <p>
 * <p>
 * Generating Streaming URLs
 * See [https://cloud.tencent.com/document/product/454/7915].
 */
public class URLUtils {

    public static final String WEBRTC      = "webrtc://";
    public static final String RTMP        = "rtmp://";
    public static final String HTTP        = "http://";
    public static final String TRTC        = "trtc://";
    public static final String TRTC_DOMAIN = "cloud.tencent.com";
    public static final String APP_NAME    = "live";

    /**
     * 生成推流地址
     * Generating Publishing URLs
     *
     * @param streamId
     * @param userId
     * @param type 0:RTC  1：RTMP
     * @return
     */
    public static String generatePushUrl(String streamId, String userId, int type){
        String pushUrl = "";
        if(type == 0){
            pushUrl = TRTC + TRTC_DOMAIN + "/push/" + streamId + "?sdkappid=" + GenerateTestUserSig.SDKAPPID + "&userid=" + userId + "&usersig=" + GenerateTestUserSig.genTestUserSig(userId);
        }
        return pushUrl;
    }

    /**
     * 生成拉流地址
     * Generating Playback URLs
     *
     * @param streamId
     * @param userId
     * @param type type 0:RTC  1：RTMP 2:WEBRTC
     * @return
     */
    public static String generatePlayUrl(String streamId, String userId, int type){
        String playUrl = "";
        if(type == 0){
            playUrl = TRTC + TRTC_DOMAIN + "/play/" + streamId + "?sdkappid=" + GenerateTestUserSig.SDKAPPID + "&userid=" + userId + "&usersig=" + GenerateTestUserSig.genTestUserSig(userId);
        }
        return playUrl;
    }
}
