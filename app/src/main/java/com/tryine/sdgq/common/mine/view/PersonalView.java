package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.mvp.BaseView;

public interface PersonalView extends BaseView {
    void onUpdateAvatarSuccess(String avatarUrl);
    void onGetUserdetailSuccess(UserBean userBean);
    void onCampusNameSuccess(String  campusName);
    void onUpdateSuccess();
    void onFailed(String message);
}
