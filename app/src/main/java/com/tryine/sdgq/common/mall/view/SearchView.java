package com.tryine.sdgq.common.mall.view;

import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface SearchView extends BaseView {

    void onGetLabelBeanSuccess(List<LabelBean> labelBeanList);

    void onFailed(String message);
}
