package com.tryine.sdgq.common.mall.view;

import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.bean.GoodsCommentBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;


public interface GoodsDetailView extends BaseView {

    void onGetGoodsDetailSuccess(GoodsBean goodsBean);
    void onGetGoodsCommentListSuccess(List<GoodsCommentBean> goodsCommentBeans, int pages);
    void onaddCarSuccess();
    void onGiveSuccess();


    void onFailed(String message);
}
