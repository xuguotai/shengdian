package com.tryine.sdgq.common.home.view;

import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.BargainOrderDetailBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface BargainView extends BaseView {
    void onGetBargainBeanListSuccess(List<BargainBean> bargainBeanList,int pages);
    void onGetCampusBeanSuccess(List<CampusBean> campusBeanList);
    void onSaveorderSuccess(String orderId);
    void onGetBargainOrderDetailBeanSuccess(BargainOrderDetailBean bargainOrderDetailBean);
    void onBargainBuySuccess();

    void onFailed(String message);
}
