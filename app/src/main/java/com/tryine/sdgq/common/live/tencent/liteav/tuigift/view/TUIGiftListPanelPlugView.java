package com.tryine.sdgq.common.live.tencent.liteav.tuigift.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.presenter.TUIGiftPresenter;
import com.tryine.sdgq.common.mine.activity.TaskHomeActivity;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.mine.presenter.LiveInPresenter;
import com.tryine.sdgq.common.mine.view.LiveInView;
import com.tryine.sdgq.common.mine.wallet.JDRechargeActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.ToastUtil;

import java.util.List;


/**
 * 礼物面板
 */
public class TUIGiftListPanelPlugView extends BottomSheetDialog implements LiveInView {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private TUIGiftListPanelView mPanelView;
    private TUIGiftPresenter mPresenter;
    private String mGroupId;
    TextView tv_jzd;
    TextView tv_yzd;

    LiveInPresenter liveInPresenter;

    public TUIGiftListPanelPlugView(Context context, String groupId) {
        super(context, R.style.TUIGiftListPanelViewTheme);
        mContext = context;
        setContentView(R.layout.tuigift_panel);
        this.mGroupId = groupId;
        init();
    }

    private void init() {
        mLayoutInflater = LayoutInflater.from(mContext);
        mPanelView = findViewById(R.id.gift_panel_view_pager);
        tv_jzd = findViewById(R.id.tv_jzd);
        tv_yzd = findViewById(R.id.tv_yzd);
        mPanelView.setTextView(tv_jzd, tv_yzd);
        setCanceledOnTouchOutside(true);

        tv_jzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JDRechargeActivity.start(mContext);
            }
        });
        tv_yzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskHomeActivity.start(mContext);
            }
        });


    }

    public void show() {
        super.show();
        initGiftData();
    }

    /**
     * 初始化礼物面板
     */
    public void initGiftData() {
        if (mPresenter == null) {
            mPresenter = mPanelView.getPresenter(mGroupId);
        }
        mPresenter.initGiftData();

        liveInPresenter = new LiveInPresenter(mContext);
        liveInPresenter.attachView(this);
        liveInPresenter.getUserbean();
    }

    /**
     * 设置Listener处理发送礼物事件
     */
    public void setListener() {
        if (mPresenter == null) {
            mPresenter = mPanelView.getPresenter(mGroupId);
        }
        mPanelView.setListener(new TUIGiftListener() {
            @Override
            public void onSuccess(int code, String msg, TUIGiftModel giftModel) {
                if (mPresenter != null && mPresenter.getPlayView() != null) {
                    mPresenter.getPlayView().receiveGift(giftModel);
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });

    }

    @Override
    public void onCloseRoomSuccess() {

    }

    @Override
    public void onSendPresentSuccess(TUIGiftModel giftModel) {

    }

    @Override
    public void onGiftBeanListSuccess(List<GiftBean> giftBeanList) {

    }

    @Override
    public void onGetUserbeanSuccess(int goldenBean, int silverBean) {
        tv_jzd.setText(goldenBean + " SD金豆");
        tv_yzd.setText(silverBean + " SD银豆");

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
