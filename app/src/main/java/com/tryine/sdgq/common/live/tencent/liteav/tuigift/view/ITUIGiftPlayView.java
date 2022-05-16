package com.tryine.sdgq.common.live.tencent.liteav.tuigift.view;


import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;

public interface ITUIGiftPlayView {
    /**
     * 接收礼物
     * @param giftModel 收到的礼物
     */
    void receiveGift(TUIGiftModel giftModel);
}
