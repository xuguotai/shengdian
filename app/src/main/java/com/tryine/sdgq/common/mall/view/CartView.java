package com.tryine.sdgq.common.mall.view;

import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface CartView extends BaseView {

    void onGetCartListSuccess(List<GoodsBean> goodsBeanList, int pages);

    void onUpdateCarSuccess(int quantity, int position);

    void onGoodsCarDelSuccess();

    void onCreateOrderSuccess(String orderNo,String totalPrice);

    void onPayOrderSuccess();

    void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList, int pages);

    void onFailed(String message);
}
