package com.tryine.sdgq.common.mall.view;

import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.bean.OrderListBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface OrderView extends BaseView {

    void onGetOrderBeanListSuccess(List<OrderListBean> orderListBeanList, int pages);

    void onGetOrderBeanSuccess(OrderListBean orderListBean);

    void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList);

    void onCancelorderSuccess();

    void onDetermineSuccess();

    void onPayOrderSuccess();

    void onRefundSuccess();

    void onCommentSuccess(int i);

    void onUploadFileSuccess(String fileUrl, int type);

    void onFailed(String message);
}
