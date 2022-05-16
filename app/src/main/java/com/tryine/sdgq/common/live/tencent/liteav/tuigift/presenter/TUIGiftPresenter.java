package com.tryine.sdgq.common.live.tencent.liteav.tuigift.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tencent.qcloud.tuicore.TUILogin;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModel;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModelManager;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftBean;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftConstants;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftIMService;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.TUIGiftListPanelView;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.TUIGiftPlayView;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.mine.presenter.LiveInPresenter;
import com.tryine.sdgq.common.mine.view.LiveInView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 礼物Presenter
 */
public class TUIGiftPresenter implements LiveInView {
    private static TUIGiftPresenter sInstance;

    private TUIGiftPlayView mPlayView;
    private TUIGiftListPanelView mPanelView;
    private String mGroupId;
    private TUIGiftIMService mImService;
    private Context mContext;
    private TUIGiftDataDownload mGiftDataDownload;

    LiveInPresenter liveInPresenter;

    public TUIGiftPresenter() {
        mGiftDataDownload = new TUIGiftDataDownload();
        mGiftDataDownload.setGiftListQuery(new TUIGiftListQueryImpl());

    }

    public void initLiveInPresenter(Context context) {
        liveInPresenter = new LiveInPresenter(context);
        liveInPresenter.attachView(this);
        liveInPresenter.getiGiftList(1);
    }


    public static synchronized TUIGiftPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new TUIGiftPresenter();
        }
        return sInstance;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void initGiftPlayView(TUIGiftPlayView playView, String groupId) {
        this.mPlayView = playView;
        mGroupId = groupId;
        initIMService();
    }

    public void initGiftPanelView(TUIGiftListPanelView panelView, String groupId) {
        this.mPanelView = panelView;
        mGroupId = groupId;
        initIMService();
    }

    public TUIGiftPlayView getPlayView() {
        return mPlayView;
    }

    /**
     * 初始化IM
     */
    private void initIMService() {
        if (mImService == null) {
            mImService = new TUIGiftIMService(mGroupId);
            mImService.setPresenter(this);
        }
        mImService.setGroupId(mGroupId);
    }

    public void setGroupId(String groupId) {
        mGroupId = groupId;
    }

    /**
     * 发送礼物消息
     *
     * @param giftModel 待发送礼物信息
     * @param callback  发送结果回调
     */
    public void sendGroupGiftMessage(final TUIGiftModel giftModel, final TUIGiftCallBack.GiftSendCallBack callback) {
        mImService.sendGroupGiftMessage(giftModel, new TUIGiftCallBack.ActionCallBack() {
            @Override
            public void onCallback(int code, String msg) {
                if (code != 0) {
                    callback.onFailed(code, msg);
                } else {
                    final UserModel userModel = UserModelManager.getInstance().getUserModel();
                    giftModel.extInfo.put(TUIGiftConstants.KEY_USER_NAME, "我");
                    giftModel.extInfo.put(TUIGiftConstants.KEY_USER_AVATAR, userModel.userAvatar);
                    giftModel.extInfo.put(TUIGiftConstants.KEY_USER_ID, userModel.userId);
                    callback.onSuccess(code, msg, giftModel);
                }
            }
        });
    }

    /**
     * 接收礼物消息
     *
     * @param groupId   接收的群组groupId
     * @param giftModel 接收到的礼物信息
     */
    public void recvGroupGiftMessage(String groupId, TUIGiftModel giftModel) {
        if (mGroupId != null && mGroupId.equals(groupId)) {
            mPlayView.receiveGift(giftModel);
        }
    }

    /**
     * 初始化礼物面板
     */
    public void initGiftData() {
//        mGiftDataDownload.queryGiftInfoList(new TUIGiftDataDownload.GiftQueryCallback() {
//            @Override
//            public void onQuerySuccess(final List<TUIGiftModel> giftInfoList) {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPanelView.setGiftModelSource(giftInfoList);
//                        mPanelView.init(mGroupId);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onQueryFailed(String errorMsg) {
//            }
//        });
    }

    @Override
    public void onCloseRoomSuccess() {

    }

    @Override
    public void onSendPresentSuccess(TUIGiftModel giftModel) {

    }

    @Override
    public void onGiftBeanListSuccess(List<GiftBean> giftBeanList) {

        List<TUIGiftModel> tuiGiftModels = new ArrayList<>();
        for (GiftBean bean : giftBeanList) {
            TUIGiftModel giftModel = new TUIGiftModel();
            giftModel.giftId = bean.getGiftId();
            giftModel.giveDesc = bean.getTitle();
            giftModel.normalImageUrl = bean.getGiftImageUrl();
            giftModel.animationUrl = bean.getLottieUrl();
            giftModel.price = bean.getPrice();
            giftModel.sdType = bean.getSdType();
            tuiGiftModels.add(giftModel);
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPanelView.setGiftModelSource(tuiGiftModels);
                mPanelView.init(mGroupId);
            }
        });
    }

    @Override
    public void onGetUserbeanSuccess(int goldenBean, int silverBean) {

    }

    @Override
    public void onGetliveroomdetailSuccess(String pushAddr) {

    }

    @Override
    public void onGetliveroomdetailSuccess(String userId, String teacherName, String teacherHead, String mPlayURL, String streamName) {

    }

    @Override
    public void onGetUsertrtcurlSuccess(String pushUrl, String playUrl) {

    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


}
