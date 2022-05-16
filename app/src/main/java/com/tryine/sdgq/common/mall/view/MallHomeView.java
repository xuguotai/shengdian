package com.tryine.sdgq.common.mall.view;

import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface MallHomeView extends BaseView {

    void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList);
    void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList, int pages);
    void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList);


    void onFailed(String message);
}
