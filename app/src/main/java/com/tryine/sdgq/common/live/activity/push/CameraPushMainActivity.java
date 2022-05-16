package com.tryine.sdgq.common.live.activity.push;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupChangeInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupListener;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMUserInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.live2.V2TXLiveCode;
import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.V2TXLivePusher;
import com.tencent.live2.V2TXLivePusherObserver;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.live2.impl.V2TXLivePusherImpl;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModel;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModelManager;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDef;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TRTCLogger;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TXCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TXUserInfo;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.impl.IMAnchorInfo;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.impl.IMProtocol;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.impl.SignallingData;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.anchor.ExitConfirmDialogFragment;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.anchor.FinishDetailDialogFragment;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.msg.TCChatEntity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.msg.TCChatMsgListAdapter;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCConstants;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCUtils;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.widget.InputTextMsgDialog;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.mine.presenter.LiveInPresenter;
import com.tryine.sdgq.common.mine.view.LiveInView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.event.GiftNoticeEvent;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.view.dialog.LiveCommentDialog;
import com.tryine.sdgq.view.dialog.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.tencent.live2.V2TXLiveDef.V2TXLiveMixInputType.V2TXLiveMixInputTypePureVideo;
import static com.tencent.live2.V2TXLiveDef.V2TXLiveVideoResolutionMode.V2TXLiveVideoResolutionModeLandscape;
import static com.tencent.live2.V2TXLiveDef.V2TXLiveVideoResolutionMode.V2TXLiveVideoResolutionModePortrait;


