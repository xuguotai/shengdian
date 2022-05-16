package com.tryine.sdgq.common.live.view;

import com.tryine.sdgq.mvp.BaseView;


public interface ProtocolView extends BaseView {

    void onGetProtocolSuccess(String agreement);

    void onFailed(String message);
}
