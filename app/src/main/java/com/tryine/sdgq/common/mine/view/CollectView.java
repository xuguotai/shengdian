package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface CollectView extends BaseView {
    void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages);
    void onGetVideoModelListSuccess(List<VideoModel> circleBeanList, int pages);
    void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList, int pages);

    void onCollectSuccess();
    void onFailed(String message);
}
