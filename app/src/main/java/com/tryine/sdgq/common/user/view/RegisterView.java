package com.tryine.sdgq.common.user.view;


import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.mvp.BaseView;

public interface RegisterView extends BaseView {

    void onLoginSuccess(UserBean bean);
    void onRegisterSuccess();
    void onCodeSuccess();
    void onUpdatepasswordSuccess();

    void onFailed(String message);
}
