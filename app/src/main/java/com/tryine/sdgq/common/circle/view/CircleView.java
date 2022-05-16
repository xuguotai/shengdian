package com.tryine.sdgq.common.circle.view;

import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface CircleView extends BaseView {
    void onAddCircleSuccess();

    void onUploadFileSuccess(String fileUrl, int type);

    void onGetTopicListSuccess(List<HomeMenuBean> homeMenuBeanList);

    void onGetHdListSuccess(List<HomeMenuBean> homeMenuBeanList);

    void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages);

    void onGetsignatureSuccess(String signature);

    void onGetFansBeanSuccess(List<FansBean> fansBeanList, int pages);

    void onFocusSuccess(String isFocus);

    void onGiveSuccess(String type,int position);

    void onFailed(String message);
}
