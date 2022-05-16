package com.tryine.sdgq.common.circle.view;

import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface BannerView extends BaseView {
    void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList);

    void onFailed(String message);
}
