package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftBean;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface LiveInView extends BaseView {
    void onCloseRoomSuccess();

    void onSendPresentSuccess(TUIGiftModel giftModel);

    void onGiftBeanListSuccess(List<GiftBean> giftBeanList);

    void onGetUserbeanSuccess(int goldenBean, int silverBean);

    void onGetliveroomdetailSuccess(String pushAddr);

    void onGetliveroomdetailSuccess(String userId, String teacherName, String teacherHead, String mPlayURL,String streamName);

    void onGetUsertrtcurlSuccess(String pushUrl, String playUrl);

//    void onGetLiveDetailbeanSuccess();

    void onGetUserdetailSuccess(UserBean userBean);

    void onFailed(String message);
}
