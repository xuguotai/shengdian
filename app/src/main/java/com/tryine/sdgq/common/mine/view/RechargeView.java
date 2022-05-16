package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;


public interface RechargeView extends BaseView {
    void onRechargeSuccess(String param, int payType);

    void onCreateOrderSuccess(String orderNo);

    void onGetUserbeanSuccess(int goldenBean, int silverBean);

    void onGetUserWalletSuccess(int goldenBean, int goldenBeanobtain, int goldenBeanconp, int silverBean, int silverBeanbtain, int silverBeanconp);

    void onGetWalletListSuccess(List<PayRecordBean> payRecordBeanList, int pages);

    void onGetProportionSuccess(String teaBean, String realStatus, String miniWithdraw, String serviceCharge,String proportion);

    void onWithdrawSuccess();

    void onGetProtocolSuccess(String agreement);

    void onFailed(String message);
}
