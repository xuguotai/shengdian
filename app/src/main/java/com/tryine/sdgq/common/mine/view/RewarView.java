package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface RewarView extends BaseView {
    void onGetRewarBeanListSuccess(List<RewarBean> rewarBeanList, int pages);
    void onGetPayRecordBeanListSuccess(List<PayRecordBean> payRecordBeanList, int pages);
    void onFailed(String message);
}
