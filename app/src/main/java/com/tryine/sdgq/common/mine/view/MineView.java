package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface MineView extends BaseView {
    void onGetUserdetailSuccess(UserBean userBean);

    void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList);

    void onGetFansBeanSuccess(List<FansBean> fansBeanList, int pages);

    void onFocusSuccess();

    void onUpdatepasswordSuccess();

    void onCodeSuccess();

    void onGetIsLiveSuccess(int isLive, int realStatus, int liveId, String trtcPushAddr);

    void onGetaboutinfoSuccess(String rewardDesc);

    void onFailed(String message);
}
