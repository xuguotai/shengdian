package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.mvp.BaseView;

public interface TeacherView extends BaseView {
    void onAddTeacherSuccess();
    void onFailed(String message);
}
