package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.ShareInfoBean;

public interface ShareView extends BaseView {
    void onSuccess(int type, ShareInfoBean bean);
    void onFailed(String message);
}
