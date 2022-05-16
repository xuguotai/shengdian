package com.tryine.sdgq.common.live.activity;

import android.content.Context;
import android.content.Intent;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.common.live.presenter.LivePresenter;
import com.tryine.sdgq.common.live.view.LiveView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.List;

/**
 * 直播间
 * @author: zhangshuaijun
 * @time: 2022-01-04 16:18
 */
public class LiveRoomActivity extends BaseActivity implements LiveView {

    LivePresenter livePresenter;

    String liveRoomId;

    public static void start(Context context, String liveRoomId) {
        Intent intent = new Intent();
        intent.setClass(context, LiveCourseDetailActivity.class);
        intent.putExtra("liveRoomId", liveRoomId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_room;
    }

    @Override
    protected void init() {
        setWhiteBar();
        liveRoomId = getIntent().getStringExtra("liveRoomId");
        livePresenter = new LivePresenter(mContext);
        livePresenter.attachView(this);
    }

    @Override
    public void onLiveCourseBeanSuccess(LiveCourseBean liveCourseBean) {

    }

    @Override
    public void onGetCourseBeanSuccess(List<HomeMenuBean> courseBeanList) {

    }

    @Override
    public void onGetCourseChildBeanSuccess(List<HomeMenuBean> courseChildBeanList) {

    }

    @Override
    public void onGetliveroomdetailSuccess(int liveId, String trtcPushAddr) {

    }

    @Override
    public void onBuyCourseSuccess() {

    }

    @Override
    public void onAddroomSuccess(int mRoomId) {

    }

    @Override
    public void onGetIsLiveSuccess(int isLive, int realStatus, int liveId, String trtcPushAddr) {

    }


    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
