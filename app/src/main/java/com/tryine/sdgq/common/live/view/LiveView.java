package com.tryine.sdgq.common.live.view;

import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;


public interface LiveView extends BaseView {

    void onLiveCourseBeanSuccess(LiveCourseBean liveCourseBean);

    void onGetCourseBeanSuccess(List<HomeMenuBean> courseBeanList);

    void onGetCourseChildBeanSuccess(List<HomeMenuBean> courseChildBeanList);

    void onGetliveroomdetailSuccess(int liveId,String trtcPushAddr);

    void onBuyCourseSuccess();

    void onAddroomSuccess(int mRoomId);

    void onGetIsLiveSuccess(int isLive, int realStatus, int liveId,String trtcPushAddr);

    void onFailed(String message);
}
