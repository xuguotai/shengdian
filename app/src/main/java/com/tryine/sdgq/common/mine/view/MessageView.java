package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.mine.bean.MessageBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface MessageView extends BaseView {
    void onGetMessageBeanListSuccess(List<MessageBean> messageBeanList, int pages);

    void onGetMessagenumberSuccess(int sysSum, int thumbSum, int shareSum, int commentsSum, int exceptionalSum);

    void onAllReadSuccess();

    void onFailed(String message);
}
