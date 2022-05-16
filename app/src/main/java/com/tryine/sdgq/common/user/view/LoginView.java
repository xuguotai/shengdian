package com.tryine.sdgq.common.user.view;

import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.mvp.BaseView;

public interface LoginView extends BaseView {
    void onCodeSuccess();

    void onLoginSuccess(UserBean bean);

    void onBind(String type, String openId, String nickName, String headimgurl);

    void onGetUsersign(String userSign);

    void onFailed(String message);
}
