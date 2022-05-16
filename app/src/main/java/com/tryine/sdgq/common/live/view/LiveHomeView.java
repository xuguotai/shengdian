package com.tryine.sdgq.common.live.view;

import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface LiveHomeView extends BaseView {

    void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList);
    void onGetLiveBeanListSuccess(List<LiveBean> liveBeanLists);
    void onGetLiveBeanListSuccess(List<LiveBean> liveBeanLists, int pages);
    void onBuyCourseSuccess();

    void onFailed(String message);
}
