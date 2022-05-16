package com.tryine.sdgq.common.circle.view;

import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface TopicView extends BaseView {

    void onGetTopicDetailSuccess(TopicBean topicBean);

    void onGetTopicListSuccess(List<TopicBean> topicBeanList,int pages);

    void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages);

    void onFailed(String message);
}
