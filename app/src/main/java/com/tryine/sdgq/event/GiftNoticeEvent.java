package com.tryine.sdgq.event;

import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;

public class GiftNoticeEvent {
    TUIGiftModel model;

    public GiftNoticeEvent(TUIGiftModel model) {
        this.model = model;
    }

    public TUIGiftModel getModel() {
        return model;
    }

    public void setModel(TUIGiftModel model) {
        this.model = model;
    }
}