public class CameraPushMainActivity extends FragmentActivity implements
        PusherVideoQualityFragment.OnVideoQualityChangeListener, PusherSettingFragment.OnSettingChangeListener, LiveCommentDialog.OnTextSendListener, LiveInView {

    private static final String TAG = CameraPushMainActivity.class.getSimpleName();
    private static final String LIVE_TOTAL_TITLE = "live_total_title";       // KEY，表示本场直播的时长
    protected static final String LIVE_TOTAL_TIME = "live_total_time";       // KEY，表示本场直播的时长
    protected static final String ANCHOR_HEART_COUNT = "anchor_heart_count";    // KEY，表示本场主播收到赞的数量
    protected static final String TOTAL_AUDIENCE_COUNT = "total_audience_count";  // KEY，表示本场观众的总人数
    private static final String PUSHER_SETTING_FRAGMENT = "push_setting_fragment";
    private static final String PUSHER_PLAY_QR_CODE_FRAGMENT = "push_play_qr_code_fragment";
    private static final String PUSHER_VIDEO_QUALITY_FRAGMENT = "push_video_quality_fragment";

    private TXPhoneStateListener mPhoneListener;

    private ImageView mImagesAnchorHead;      // 显示房间主播头像
    private ImageView mImageRecordBall;       // 表明正在录制的红点球
    private TextView mTextBroadcastTime;     // 显示已经开播的时间
    private TextView mTextRoomId;            // 显示当前房间号

    protected String mSelfAvatar;               // 个人头像地址
    protected String mSelfName;                 // 个人昵称
    protected String mSelfUserId;               // 个人用户id

    private ObjectAnimator mAnimatorRecordBall;    // 显示录制状态红点的闪烁动画

    int mRoomId = 1;

    //自带参数
    LiveInPresenter liveInPresenter;

    /**
     * 消息列表相关
     */
    private ListView mLvMessage;             // 消息控件
    LiveCommentDialog liveCommentDialog;   // 消息输入框
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter
    private ArrayList<TCChatEntity> mArrayListChatEntity;   // 消息内容

    private RelativeLayout mLayoutGiftShow;            // 礼物控件


    protected long mHeartCount = 0;   // 点赞数量
    protected long mTotalMemberCount = 0;   // 总进房观众数量
    protected long mCurrentMemberCount = 0;   // 当前观众数量

    /**
     * 定时的 Timer 去更新开播时间
     */
    private Timer mBroadcastTimer;        // 定时的 Timer
    private BroadcastTimerTask mBroadcastTimerTask;    // 定时任务
    protected long mSecond = 0;            // 开播的时间，单位为秒

    private TextView mTextNetBusyTips;              // 网络繁忙Tips
    //    private BeautyPanel mBeautyPanelView;              // 美颜模块pannel
    private LinearLayout mLinearBottomBar;              // 底部工具栏布局
    private AudioEffectPanel mAudioEffectPanel;             // 音效面板

    private PusherSettingFragment mPusherSettingFragment;      // 设置面板
    private PusherVideoQualityFragment mPusherVideoQualityFragment; // 画质面板

    private String mPusherURL = "";   // 推流地址
    private String mRTMPPlayURL = "";   // RTMP 拉流地址
    private String mFlvPlayURL = "";   // flv 拉流地址
    private String mHlsPlayURL = "";   // hls 拉流地址
    private String mRealtimePlayURL = "";   // 低延时拉流地址

    private int mLogClickCount = 0;

    private V2TXLivePusher mLivePusher; //主播推流
    private V2TXLivePlayer mLivePlayer; //主播连麦之后推流
    private TXCloudVideoView mPusherView;
    private TXCloudVideoView mPusherView1; //连麦view

    private FrameLayout ll_link;             // 连麦控件
    private ImageView iv_close;             // 关闭连麦控件


    private Bitmap mWaterMarkBitmap;

    private boolean mIsPushing = false;
    private boolean mIsResume = false;
    private boolean mIsWaterMarkEnable = false;
    private boolean mIsDebugInfo = false;
    private boolean mIsMuteAudio = false;
    private boolean mIsLandscape = false;
    private boolean mIsMirrorEnable = true;
    private boolean mIsFocusEnable = false;
    private boolean mIsEarMonitoringEnable = false;
    private boolean mFrontCamera = true;
    private boolean mIsEnableAdjustBitrate = true;

    // 已抛出的观众列表
    private Set<String> mAudienceList;

    private V2TXLiveDef.V2TXLiveVideoResolution mVideoResolution = V2TXLiveDef.V2TXLiveVideoResolution.V2TXLiveVideoResolution960x540;
    private V2TXLiveDef.V2TXLiveAudioQuality mAudioQuality = V2TXLiveDef.V2TXLiveAudioQuality.V2TXLiveAudioQualityDefault;
    private int mQualityType;


    private final Map<String, TXUserInfo> mAudienceInfoMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.transparent).
                statusBarDarkFont(false).
                navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();
        setContentView(R.layout.livepusher_activity_live_pusher_main);
        EventBus.getDefault().register(this);
        liveInPresenter = new LiveInPresenter(this);
        liveInPresenter.attachView(this);
        initData();                // 初始化数据
        initFragment();            // 初始化Fragment
        initPusher();              // 初始化 SDK 推流器
        initMainView();            // 初始化一些核心的 View
        initView();

        // 进入页面，自动开始推流，并且弹出推流对应的拉流地址
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                // 初始化完成之后自动播放
                startPush();
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                ToastUtils.showShort(R.string.livepusher_app_camera_mic);
                finish();
            }
        }).request();

    }

    private void initView() {
        mImageRecordBall = (ImageView) findViewById(R.id.iv_anchor_record_ball);
        mImagesAnchorHead = (ImageView) findViewById(R.id.iv_anchor_head);
        mLayoutGiftShow = findViewById(R.id.rl_gift_show);
        showHeadIcon(mImagesAnchorHead, mSelfAvatar);
        ll_link = (FrameLayout) findViewById(R.id.ll_link);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        mTextBroadcastTime = (TextView) findViewById(R.id.tv_anchor_broadcasting_time);
        mTextBroadcastTime.setText(String.format(Locale.US, "%s", "00:00:00"));
        mTextRoomId = (TextView) findViewById(R.id.tv_room_id);
        mTextRoomId.setText(String.format(getString(R.string.trtcliveroom_room_id), String.valueOf(mRoomId)));

        mLvMessage = (ListView) findViewById(R.id.lv_im_msg);

        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mLvMessage, mArrayListChatEntity);
        mLvMessage.setAdapter(mChatMsgListAdapter);

        liveCommentDialog = new LiveCommentDialog(this, R.style.dialog_center);
        liveCommentDialog.setmOnTextSendListener(this);

        mLayoutGiftShow = (RelativeLayout) findViewById(R.id.rl_gift_show);
        setGroupId(mRoomId + "");
    }

    private void initData() {
        Intent intent = getIntent();
        mPusherURL = intent.getStringExtra("intent_url_push");
        mRoomId = intent.getIntExtra("liveId", 0);

        UserModel userModel = UserModelManager.getInstance().getUserModel();
        mSelfUserId = userModel.userId;
        mSelfName = userModel.userName;
        mSelfAvatar = userModel.userAvatar;

        mArrayListChatEntity = new ArrayList<>();
        mAudienceList = new HashSet<>();
        createGroup(mRoomId);

    }


    private void createGroup(int roomId) {
        final V2TIMManager imManager = V2TIMManager.getInstance();
        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {
            @Override
            public void onRecvC2CTextMessage(String msgID, V2TIMUserInfo sender, String text) {
                super.onRecvC2CTextMessage(msgID, sender, text);
            }

            @Override
            public void onRecvC2CCustomMessage(String msgID, V2TIMUserInfo sender, byte[] customData) {
                super.onRecvC2CCustomMessage(msgID, sender, customData);
            }

            @Override
            public void onRecvGroupTextMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, String text) {
                super.onRecvGroupTextMessage(msgID, groupID, sender, text);
                handleTextMsg(sender, text);
            }

            @Override
            public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
                super.onRecvGroupCustomMessage(msgID, groupID, sender, customData);
            }
        });
        imManager.createGroup(V2TIMManager.GROUP_TYPE_AVCHATROOM, roomId + "", roomId + "", new V2TIMValueCallback<String>() {
            @Override
            public void onError(int code, String s) {
                String msg = s;
                if (code == 10036) {
                    msg = getString(R.string.trtcliveroom_create_room_limit);
                }
                if (code == 10037) {
                    msg = getString(R.string.trtcliveroom_create_or_join_group_limit);
                }
                if (code == 10038) {
                    msg = getString(R.string.trtcliveroom_group_member_limit);
                }
                if (code == 10025) {
                    // 10025 表明群主是自己，那么认为创建房间成功
                    onSuccess("success");
                    V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
                    v2TIMGroupInfo.setGroupID(mRoomId + "");
                    v2TIMGroupInfo.setIntroduction("");
                    V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, null);
                } else {
                    TRTCLogger.e(TAG, "create room fail, code:" + code + " msg:" + msg);
//                    if (callback != null) {
//                        callback.onCallback(code, msg);
//                    }
                }
            }

            @Override
            public void onSuccess(String s) {

            }
        });

        V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
        v2TIMGroupInfo.setNotification("");
        V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, null);

        V2TIMManager.getInstance().setGroupListener(new LiveRoomGroupListener());
        V2TIMManager.getSignalingManager().addSignalingListener(new LiveV2TIMSignalingListener());
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

    }

    private class LiveRoomGroupListener extends V2TIMGroupListener {
        @Override
        public void onMemberEnter(String groupID, List<V2TIMGroupMemberInfo> memberList) {
            Log.d(TAG, "onMemberEnter");

            for (V2TIMGroupMemberInfo timUserProfile : memberList) {
                TXUserInfo userInfo = new TXUserInfo();
                userInfo.userName = timUserProfile.getNickName();
                Log.d(TAG, "onMemberEnter userName: " + userInfo.userName);
                userInfo.userId = timUserProfile.getUserID();
                userInfo.avatarURL = timUserProfile.getFaceUrl();
//                mAudienceInfoMap.put(userInfo.userId, userInfo);
//                if (TextUtils.isEmpty(userInfo.userId) || userInfo.userId.equals(mMySelfIMInfo.userId)) {
//                    return;
//                }
//                if (mDelegate != null) {
//                    mDelegate.onRoomAudienceEnter(userInfo);
//                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAudienceList.contains(userInfo.userId)) {
                            return;
                        }
                        mAudienceList.add(userInfo.userId);
                        TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                        info.userId = userInfo.userId;
                        info.userAvatar = userInfo.avatarURL;
                        info.userName = userInfo.userName;
                        handleMemberJoinMsg(info);
                    }
                });


            }
        }

        @Override
        public void onMemberLeave(String groupID, V2TIMGroupMemberInfo member) {
            TXUserInfo userInfo = new TXUserInfo();
            userInfo.userName = member.getNickName();
            userInfo.userId = member.getUserID();
            userInfo.avatarURL = member.getFaceUrl();
            mAudienceInfoMap.remove(userInfo.userId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAudienceList.remove(userInfo.userId);
                    TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                    info.userId = userInfo.userId;
                    info.userAvatar = userInfo.avatarURL;
                    info.userName = userInfo.userName;
                    handleMemberQuitMsg(info);
                }
            });
        }

        @Override
        public void onGroupDismissed(String groupID, V2TIMGroupMemberInfo opUser) {
            TRTCLogger.i(TAG, "recv room destroy msg");
            // 如果发现房间已经解散，那么内部退一次房间
//            exitRoom(new TXCallback() {
//                @Override
//                public void onCallback(int code, String msg) {
//                    TRTCLogger.i(TAG, "recv room destroy msg, exit room inner, code:" + code + " msg:" + msg);
//                    // 无论结果是否成功，都清空状态，并且回调出去
//                    cleanStatus();
//                    ITXRoomServiceDelegate delegate = mDelegate;
//                    if (delegate != null) {
//                        String roomId = mRoomId;
//                        delegate.onRoomDestroy(roomId);
//                    }
//                }
//            });
        }

        @Override
        public void onGroupInfoChanged(String groupID, List<V2TIMGroupChangeInfo> changeInfos) {
            super.onGroupInfoChanged(groupID, changeInfos);
        }
    }


    /**
     * 开启红点与计时动画
     */
    private void startRecordAnimation() {
        mAnimatorRecordBall = ObjectAnimator.ofFloat(mImageRecordBall, "alpha", 1f, 0f, 1f);
        mAnimatorRecordBall.setDuration(1000);
        mAnimatorRecordBall.setRepeatCount(-1);
        mAnimatorRecordBall.start();
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecordAnimation() {
        if (null != mAnimatorRecordBall)
            mAnimatorRecordBall.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPush();
        mPusherView.onDestroy();
        unInitPhoneListener();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);

        if (mAudioEffectPanel != null) {
            mAudioEffectPanel.unInit();
            mAudioEffectPanel = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mAudioEffectPanel && mAudioEffectPanel.getVisibility() != View.GONE && ev.getRawY() < mAudioEffectPanel.getTop()) {
            mAudioEffectPanel.setVisibility(View.GONE);
            mAudioEffectPanel.hideAudioPanel();
            mLinearBottomBar.setVisibility(View.VISIBLE);
        }
//        if (null != mBeautyPanelView && mBeautyPanelView.getVisibility() != View.GONE && ev.getRawY() < mBeautyPanelView.getTop()) {
//            mBeautyPanelView.setVisibility(View.GONE);
//            mLinearBottomBar.setVisibility(View.VISIBLE);
//        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 加载主播头像
     *
     * @param view   view
     * @param avatar 头像链接
     */
    private void showHeadIcon(ImageView view, String avatar) {
        TCUtils.showPicWithUrl(this, view, avatar, R.mipmap.trtcliveroom_bg_cover);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_close) {
            showExitInfoDialog(getString(R.string.trtcliveroom_warning_anchor_exit_room), false);
        } else if (id == R.id.btn_message_input) {
            showInputMsgDialog();
        } else if (id == R.id.livepusher_btn_switch_camera) {
            // 表明当前是前摄像头
            if (view.getTag() == null || (Boolean) view.getTag()) {
                view.setTag(false);
                view.setBackgroundResource(R.mipmap.trtcliveroom_icon_switch_camera_on);
            } else {
                view.setTag(true);
                view.setBackgroundResource(R.mipmap.trtcliveroom_icon_switch_camera_on);
            }
            switchCamera();
            // } else if (id == R.id.livepusher_btn_beauty) {
//            if (mLogInfoWindow.isShowing()) {
//                mLogInfoWindow.dismiss();
//            }
//            if (mBeautyPanelView.isShown()) {
//                mBeautyPanelView.setVisibility(View.GONE);
//                mLinearBottomBar.setVisibility(View.VISIBLE);
//            } else {
//                mBeautyPanelView.setVisibility(View.VISIBLE);
//                mLinearBottomBar.setVisibility(View.GONE);
//            }
        } else if (id == R.id.livepusher_btn_bgm) {

            if (mAudioEffectPanel.isShown()) {
                mAudioEffectPanel.setVisibility(View.GONE);
                mAudioEffectPanel.hideAudioPanel();
                mLinearBottomBar.setVisibility(View.VISIBLE);
            } else {
                mAudioEffectPanel.setVisibility(View.VISIBLE);
                mAudioEffectPanel.showAudioPanel();
                mLinearBottomBar.setVisibility(View.GONE);
            }
        } else if (id == R.id.livepusher_btn_video_quality) {
            mPusherVideoQualityFragment.toggle(getSupportFragmentManager(), PUSHER_VIDEO_QUALITY_FRAGMENT);
        } else if (id == R.id.livepusher_btn_setting) {
            mPusherSettingFragment.toggle(getSupportFragmentManager(), PUSHER_SETTING_FRAGMENT);
        } else if (id == R.id.iv_close) {
            V2TIMManager.getInstance().sendGroupTextMessage(IMProtocol.SignallingDefine.closeJoinAnchor, mRoomId + "", V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    TRTCLogger.e(TAG, "message send fail, code: " + i + " msg:" + s);
                }

                @Override
                public void onSuccess(V2TIMMessage v2TIMMessage) {
                    ll_link.setVisibility(View.GONE);
                    if (mLivePlayer != null) {
                        mLivePlayer.stopPlay();
                    }

                    //关闭连麦后
                    V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
                    v2TIMGroupInfo.setGroupID(mRoomId + "");
                    v2TIMGroupInfo.setNotification("");
                    V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, null);

                    int result = mLivePusher.setMixTranscodingConfig(null);
                }
            });

        }
    }

    @Override
    public void onMuteChange(boolean enable) {
        setMute(enable);
    }

    @Override
    public void onHomeOrientationChange(boolean isLandscape) {
        mIsLandscape = isLandscape;
        // 横竖屏推流
        // 横竖屏推流
        V2TXLiveDef.V2TXLiveVideoEncoderParam param = new V2TXLiveDef.V2TXLiveVideoEncoderParam(mVideoResolution);
        param.videoResolutionMode =
                isLandscape ? V2TXLiveVideoResolutionModeLandscape : V2TXLiveVideoResolutionModePortrait;
        mLivePusher.setVideoQuality(param);
    }

    @Override
    public void onMirrorChange(boolean enable) {
        setMirror(enable);
    }

    @Override
    public void onFlashLightChange(boolean enable) {
        turnOnFlashLight(enable);
    }

    @Override
    public void onWatermarkChange(boolean enable) {
        setWatermark(enable);
    }

    @Override
    public void onTouchFocusChange(boolean enable) {
        setTouchFocus(enable);
        if (mIsPushing) {
            showToast(R.string.livepusher_pushing_start_stop_retry_push_by_focus);
        }
    }

    @Override
    public void onClickSnapshot() {
        snapshot();
    }

    @Override
    public void onAdjustBitrateChange(boolean enable) {
        setAdjustBitrate(enable, mPusherVideoQualityFragment.getQualityType());
    }

    @Override
    public void onQualityChange(int type) {
        setQuality(mPusherSettingFragment.isAdjustBitrate(), type);
    }

    @Override
    public void onEnableAudioEarMonitoringChange(boolean enable) {
        enableAudioEarMonitoring(enable);
    }

    @Override
    public void onAudioQualityChange(V2TXLiveDef.V2TXLiveAudioQuality audioQuality) {
        setAudioQuality(audioQuality);
    }


    /**
     * 初始化 SDK 推流器
     */
    private void initPusher() {
        mPusherView = findViewById(R.id.livepusher_tx_cloud_view);
        mPusherView1 = findViewById(R.id.tx_cloud_view_audience);

        mLivePusher = new V2TXLivePusherImpl(this, V2TXLiveDef.V2TXLiveMode.TXLiveMode_RTC);
        // 设置默认美颜参数， 美颜样式为光滑，美颜等级 5，美白等级 3，红润等级 2
        mLivePusher.getBeautyManager().setBeautyStyle(TXLiveConstants.BEAUTY_STYLE_SMOOTH);
        mLivePusher.getBeautyManager().setBeautyLevel(5);
        mLivePusher.getBeautyManager().setWhitenessLevel(3);
        mLivePusher.getBeautyManager().setRuddyLevel(2);


        mWaterMarkBitmap = decodeResource(getResources(), R.mipmap.iv_zblk);
        initListener();

        setMirror(mPusherSettingFragment.isMirror());
        setTouchFocus(mPusherSettingFragment.isTouchFocus());
        enableAudioEarMonitoring(mPusherSettingFragment.enableAudioEarMonitoring());
        setQuality(mPusherSettingFragment.isAdjustBitrate(), mPusherVideoQualityFragment.getQualityType());
        setAudioQuality(mPusherSettingFragment.getAudioQuality());
        turnOnFlashLight(mPusherSettingFragment.isFlashEnable());
        mIsLandscape = mPusherSettingFragment.isLandscape();
    }

    /**
     * 初始化两个配置的 Fragment
     */
    private void initFragment() {
        if (mPusherSettingFragment == null) {
            mPusherSettingFragment = new PusherSettingFragment();
            mPusherSettingFragment.loadConfig(this);
            mPusherSettingFragment.setOnSettingChangeListener(this);
        }

        if (mPusherVideoQualityFragment == null) {
            mPusherVideoQualityFragment = new PusherVideoQualityFragment();
            mPusherVideoQualityFragment.loadConfig(this);
            mPusherVideoQualityFragment.setOnVideoQualityChangeListener(this);
        }

    }

    /**
     * 初始化 美颜、log、二维码 等 view
     */
    private void initMainView() {
//        mBtnStartPush = findViewById(R.id.livepusher_btn_start);
//        mBeautyPanelView = findViewById(R.id.livepusher_bp_beauty_pannel);
        mTextNetBusyTips = findViewById(R.id.livepusher_tv_net_error_warning);
        mLinearBottomBar = findViewById(R.id.livepusher_ll_bottom_bar);

        mAudioEffectPanel = findViewById(R.id.livepusher_audio_panel);
        mAudioEffectPanel.setAudioEffectManager(mLivePusher.getAudioEffectManager());
        mAudioEffectPanel.setBackgroundColor(0xff13233F);
        mAudioEffectPanel.setOnAudioEffectPanelHideListener(new AudioEffectPanel.OnAudioEffectPanelHideListener() {
            @Override
            public void onClosePanel() {
                mAudioEffectPanel.setVisibility(View.GONE);
                mLinearBottomBar.setVisibility(View.VISIBLE);
            }
        });

//        mBeautyPanelView.setBeautyManager(mLivePusher.getBeautyManager());
//        mBeautyPanelView.setOnBeautyListener(new BeautyPanel.OnBeautyListener() {
//            @Override
//            public boolean onClose() {
//                mBeautyPanelView.setVisibility(View.GONE);
//                mLinearBottomBar.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });
    }

    /**
     * 显示网络繁忙的提示
     */
    private void showNetBusyTips() {
        if (mTextNetBusyTips.isShown()) {
            return;
        }
        mTextNetBusyTips.setVisibility(View.VISIBLE);
        mTextNetBusyTips.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextNetBusyTips.setVisibility(View.GONE);
            }
        }, 5000);
    }

    private Uri getUri(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, "com.tencent.liteav.demo", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * 判断系统 "自动旋转" 设置功能是否打开
     *
     * @return false---Activity可根据重力感应自动旋转
     */
    private boolean isActivityCanRotation() {
        int flag = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        return flag != 0;
    }

    private void showToast(final @StringRes int resId) {
        showToast(getString(resId));
    }

    private void showToast(final String text) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CameraPushMainActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onPushStart(int code) {
        TXLog.d(TAG, "onPusherStart: code -> " + code);
        switch (code) {
            case 0:
                startRecordAnimation();
                startTimer();
                break;
            case -1:
                showToast(R.string.livepusher_url_illegal);
                // 输出状态log
                Bundle params = new Bundle();
                params.putString(TXLiveConstants.EVT_DESCRIPTION, getString(R.string.livepusher_check_url));
                break;
            case -5:
                String errInfo = getString(R.string.livepusher_license_check_fail);
                int start = (errInfo + getString(R.string.livepusher_license_click_info)).length();
                int end = (errInfo + getString(R.string.livepusher_license_click_use_info)).length();
                SpannableStringBuilder spannableStrBuidler = new SpannableStringBuilder(errInfo + getString(R.string.livepusher_license_click_use_info));
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("https://cloud.tencent.com/document/product/454/34750");
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                };
                spannableStrBuidler.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStrBuidler.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                TextView tv = new TextView(this);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(spannableStrBuidler);
                tv.setPadding(20, 0, 20, 0);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle(getString(R.string.livepusher_push_fail)).setView(tv).setPositiveButton(getString(R.string.livepusher_comfirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopPush();
                    }
                });
                dialogBuilder.show();
            default:
                break;
        }
        if (code != -1) {
            // 输出状态log
            Bundle bundle = new Bundle();
            bundle.putString(TXLiveConstants.EVT_DESCRIPTION, getString(R.string.livepusher_check_url));
        }
    }


    private void startPush() {
        int resultCode = 0;
        String tRTMPURL = "";
        if (!TextUtils.isEmpty(mPusherURL)) {
            String url[] = mPusherURL.split("###");
            if (url.length > 0) {
                tRTMPURL = url[0];
            }
        }

        if (TextUtils.isEmpty(tRTMPURL) || (!tRTMPURL.trim().toLowerCase().startsWith("trtc://"))) {
            resultCode = -1;
        } else {
            // 显示本地预览的View
            mPusherView.setVisibility(View.VISIBLE);
            // 添加播放回调
            mLivePusher.setObserver(new MyPusherObserver());
            // 设置推流分辨率
            V2TXLiveDef.V2TXLiveVideoEncoderParam param = new V2TXLiveDef.V2TXLiveVideoEncoderParam(mVideoResolution);
            param.videoResolutionMode =
                    mIsLandscape ? V2TXLiveVideoResolutionModeLandscape : V2TXLiveVideoResolutionModePortrait;
            mLivePusher.setVideoQuality(param);
            // 是否开启观众端镜像观看
            mLivePusher.setEncoderMirror(mIsMirrorEnable);
            // 是否打开调试信息
            mPusherView.showLog(mIsDebugInfo);


            // 是否打开曝光对焦
            mLivePusher.getDeviceManager().enableCameraAutoFocus(mIsFocusEnable);

            mLivePusher.getAudioEffectManager().enableVoiceEarMonitor(mIsEarMonitoringEnable);
            // 设置场景
            setPushScene(4, mIsEnableAdjustBitrate);

            // 设置声道，设置音频采样率，必须在 TXLivePusher.setVideoQuality 之后，TXLivePusher.startPusher之前设置才能生效
            setAudioQuality(mAudioQuality);

            // 设置本地预览View
            mLivePusher.setRenderView(mPusherView);
            mLivePusher.startCamera(mFrontCamera);
            mLivePusher.startMicrophone();
            if (!mFrontCamera) mLivePusher.getDeviceManager().switchCamera(mFrontCamera);
            // 发起推流
            resultCode = mLivePusher.startPush(tRTMPURL.trim());

            mIsPushing = true;
        }
        TXLog.i(TAG, "start: mIsResume -> " + mIsResume);
        onPushStart(resultCode);
    }

    private void stopPush() {
        if (!mIsPushing) {
            return;
        }
        // 停止本地预览
        mLivePusher.stopCamera();
        // 移除监听
        mLivePusher.setObserver(null);
        // 停止推流
        mLivePusher.stopPush();
        // 隐藏本地预览的View
        mPusherView.setVisibility(View.GONE);
        mIsPushing = false;
        mAudioEffectPanel.reset();
        liveInPresenter.closeRoom(mRoomId);
    }

    @Override
    public void onTextSend(String msg, boolean tanmuOpen) {
        if (msg.length() == 0) {
            return;
        }
        byte[] byte_num = msg.getBytes(StandardCharsets.UTF_8);
        if (byte_num.length > 160) {
            Toast.makeText(this, R.string.trtcliveroom_tips_input_content, Toast.LENGTH_SHORT).show();
            return;
        }

        //消息回显
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(getString(R.string.trtcliveroom_me));
        entity.setContent(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        V2TIMManager.getInstance().sendGroupTextMessage(msg, mRoomId + "", V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                TRTCLogger.e(TAG, "message send fail, code: " + i + " msg:" + s);

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {

            }
        });

    }

    @Override
    public void dismiss() {

    }

    /**
     * 接受群消息
     * @param entity
     */
    private void notifyMsg(final TCChatEntity entity) {
        if (entity.getContent().contains("liveBannedGroupRoom")) { //liveBannedGroupRoom禁播标识
            stopPush();
            if (null != mLivePusher) {
                mLivePusher.stopPush();
            }
            showPublishFinishDetailsDialog(1);
        } else if (entity.getContent().contains(IMProtocol.SignallingDefine.closeJoinAnchor)) { //关闭连麦
            ll_link.setVisibility(View.GONE);
            if (mLivePlayer != null) {
                mLivePlayer.stopPlay();
            }

            V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
            v2TIMGroupInfo.setGroupID(mRoomId + "");
            v2TIMGroupInfo.setNotification("");
            V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, null);
            int result = mLivePusher.setMixTranscodingConfig(null);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mArrayListChatEntity.size() > 1000) {
                        while (mArrayListChatEntity.size() > 900) {
                            mArrayListChatEntity.remove(0);
                        }
                    }
                    mArrayListChatEntity.add(entity);
                    mChatMsgListAdapter.notifyDataSetChanged();
                }
            });
        }

    }


    private class MyPusherObserver extends V2TXLivePusherObserver {
        @Override
        public void onWarning(int code, String msg, Bundle extraInfo) {
            Log.w(TAG, "[Pusher] onWarning errorCode: " + code + ", msg " + msg);
            if (code == V2TXLiveCode.V2TXLIVE_WARNING_NETWORK_BUSY) {
                showNetBusyTips();
            }
        }

        @Override
        public void onError(int code, String msg, Bundle extraInfo) {
            Log.e(TAG, "[Pusher] onError: " + msg + ", extraInfo " + extraInfo);
        }

        @Override
        public void onCaptureFirstAudioFrame() {
            Log.i(TAG, "[Pusher] onCaptureFirstAudioFrame");
        }

        @Override
        public void onCaptureFirstVideoFrame() {
            Log.i(TAG, "[Pusher] onCaptureFirstVideoFrame");
        }

        @Override
        public void onMicrophoneVolumeUpdate(int volume) {
        }

        @Override
        public void onPushStatusUpdate(V2TXLiveDef.V2TXLivePushStatus status, String msg, Bundle bundle) {
        }

        @Override
        public void onSnapshotComplete(Bitmap bitmap) {
            if (mLivePusher.isPushing() == 1) {
                if (bitmap != null) {
                    saveSnapshotBitmap(bitmap);
                } else {
                    showToast(R.string.livepusher_screenshot_fail);
                }
            } else {
                showToast(R.string.livepusher_screenshot_fail_push);
            }
        }

        @Override
        public void onStatisticsUpdate(V2TXLiveDef.V2TXLivePusherStatistics statistics) {
            Bundle netStatus = new Bundle();
            netStatus.putInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH, statistics.width);
            netStatus.putInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT, statistics.height);
            int appCpu = statistics.appCpu / 10;
            int totalCpu = statistics.systemCpu / 10;
            String strCpu = appCpu + "/" + totalCpu + "%";
            netStatus.putCharSequence(TXLiveConstants.NET_STATUS_CPU_USAGE, strCpu);
            netStatus.putInt(TXLiveConstants.NET_STATUS_NET_SPEED, statistics.videoBitrate + statistics.audioBitrate);
            netStatus.putInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE, statistics.audioBitrate);
            netStatus.putInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE, statistics.videoBitrate);
            netStatus.putInt(TXLiveConstants.NET_STATUS_VIDEO_FPS, statistics.fps);
            netStatus.putInt(TXLiveConstants.NET_STATUS_VIDEO_GOP, 5);
            Log.d(TAG, "Current status, CPU:" + netStatus.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                    ", RES:" + netStatus.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + netStatus.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                    ", SPD:" + netStatus.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                    ", FPS:" + netStatus.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                    ", ARA:" + netStatus.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                    ", VRA:" + netStatus.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
        }
    }

    private void togglePush() {
        if (mIsPushing) {
            stopPush();
        } else {
            startPush();
        }
    }

    private void resume() {
        TXLog.i(TAG, "resume: mIsResume -> " + mIsResume);
        if (mIsResume) {
            return;
        }
        if (mPusherView != null) {
            mPusherView.onResume();
        }
        mLivePusher.stopVirtualCamera();
        if (mIsMuteAudio) {// audio这里要结合外部设定的 MuteAudio 和 PausePusher 来决定是否静音上行。
            mLivePusher.pauseAudio();
        } else {
            mLivePusher.resumeAudio();
        }
        mLivePusher.resumeVideo();
        mIsResume = true;
        mAudioEffectPanel.resumeBGM();
    }

    private void pause() {
        TXLog.i(TAG, "pause: mIsResume -> " + mIsResume);
        if (mPusherView != null) {
            mPusherView.onPause();
        }
        mLivePusher.startVirtualCamera(decodeResource(getResources(), R.mipmap.iv_zblk));
        mLivePusher.pauseAudio();
        mIsResume = false;
        mAudioEffectPanel.pauseBGM();
    }

    private void setMute(boolean enable) {
        mIsMuteAudio = enable;
        if (enable) {
            mLivePusher.pauseAudio();
        } else {
            mLivePusher.resumeAudio();
        }
    }

    private void switchCamera() {
        mFrontCamera = !mFrontCamera;
        mLivePusher.getDeviceManager().switchCamera(mFrontCamera);
    }

    private void setMirror(boolean enable) {
        mIsMirrorEnable = enable;
        mLivePusher.setEncoderMirror(enable);
    }

    private void turnOnFlashLight(boolean enable) {
        mLivePusher.getDeviceManager().enableCameraTorch(enable);
    }

    private void showLog(boolean enable) {
        mIsDebugInfo = enable;
        mPusherView.showLog(enable);
    }

    private void setWatermark(boolean enable) {
        mIsWaterMarkEnable = enable;
        if (enable) {
            mLivePusher.setWatermark(mWaterMarkBitmap, 0.02f, 0.05f, 0.2f);
        } else {
            mLivePusher.setWatermark(null, 0, 0, 0);
        }
    }

    private void setTouchFocus(boolean enable) {
        mIsFocusEnable = !enable;
        mLivePusher.getDeviceManager().enableCameraAutoFocus(mIsFocusEnable);
        if (mLivePusher.isPushing() == 1) {
            stopPush();
            startPush();
        }
    }

    private void snapshot() {
        mLivePusher.snapshot();
    }

    private void setAdjustBitrate(boolean enable, int qualityType) {
        mIsEnableAdjustBitrate = enable;
        setPushScene(qualityType, enable);
    }

    private void setQuality(boolean enable, int type) {
        setPushScene(type, enable);
    }

    private void enableAudioEarMonitoring(boolean enable) {
        mIsEarMonitoringEnable = enable;
        if (mLivePusher != null) {
            mLivePusher.getAudioEffectManager().enableVoiceEarMonitor(enable);
        }
    }

    private void setAudioQuality(V2TXLiveDef.V2TXLiveAudioQuality audioQuality) {
        mAudioQuality = audioQuality;
        if (mLivePusher != null) {
            mLivePusher.setAudioQuality(audioQuality);
        }
    }

    /**
     * 设置推流场景
     * <p>
     * SDK 内部将根据具体场景，进行推流 分辨率、码率、FPS、是否启动硬件加速、是否启动回声消除 等进行配置
     * <p>
     * 适用于一般客户，方便快速进行配置
     * <p>
     */
    private void setPushScene(int type, boolean enableAdjustBitrate) {
        TXLog.i(TAG, "setPushScene: type = " + type + " enableAdjustBitrate = " + enableAdjustBitrate);
        mQualityType = type;
        mIsEnableAdjustBitrate = enableAdjustBitrate;
        switch (type) {
            case TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION:     // 360P
                if (mLivePusher != null) {
                    V2TXLiveDef.V2TXLiveVideoEncoderParam param =
                            new V2TXLiveDef.V2TXLiveVideoEncoderParam(mVideoResolution);
                    param.videoResolutionMode =
                            mIsLandscape ? V2TXLiveVideoResolutionModeLandscape : V2TXLiveVideoResolutionModePortrait;
                    mLivePusher.setVideoQuality(param);
                    mVideoResolution = V2TXLiveDef.V2TXLiveVideoResolution.V2TXLiveVideoResolution640x360;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION:         // 540P
                if (mLivePusher != null) {
                    V2TXLiveDef.V2TXLiveVideoEncoderParam param =
                            new V2TXLiveDef.V2TXLiveVideoEncoderParam(mVideoResolution);
                    param.videoResolutionMode =
                            mIsLandscape ? V2TXLiveVideoResolutionModeLandscape : V2TXLiveVideoResolutionModePortrait;
                    mLivePusher.setVideoQuality(param);
                    mVideoResolution = V2TXLiveDef.V2TXLiveVideoResolution.V2TXLiveVideoResolution960x540;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION:        // 720p
                if (mLivePusher != null) {
                    V2TXLiveDef.V2TXLiveVideoEncoderParam param =
                            new V2TXLiveDef.V2TXLiveVideoEncoderParam(mVideoResolution);
                    param.videoResolutionMode =
                            mIsLandscape ? V2TXLiveVideoResolutionModeLandscape : V2TXLiveVideoResolutionModePortrait;
                    mLivePusher.setVideoQuality(param);
                    mVideoResolution = V2TXLiveDef.V2TXLiveVideoResolution.V2TXLiveVideoResolution1280x720;
                }
                break;
            case TXLiveConstants.VIDEO_QUALITY_ULTRA_DEFINITION:        // 1080p
                if (mLivePusher != null) {
                    V2TXLiveDef.V2TXLiveVideoEncoderParam param =
                            new V2TXLiveDef.V2TXLiveVideoEncoderParam(mVideoResolution);
                    param.videoResolutionMode =
                            mIsLandscape ? V2TXLiveVideoResolutionModeLandscape : V2TXLiveVideoResolutionModePortrait;
                    mLivePusher.setVideoQuality(param);
                    mVideoResolution = V2TXLiveDef.V2TXLiveVideoResolution.V2TXLiveVideoResolution1920x1080;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化电话监听、系统是否打开旋转监听
     */
    private void initListener() {
        mPhoneListener = new TXPhoneStateListener();
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 销毁
     */
    private void unInitPhoneListener() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * 获取资源图片
     *
     * @param resources
     * @param id
     * @return
     */
    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    /**
     * 保存并分享图片
     *
     * @param bmp
     */
    private void saveSnapshotBitmap(final Bitmap bmp) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String bitmapFileName = UUID.randomUUID().toString();//通过UUID生成字符串文件名
                FileOutputStream out = null;
                File sdcardDir = getExternalFilesDir(null);
                if (sdcardDir == null) {
                    TXLog.e(TAG, "sdcardDir is null");
                    return;
                }
                final String path = sdcardDir + File.separator + bitmapFileName + ".png";
                final File file = new File(path);
                try {
                    file.getParentFile().mkdirs();
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (file.exists() && file.length() > 0) {
                    showToast(R.string.livepusher_screenshot_success);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);//设置分享行为
                    Uri uri = getUri(file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, getString(R.string.livepusher_share_pic)));
                } else {
                    showToast(R.string.livepusher_screenshot_fail);
                }
            }
        });
    }

    /**
     * 电话监听
     */
    private class TXPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLog.i(TAG, "onCallStateChanged: state -> " + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:   //电话等待接听
                case TelephonyManager.CALL_STATE_OFFHOOK:   //电话接听
                    pause();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:      //电话挂机
                    resume();
                    break;
            }
        }
    }


    /**
     * 记时器
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            //Log.i(TAG, "timeTask ");
            ++mSecond;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onBroadcasterTimeUpdate(mSecond);
                }
            });
        }
    }


    private void onBroadcasterTimeUpdate(long second) {
        mTextBroadcastTime.setText(TCUtils.formattedTime(second));
    }

    private void startTimer() {
        //直播时间
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    private void stopTimer() {
        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }


    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showExitInfoDialog(String msg, Boolean isError) {
        final ExitConfirmDialogFragment dialogFragment = new ExitConfirmDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);

        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }

        if (isError) {
            stopPush();
            dialogFragment.setPositiveClickListener(new ExitConfirmDialogFragment.PositiveClickListener() {
                @Override
                public void onClick() {
                    dialogFragment.dismiss();
                    showPublishFinishDetailsDialog(0);
                }
            });
            dialogFragment.show(getFragmentManager(), "ExitConfirmDialogFragment");
            return;
        }

        dialogFragment.setPositiveClickListener(new ExitConfirmDialogFragment.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
                stopPush();
                if (null != mLivePusher) {
                    mLivePusher.stopPush();
                }
                showPublishFinishDetailsDialog(0);
            }
        });

        dialogFragment.setNegativeClickListener(new ExitConfirmDialogFragment.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getFragmentManager(), "ExitConfirmDialogFragment");
    }


    /**
     * 显示直播结果的弹窗
     * <p>
     * 如：观看数量、点赞数量、直播时长数
     */
    protected void showPublishFinishDetailsDialog(int type) {
        //确认则显示观看detail
        FinishDetailDialogFragment dialogFragment = new FinishDetailDialogFragment();
        Bundle args = new Bundle();
        if (type == 1) {
            args.putString(LIVE_TOTAL_TITLE, "您已被禁播!");
        } else {
            args.putString(LIVE_TOTAL_TITLE, "已下课，辛苦啦!");
        }
        args.putString(LIVE_TOTAL_TIME, TCUtils.formattedTime(mSecond));
        args.putString(ANCHOR_HEART_COUNT, String.format(Locale.CHINA, "%d", mHeartCount));
        args.putString(TOTAL_AUDIENCE_COUNT, String.format(Locale.CHINA, "%d", mTotalMemberCount));
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
        } else {
            dialogFragment.show(getFragmentManager(), "");
        }


        V2TIMManager.getInstance().dismissGroup(mRoomId+"", new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                TRTCLogger.e(TAG, "destroy room fail, code:" + i + " msg:" + s);
            }

            @Override
            public void onSuccess() {
                TRTCLogger.d(TAG, "destroyRoom remove GroupListener roomId: " + mRoomId );

            }
        });
    }


    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * //
     * //                      处理接收到的各种信息
     * //
     * /////////////////////////////////////////////////////////////////////////////////
     */
    protected void handleTextMsg(V2TIMGroupMemberInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.getNickName());
        entity.setContent(text);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);
    }

    /**
     * 处理观众加入信息
     *
     * @param userInfo
     */
    protected void handleMemberJoinMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mTotalMemberCount++;
        mCurrentMemberCount++;
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(getString(R.string.trtcliveroom_notification));
        if (TextUtils.isEmpty(userInfo.userName))
            entity.setContent(getString(R.string.trtcliveroom_user_join_live, userInfo.userId));
        else
            entity.setContent(getString(R.string.trtcliveroom_user_join_live, userInfo.userName));
        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    /**
     * 处理观众退出信息
     *
     * @param userInfo
     */
    protected void handleMemberQuitMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        if (mCurrentMemberCount > 0) {
            mCurrentMemberCount--;
        } else {
            Log.d(TAG, "接受多次退出请求，目前人数为负数");
        }

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(getString(R.string.trtcliveroom_notification));
        if (TextUtils.isEmpty(userInfo.userName)) {
            entity.setContent(getString(R.string.trtcliveroom_user_quit_live, userInfo.userId));
        } else {
            entity.setContent(getString(R.string.trtcliveroom_user_quit_live, userInfo.userName));
        }
        entity.setType(TCConstants.MEMBER_EXIT);
        notifyMsg(entity);
    }

    /**
     * 处理礼物信息
     *
     * @param text
     */
    protected void handleGiftMsg(String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(getString(R.string.trtcliveroom_notification));
        entity.setContent(text);
        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }


    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        liveCommentDialog.show();
    }


    public void setGroupId(String groupId) {
        //TODO 礼物
        HashMap<String, Object> giftParaMap = new HashMap<>();
        giftParaMap.put("context", this);
        giftParaMap.put("groupId", groupId);
        Map<String, Object> giftRetMap = TUICore.getExtensionInfo("com.tryine.sdgq.common.live.tencent.liteav.tuigift.core.TUIGiftExtension", giftParaMap);
        if (giftRetMap != null && giftRetMap.size() > 0) {
            Object giftSendView = giftRetMap.get("TUIExtensionView");
//            if (giftSendView != null && giftSendView instanceof View) {
//                setGiftView((View) giftSendView);
//                TXCLog.d(TAG, "TUIGift TUIExtensionView getExtensionInfo success");
//            } else {
//                TXCLog.d(TAG, "TUIGift TUIExtensionView getExtensionInfo not find");
//            }

            Object giftDisplayView = giftRetMap.get("TUIGiftPlayView");
            if (giftDisplayView != null && giftDisplayView instanceof View) {
                setGiftShowView((View) giftDisplayView);
                TXCLog.d(TAG, "TUIGift TUIGiftPlayView getExtensionInfo success");
            } else {
                TXCLog.d(TAG, "TUIGift TUIGiftPlayView getExtensionInfo not find");
            }
        } else {
            TXCLog.d(TAG, "TUIGift getExtensionInfo null");
        }
    }


    public void setGiftShowView(View view) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutGiftShow.addView(view, params);
    }

    /**
     * 这里用到的了EventBus框架
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGiftNoticeEvent(GiftNoticeEvent event) {
        handleGiftMsg(event.getModel().extInfo.get("userName") + "送出" + event.getModel().giveDesc);
    }


    PromptDialog invitationpromptDialog;

    /**
     * 信令监听器
     */
    private final class LiveV2TIMSignalingListener extends V2TIMSignalingListener {
        @Override
        public void onReceiveNewInvitation(final String inviteID, final String inviter, String groupID, List<String> inviteeList, String data) {
            String[] datas = data.split("!#!");
                invitationpromptDialog = null;
                invitationpromptDialog = new PromptDialog(CameraPushMainActivity.this, 0, "提示", datas[2] + "邀请你连麦", "接受连麦", "拒绝连麦");
                invitationpromptDialog.show();
                invitationpromptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public void insure() {
                        V2TIMManager.getSignalingManager().accept(inviteID, "", new V2TIMCallback() {
                            @Override
                            public void onSuccess() {
                                //同意连麦
                                startPlay(datas[1]);
                                ll_link.setVisibility(View.VISIBLE);
                                //同意连麦后将播放流放入群公告
                                V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
                                v2TIMGroupInfo.setGroupID(mRoomId + "");
                                v2TIMGroupInfo.setNotification(datas[1]);
                                V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, null);

                                String data = IMProtocol.SignallingDefine.agreeJoinAnchor + "!#!" + datas[0] + "!#!" + datas[1];
                                V2TIMManager.getInstance().sendGroupTextMessage(data, mRoomId + "", V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
                                    @Override
                                    public void onError(int i, String s) {
                                        TRTCLogger.e(TAG, "message send fail, code: " + i + " msg:" + s);
                                    }

                                    @Override
                                    public void onSuccess(V2TIMMessage v2TIMMessage) {
                                    }
                                });

                                int result = mLivePusher.setMixTranscodingConfig(createConfig("1_stream", "26"));
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });

                    }

                    @Override
                    public void cancel() {
                        //拒绝连麦
                        V2TIMManager.getSignalingManager().reject(inviteID, IMProtocol.SignallingDefine.rejectJoinAnchor, null);
                    }
                });

        }

        @Override
        public void onInviteeAccepted(String inviteID, String invitee, String data) {
            TRTCLogger.i(TAG, String.format("onInviteeAccepted enter, inviteID=%s, invitee=%s, data=%s", inviteID, invitee, data));
            SignallingData signallingData = IMProtocol.convert2SignallingData(data);
            if (!IMProtocol.SignallingDefine.VALUE_BUSINESS_ID.equals(signallingData.getBusinessID())) {
                return;
            }
            final String cmd = signallingData.getData().getCmd();
            if (IMProtocol.SignallingDefine.requestJoinAnchor.equals(cmd)) {
                onLinkMicResponseResult(inviteID, true);
            } else if (IMProtocol.SignallingDefine.CMD_REQUESTROOMPK.equals(cmd)) {
                onPkResponseResult(inviteID, invitee, true);
            }
        }

        @Override
        public void onInviteeRejected(final String inviteID, final String invitee, String data) {
            TRTCLogger.i(TAG, String.format("onInviteeRejected enter, inviteID=%s, invitee=%s, data=%s", inviteID, invitee, data));
            SignallingData signallingData = IMProtocol.convert2SignallingData(data);
            if (!IMProtocol.SignallingDefine.VALUE_BUSINESS_ID.equals(signallingData.getBusinessID())) {
                return;
            }
            final String cmd = signallingData.getData().getCmd();
            if (IMProtocol.SignallingDefine.requestJoinAnchor.equals(cmd)) {
                onLinkMicResponseResult(inviteID, false);
            } else if (IMProtocol.SignallingDefine.CMD_REQUESTROOMPK.equals(cmd)) {
                onPkResponseResult(inviteID, invitee, false);
            }
        }

        private void onLinkMicResponseResult(String inviteID, boolean agree) {

        }

        private void onPkResponseResult(final String inviteID, final String userId, boolean agree) {

        }

        @Override
        public void onInvitationCancelled(String inviteID, String inviter, String data) {
            if (null != invitationpromptDialog) {
                invitationpromptDialog.dismiss();
            }
            ToastUtils.showShort(data + "取消了连麦");
        }

        @Override
        public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
            TRTCLogger.i(TAG, String.format("onInvitationTimeout enter, inviteID=%s, inviteeList=%s", inviteID, inviteeList));
        }
    }

    //进入连麦推流
    private void startPlay(String data) {
        if (mLivePlayer == null) {
            mLivePlayer = new V2TXLivePlayerImpl(this);
            mLivePlayer.setRenderView(mPusherView1);
            mLivePlayer.setObserver(new V2TXLivePlayerObserver() {

                @Override
                public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
                    Log.e(TAG, "[Player] onError: player-" + player + " code-" + code + " msg-" + msg + " info-" + extraInfo);
                }

                @Override
                public void onVideoLoading(V2TXLivePlayer player, Bundle extraInfo) {
                    Log.i(TAG, "[Player] onVideoLoading: player-" + player + ", extraInfo-" + extraInfo);
                }

                @Override
                public void onVideoPlaying(V2TXLivePlayer player, boolean firstPlay, Bundle extraInfo) {
                    Log.i(TAG, "[Player] onVideoPlaying: player-"
                            + player + " firstPlay-" + firstPlay + " info-" + extraInfo);
                }

                @Override
                public void onVideoResolutionChanged(V2TXLivePlayer player, int width, int height) {
                    Log.i(TAG, "[Player] onVideoResolutionChanged: player-"
                            + player + " width-" + width + " height-" + height);
                }
            });
        }
        int result = mLivePlayer.startPlay(data);
        Log.d(TAG, "startPlay : " + result);
    }

    private V2TXLiveDef.V2TXLiveTranscodingConfig createConfig(String linkStreamId, String linkUserId) {
        V2TXLiveDef.V2TXLiveTranscodingConfig config = new V2TXLiveDef.V2TXLiveTranscodingConfig();
        config.videoWidth = 360;
        config.videoHeight = 640;
        config.videoBitrate = 900;
        config.videoFramerate = 15;
        config.videoGOP = 2;
        config.backgroundColor = 0x000000;
        config.backgroundImage = null;
        config.audioSampleRate = 48000;
        config.audioBitrate = 64;
        config.audioChannels = 1;
        config.outputStreamId = null;
        config.mixStreams = new ArrayList<>();

        V2TXLiveDef.V2TXLiveMixStream mixStream = new V2TXLiveDef.V2TXLiveMixStream();
        mixStream.userId = "";
        mixStream.streamId = "";
        mixStream.x = 0;
        mixStream.y = 0;
        mixStream.width = 360;
        mixStream.height = 640;
        mixStream.zOrder = 0;
        mixStream.inputType = V2TXLiveMixInputTypePureVideo;
        config.mixStreams.add(mixStream);

        V2TXLiveDef.V2TXLiveMixStream remote = new V2TXLiveDef.V2TXLiveMixStream();
        remote.userId = "125";
        remote.streamId = "ShengDianPush1221648713993201";
        remote.x = 150;
        remote.y = 300;
        remote.width = 135;
        remote.height = 240;
        remote.zOrder = 1;
        mixStream.inputType = V2TXLiveMixInputTypePureVideo;
        config.mixStreams.add(remote);
        return config;
    }


    @Override
    public void onBackPressed() {
        showExitInfoDialog(getString(R.string.trtcliveroom_warning_anchor_exit_room), false);
    }


}
