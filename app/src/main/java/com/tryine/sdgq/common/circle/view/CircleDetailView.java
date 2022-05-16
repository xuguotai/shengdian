package com.tryine.sdgq.common.circle.view;

import com.tryine.sdgq.common.circle.bean.CircleCommentBean;
import com.tryine.sdgq.common.circle.bean.CircleDetailBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;


public interface CircleDetailView extends BaseView {

    void onGetCircleDetailSuccess(CircleDetailBean circleDetailBean);

    void onGetCircleCommentBeanListSuccess(List<CircleCommentBean> circleCommentBeanLists, int pages);

    void onCommentSuccess();

    void onHfCommentSuccess();

    void onGiveSuccess();

    void onFocusSuccess();

    void onReplycommentSuccess();

    void onGetCircleCommentBeanChildListSuccess(List<CircleCommentBean> circleCommentBeanLists, int pageNum, int pages, int selectedPosition);

    void onFailed(String message);
}
