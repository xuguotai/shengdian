package com.tryine.sdgq.common.live.tencent.liteav.tuigift.view;

import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;

public interface ITUIGiftListPanelView {
    /**
     * 发送礼物
     *
     * @param giftModel 待发送礼物
     */
    void sendGift(TUIGiftModel giftModel);
}
