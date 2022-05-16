package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.mine.bean.KtzlBean;
import com.tryine.sdgq.common.mine.bean.TaskBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface TaskView extends BaseView {
    void onGetTaskBeanListSuccess(List<TaskBean> taskBeanLists);
    void onSigninSuccess();
    void onReceiveSuccess();
    void onGetContinuesignSuccess(int count);
    void onGetKtzlBeanSuccess(List<KtzlBean> ktzlBeanList, int pages);
    void onFailed(String message);
}
