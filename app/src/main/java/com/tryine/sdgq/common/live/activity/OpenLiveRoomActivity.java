package com.tryine.sdgq.common.live.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.activity.push.CameraPushMainActivity;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.common.live.presenter.LivePresenter;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.anchor.TCCameraAnchorActivity;
import com.tryine.sdgq.common.live.view.LiveView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.VideoTypeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-05 11:15
 */
public class OpenLiveRoomActivity extends BaseActivity implements LiveView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_courseType)
    TextView tv_courseType;
    @BindView(R.id.ll_course)
    LinearLayout ll_course;
    @BindView(R.id.tv_course)
    TextView tv_course;
    @BindView(R.id.ll_courseChild)
    LinearLayout ll_courseChild;
    @BindView(R.id.tv_courseChild)
    TextView tv_courseChild;

    @BindView(R.id.et_videoTitle)
    EditText et_videoTitle;


    @BindView(R.id.cb_agree)
    CheckBox cb_agree;

    /**
     * 课程类型
     */
    List<HomeMenuBean> courseTypeList = new ArrayList<>();
    HomeMenuBean courseTypeBean;
    /**
     * 课程
     */
    List<HomeMenuBean> courseBeanList = new ArrayList<>();
    HomeMenuBean courseBean;
    /**
     * 课程章节
     */
    List<HomeMenuBean> courseChildBeanList = new ArrayList<>();
    HomeMenuBean courseChildBean;


    LivePresenter livePresenter;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, OpenLiveRoomActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_open_liveroom;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("开直播");
        livePresenter = new LivePresenter(this);
        livePresenter.attachView(this);
        verifyStoragePermissions();

        HomeMenuBean homeMenuBean = new HomeMenuBean();
        homeMenuBean.setId("0");
        homeMenuBean.setName("直播大课");
        HomeMenuBean homeMenuBean1 = new HomeMenuBean();
        homeMenuBean1.setId("1");
        homeMenuBean1.setName("一对一辅导");
        courseTypeList.add(homeMenuBean);
        courseTypeList.add(homeMenuBean1);

    }

    @OnClick({R.id.iv_black, R.id.ll_courseType, R.id.ll_course1, R.id.ll_courseChild1, R.id.ll_btn_open, R.id.tv_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_courseType:
                if (null != courseTypeList) {
                    VideoTypeDialog videoTypeDialog = new VideoTypeDialog(this, courseTypeList);
                    videoTypeDialog.show();
                    videoTypeDialog.setOnItemClickListener(new VideoTypeDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(HomeMenuBean homeMenuBean) {
                            courseTypeBean = homeMenuBean;
                            tv_courseType.setText(homeMenuBean.getName());
                            livePresenter.selectlivecourse(courseTypeBean.getId());

                            courseBean = null;
                            tv_course.setText("");
                            ll_course.setVisibility(View.VISIBLE);
                            ll_courseChild.setVisibility(View.GONE);

                        }
                    });
                }
                break;
            case R.id.ll_course1:
                if (null != courseBeanList) {
                    VideoTypeDialog videoTypeDialog = new VideoTypeDialog(this, courseBeanList);
                    videoTypeDialog.show();
                    videoTypeDialog.setOnItemClickListener(new VideoTypeDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(HomeMenuBean homeMenuBean) {
                            courseBean = homeMenuBean;
                            tv_course.setText(homeMenuBean.getName());

                            courseChildBean = null;
                            tv_courseChild.setText("");
                            livePresenter.selectlivecoursedetail(courseBean.getId());
                            ll_courseChild.setVisibility(View.VISIBLE);

                        }
                    });
                }
                break;
            case R.id.ll_courseChild1:
                if (null != courseChildBeanList) {
                    VideoTypeDialog videoTypeDialog = new VideoTypeDialog(this, courseChildBeanList);
                    videoTypeDialog.show();
                    videoTypeDialog.setOnItemClickListener(new VideoTypeDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(HomeMenuBean homeMenuBean) {
                            courseChildBean = homeMenuBean;
                            tv_courseChild.setText(homeMenuBean.getName());
                        }
                    });
                }
                break;
            case R.id.ll_btn_open:
                open();
//                Intent intent = new Intent(mContext, TCCameraAnchorActivity.class);
//                intent.putExtra("mRoomId",17);
//                startActivity(intent);
                break;
            case R.id.tv_agreement:
                ProtocolActivity.start(mContext, 0);
                break;


        }
    }

    private void open() {

        if (null == courseTypeBean) {
            ToastUtil.toastLongMessage("请选择课程类型");
            return;
        }
        if (null == courseBean) {
            ToastUtil.toastLongMessage("请选择课程");
            return;
        }
        if (null == courseChildBean) {
            ToastUtil.toastLongMessage("请选择视课程章节");
            return;
        }
        if ("".equals(getTextStr(et_videoTitle))) {
            ToastUtil.toastLongMessage("请输入标题标题");
            return;
        }

        if (!cb_agree.isChecked()) {
            ToastUtil.toastLongMessage("请阅读并同意直播协议");
            return;
        }

        livePresenter.getIsLive();


    }

    @Override
    public void onLiveCourseBeanSuccess(LiveCourseBean liveCourseBean) {

    }

    @Override
    public void onGetCourseBeanSuccess(List<HomeMenuBean> courseBeanList) {
        this.courseBeanList = courseBeanList;
    }

    @Override
    public void onGetCourseChildBeanSuccess(List<HomeMenuBean> courseChildBeanList) {
        this.courseChildBeanList = courseChildBeanList;

    }

    @Override
    public void onGetliveroomdetailSuccess(int liveId, String trtcPushAddr) {
        Intent intent = new Intent(mContext, CameraPushMainActivity.class);
        intent.putExtra("liveId", liveId);
        intent.putExtra("intent_url_push", trtcPushAddr);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBuyCourseSuccess() {

    }

    @Override
    public void onAddroomSuccess(int mRoomId) {
        livePresenter.getLiveroomdetail(mRoomId);
    }

    @Override
    public void onGetIsLiveSuccess(int isLive, int realStatus, int liveId, String trtcPushAddr) {
        //isLive 0-没有 1-有，未开始 2-有，开始
        //realStatus 实名状态（0-未认证 1-已认证）
        //liveId 直播间id
        if (isLive == 2) {
            PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                    "当前还有其他课程在直播，继续直播", "确认", "");
            promptDialog.show();
            promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                @Override
                public void insure() {
                    Intent intent = new Intent(mContext, CameraPushMainActivity.class);
                    intent.putExtra("mRoomId", liveId);
                    intent.putExtra("intent_url_push", trtcPushAddr);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void cancel() {

                }
            });
        } else if (isLive == 0) {
            livePresenter.addroom(courseTypeBean.getId(), courseBean.getId(), courseChildBean.getId(), getTextStr(et_videoTitle), "3");
        }
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    public boolean verifyStoragePermissions() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
