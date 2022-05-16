package com.tryine.sdgq.common.live.tencent.liteav.tuigift.view;

import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;

/**
 * 礼物发送结果监听
 */
public interface TUIGiftListener {
    void onSuccess(int code, String msg, TUIGiftModel giftModel);

    void onFailed(int code, String msg);
}