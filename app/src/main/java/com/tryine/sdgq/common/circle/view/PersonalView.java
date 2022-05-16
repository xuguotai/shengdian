package com.tryine.sdgq.common.circle.view;

import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.bean.PersonalBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface PersonalView extends BaseView {

    void onGetPersonaBeanSuccess(PersonalBean personalBean);

    void onGetLabelListSuccess(List<LabelBean> labelBeanList);

    void onUpdateUserInfoSuccess();

    void onUpdateLabelSuccess();

    void onFocusSuccess();

    void onDeletePyqSuccess();


    void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages);

    void onGetTopicBeanListSuccess(List<TopicBean> topicBeanList, int pages);

    void onGetVideoBeanListSuccess(List<VideoModel> videoModels, int pages);

    void onGiveSuccess(String type,int position);

    void onFailed(String message);
}
