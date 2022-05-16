package com.tryine.sdgq.common.live.activity.push;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupChangeInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupListener;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.tencent.live2.V2TXLivePusher;
import com.tencent.live2.impl.V2TXLivePlayerImpl;
import com.tencent.live2.impl.V2TXLivePusherImpl;
import com.tencent.qcloud.tim.uikit.modules.search.groupinterface.TUISearchGroupResult;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.live.activity.LiveCourseDetailActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDef;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TRTCLogger;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TXCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TXUserInfo;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.ITXRoomServiceDelegate;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.impl.IMProtocol;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.impl.SignallingData;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.impl.TXRoomService;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.audience.TCAudienceActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.audience.TCLikeFrequencyControl;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.msg.TCChatEntity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.msg.TCChatMsgListAdapter;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCConstants;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCUtils;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.widget.InputTextMsgDialog;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.TUIGiftButton;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.mine.fragment.MineFragment;
import com.tryine.sdgq.common.mine.presenter.LiveInPresenter;
import com.tryine.sdgq.common.mine.view.LiveInView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.event.GiftNoticeEvent;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.LiveCommentDialog;
import com.tryine.sdgq.view.dialog.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

import static com.tencent.live2.V2TXLiveCode.V2TXLIVE_ERROR_INVALID_PARAMETER;
import static com.tencent.live2.V2TXLiveCode.V2TXLIVE_OK;
import static com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDef.ROOM_STATUS_PK;

/**
 * 观众端
 * 腾讯云 {@link V2TXLivePlayer} 直播播放器 V2 使用参考 Demo
 * 有以下功能参考 ：
 * - 基本功能参考： 启动推流 {@link #startPlay()}与 结束推流 {@link #stopPlay()}
 * - 渲染角度、渲染模式切换： 横竖屏渲染、铺满与自适应渲染
 * - 缓存策略选择：{@link #setCacheStrategy} 缓存策略：自动、极速、流畅。 极速模式：时延会尽可能低、但抗网络抖动效果不佳；流畅模式：时延较高、抗抖动能力较强
 */
public class LivePlayerMainActivity extends Activity implements LiveInView, LiveCommentDialog.OnTextSendListener {

    private static final String TAG = "LivePlayerActivity";

    private Context mContext;

    private ImageView mImageLoading;          //显示视频缓冲动画
    private RelativeLayout mLayoutRoot;            //视频暂停时更新背景

    private int mLogClickCount = 0;

    private V2TXLivePlayer mLivePlayer;               //直播拉流的视频播放器
    private V2TXLivePlayer mLivePlayer1; //其他关注观看连麦
    private TXCloudVideoView mVideoView;

    private V2TXLivePusher mLivePusher;
    private TXCloudVideoView mVideoView1;

    private boolean mIsPlaying = false;
    private boolean mFetching = false;          //是否正在获取视频源，测试专用

    private int mCacheStrategy = Constants.CACHE_STRATEGY_AUTO;                    //Player缓存策略
    private int mActivityPlayType = Constants.ACTIVITY_TYPE_LIVE_PLAY;                //播放类型
    private V2TXLiveDef.V2TXLiveFillMode mRenderMode = V2TXLiveDef.V2TXLiveFillMode.V2TXLiveFillModeFill;    //Player 当前渲染模式
    private V2TXLiveDef.V2TXLiveRotation mRenderRotation = V2TXLiveDef.V2TXLiveRotation.V2TXLiveRotation0;         //Player 当前渲染角度

    private OkHttpClient mOkHttpClient = null;   //获取超低时延视频源直播地址

    private String mRoomId;
    private String userId;//老师用户id
    private String teacherName = "";       // 老师昵称
    private String teacherHead;            // 主播头像
    private String mPlayURL = "";          // 推流地址
    private String streamName = "";          // 直播间名称


    private ImageView mImageAnchorAvatar;     // 显示房间主播头像
    private TextView mTextAnchorName;        // 显示房间主播昵称
    private TextView mTextRoomId;            // 显示当前房间号
    private TextView btn_close;            // 显示当前房间号
    private Button btn_message_input;            // 消息按钮
    private TUIGiftButton audience_btn_gift;            // 礼物控件
    private RelativeLayout mLayoutGiftShow;            // 礼物控件
    private ListView mLvMessage;             // 消息控件
    private FrameLayout ll_link;             // 连麦控件
    private ImageView iv_close;             // 连麦控件
    LiveCommentDialog liveCommentDialog;   // 消息输入框
    private Button mButtonLinkMic;         // 连麦按钮
    private TCChatMsgListAdapter mChatMsgListAdapter;    // 消息列表的Adapter
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();   // 消息内容

    private boolean mIsBeingLinkMic = false;    // 表示当前是否在连麦状态
    private long mLastLinkMicTime;            // 上次发起连麦的时间，用于频率控制
    private static final long LINK_MIC_INTERVAL = 3 * 1000;    //连麦间隔控制
    private static final int LINK_MIC_TIMEOUT = 15;          // 连麦超时时间

    String pushUrl;

    UserBean userBean;

    //自带参数
    LiveInPresenter liveInPresenter;

    public static void start(Context context, String roomId) {
        Intent intent = new Intent();
        intent.setClass(context, LivePlayerMainActivity.class);
        intent.putExtra(TCConstants.GROUP_ID, roomId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.transparent).
                statusBarDarkFont(false).
                navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();
        mContext = this;
        setContentView(R.layout.liveplayer_activity_live_player_main);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        EventBus.getDefault().register(this);
        liveInPresenter = new LiveInPresenter(this);
        liveInPresenter.attachView(this);
        initialize();
    }

    private void initialize() {
        mLayoutRoot = (RelativeLayout) findViewById(R.id.liveplayer_rl_root);
        mImageAnchorAvatar = (ImageView) findViewById(R.id.iv_anchor_head);
        mTextRoomId = (TextView) findViewById(R.id.tv_room_id);
        mLvMessage = (ListView) findViewById(R.id.lv_im_msg);
        ll_link = (FrameLayout) findViewById(R.id.ll_link);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        btn_message_input = (Button) findViewById(R.id.btn_message_input);
        audience_btn_gift = findViewById(R.id.audience_btn_gift);
        mLayoutGiftShow = findViewById(R.id.rl_gift_show);
        mButtonLinkMic = (Button) findViewById(R.id.audience_btn_linkmic);
        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mLvMessage, mArrayListChatEntity);
        mLvMessage.setAdapter(mChatMsgListAdapter);
        findViewById(R.id.iv_anchor_record_ball).setVisibility(View.GONE);
        initData();
        initPlayView();
        initPlayButton();
        initNavigationBack();
    }

    private void initData() {
        Intent intent = getIntent();
        mRoomId = intent.getStringExtra(TCConstants.GROUP_ID);
        liveInPresenter.getLiveroomdetail(mRoomId);
        audience_btn_gift.setGroupId(this, mRoomId + "");
        setGroupId(mRoomId + "");
        liveInPresenter.countroominfo(mRoomId,0);
        joinGroup();

    }

    private void initPlayView() {
        mVideoView = (TXCloudVideoView) findViewById(R.id.liveplayer_video_view);
        mVideoView1 = (TXCloudVideoView) findViewById(R.id.tx_cloud_view_audience);
        mLivePlayer = new V2TXLivePlayerImpl(mContext);
        mLivePlayer.setCacheParams(Constants.CACHE_TIME_FAST, Constants.CACHE_TIME_SMOOTH);

        liveCommentDialog = new LiveCommentDialog(this, R.style.dialog_center);
        liveCommentDialog.setmOnTextSendListener(this);


        mImageAnchorAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //主播头像
                PersonalHomePageActivity.start(getApplicationContext(), userId);
            }
        });

        mTextRoomId.setText(String.format(getString(R.string.trtcliveroom_room_id), String.valueOf(mRoomId)));


    }

    private void joinGroup() {
        /**
         * 加入群聊
         */
        V2TIMManager.getInstance().joinGroup(mRoomId + "", "", new V2TIMCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(int i, String s) {
                // 已经是群成员了，可以继续操作
                if (i == 10013) {
                    onSuccess();
                } else {
                    TRTCLogger.e(TAG, "enter room fail, code:" + i + " msg:" + s);
                }
            }
        });
        List<String> groupList = new ArrayList<>();
        groupList.add(mRoomId + "");
        V2TIMManager.getGroupManager().getGroupsInfo(groupList, new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                V2TIMGroupInfoResult result = v2TIMGroupInfoResults.get(0);
                if (!TextUtils.isEmpty(result.getGroupInfo().getNotification())) { //群公告里面有地址，表示在连麦中，后面进来的观众直接播放连麦流
                    startPlay(result.getGroupInfo().getNotification());
                    ll_link.setVisibility(View.VISIBLE);
                    mIsBeingLinkMic = true;
                }
            }
        });
        V2TIMManager.getInstance().setGroupListener(new LiveRoomGroupListener());

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
        V2TIMManager.getSignalingManager().addSignalingListener(new LiveV2TIMSignalingListener());
    }

    private class LiveRoomGroupListener extends V2TIMGroupListener {
        @Override
        public void onMemberEnter(String groupID, List<V2TIMGroupMemberInfo> memberList) {

        }

        @Override
        public void onMemberLeave(String groupID, V2TIMGroupMemberInfo member) {

        }

        @Override
        public void onGroupDismissed(String groupID, V2TIMGroupMemberInfo opUser) {
            try{
                TRTCLogger.i(TAG, "recv room destroy msg");
                // 如果发现房间已经解散，那么内部退一次房间
                PromptDialog promptDialog = new PromptDialog(LivePlayerMainActivity.this, 0, "提示",
                        "老师已下播", "确认", "");
                promptDialog.show();
                promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public void insure() {
                        finish();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }catch (Exception e){

            }
        }

        @Override
        public void onGroupInfoChanged(String groupID, List<V2TIMGroupChangeInfo> changeInfos) {
            super.onGroupInfoChanged(groupID, changeInfos);
        }
    }


    private void initPlayButton() {
        btn_message_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liveInPresenter.userdetail();
                showInputMsgDialog();
            }
        });

    }


    private void initNavigationBack() {
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy();
                V2TIMManager.getInstance().quitGroup(mRoomId + "", new V2TIMCallback() {
                    @Override
                    public void onError(int i, String s) {
                        TRTCLogger.e(TAG, "exit room fail, code:" + i + " msg:" + s);

                    }

                    @Override
                    public void onSuccess() {
//                        TRTCLogger.i(TAG, "exit room success.");
//                        V2TIMManager.getInstance().removeSimpleMsgListener(mSimpleListener);
//                        V2TIMManager.getInstance().setGroupListener(null);
//                        cleanStatus();
//
//                        if (callback != null) {
//                            callback.onCallback(0, "exit room success.");
//                        }
                    }
                });


                finish();
            }
        });

        mButtonLinkMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsBeingLinkMic) {
                    long curTime = System.currentTimeMillis();
                    if (curTime < mLastLinkMicTime + LINK_MIC_INTERVAL) {
                        Toast.makeText(getApplicationContext(), R.string.trtcliveroom_tips_rest, Toast.LENGTH_SHORT).show();
                    } else {
                        mLastLinkMicTime = curTime;
                        startLinkMic();
                    }
                } else {
                    ToastUtil.toastLongMessage("老师与其他学生连麦中，请稍后再试");
                }
            }
        });

    }

    /**
     * 生命周期相关
     */
    private void startLinkMic() {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                if (permissionsGranted.size() == 2) {
                    onStartLinkMic();
                }
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                ToastUtils.showShort(R.string.trtcliveroom_tips_start_camera_audio);
            }
        }).request();
    }

    private void onStartLinkMic() {
        mButtonLinkMic.setEnabled(false);
        mButtonLinkMic.setBackgroundResource(R.mipmap.trtcliveroom_linkmic_off);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                liveInPresenter.getUsertrtcurl(mRoomId);
            }
        });

    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_close) {
            V2TIMManager.getInstance().sendGroupTextMessage(IMProtocol.SignallingDefine.closeJoinAnchor, mRoomId + "", V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    TRTCLogger.e(TAG, "message send fail, code: " + i + " msg:" + s);
                }

                @Override
                public void onSuccess(V2TIMMessage v2TIMMessage) {
                    mIsBeingLinkMic = false;
                    mButtonLinkMic.setEnabled(true);
                    mButtonLinkMic.setBackgroundResource(R.mipmap.trtcliveroom_linkmic_on);
                    ll_link.setVisibility(View.GONE);
                    if (mLivePusher != null) {
                        mLivePusher.stopPush();
                        mLivePusher = null;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        stopPlay();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
        EventBus.getDefault().unregister(this);
        //清空粘性事件的缓存
        EventBus.getDefault().removeAllStickyEvents();
    }

    private void startLoadingAnimation() {
        if (mImageLoading != null) {
            mImageLoading.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mImageLoading.getDrawable()).start();
        }
    }

    private void stopLoadingAnimation() {
        if (mImageLoading != null) {
            mImageLoading.setVisibility(View.GONE);
            ((AnimationDrawable) mImageLoading.getDrawable()).stop();
        }
    }

    public void onPlayStart(int code) {
        switch (code) {
            case V2TXLIVE_OK:
                startLoadingAnimation();
                break;
            case V2TXLIVE_ERROR_INVALID_PARAMETER:
                Toast.makeText(mContext, "播放地址不合法，直播目前仅支持rtmp，flv播放方式!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    private void startPlay() {
        String playURL = mPlayURL;
        int code = checkPlayURL(playURL);
        if (code != Constants.PLAY_STATUS_SUCCESS) {
            mIsPlaying = false;
        } else {
            mLivePlayer.setRenderView(mVideoView);
            mLivePlayer.setObserver(new MyPlayerObserver());

            mLivePlayer.setRenderRotation(mRenderRotation);
            mLivePlayer.setRenderFillMode(mRenderMode);

            /**
             * result返回值：
             * 0 V2TXLIVE_OK; -2 V2TXLIVE_ERROR_INVALID_PARAMETER; -3 V2TXLIVE_ERROR_REFUSED;
             */
            code = mLivePlayer.startPlay(playURL);
            mIsPlaying = code == 0;

            Log.d("video render", "timetrack start play");
        }

        //处理UI相关操作
        onPlayStart(code);
    }

    private void stopPlay() {
        if (!mIsPlaying) {
            return;
        }
        if (mLivePlayer != null) {
            mLivePlayer.setObserver(null);
            mLivePlayer.stopPlay();
        }
        mIsPlaying = false;

    }

    private void setPlayURL(int activityPlayType, String url) {
        mActivityPlayType = activityPlayType;
        mPlayURL = url;
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
        this.userId = userId;
        this.streamName = streamName;

        TCUtils.showPicWithUrl(LivePlayerMainActivity.this, mImageAnchorAvatar, teacherHead, R.mipmap.trtcliveroom_bg_cover);
        mTextAnchorName = (TextView) findViewById(R.id.tv_anchor_broadcasting_time);
        mTextAnchorName.setText(TCUtils.getLimitString(teacherName, 10));
        int activityType = getIntent().getIntExtra(Constants.INTENT_ACTIVITY_TYPE, Constants.ACTIVITY_TYPE_LIVE_PLAY);
        setPlayURL(activityType, mPlayURL);
        // 初始化完成之后自动播放
        startPlay();
    }

    PromptDialog invitepromptDialog;
    String inviteID;

    @Override
    public void onGetUsertrtcurlSuccess(String pushUrl, String playUrl) {
        this.pushUrl = pushUrl;
        invitepromptDialog = new PromptDialog(LivePlayerMainActivity.this, 0, "提示", "请求老师连麦中...", "取消", "");
        invitepromptDialog.show();
        invitepromptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                V2TIMManager.getSignalingManager().cancel(inviteID, userBean.getUserName(), null);
            }

            @Override
            public void cancel() {
            }
        });


        inviteID = V2TIMManager.getSignalingManager().invite(userId, userBean.getId() + "!#!" + playUrl + "!#!" + userBean.getUserName(), true, null, LINK_MIC_TIMEOUT, null);
    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {
        SPUtils.saveString(Parameter.USER_INFO, new Gson().toJson(userBean));
    }

    @Override
    public void onFailed(String message) {

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

    private class MyPlayerObserver extends V2TXLivePlayerObserver {

        @Override
        public void onWarning(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
            Log.w(TAG, "[Player] onWarning: player-" + player + " code-" + code + " msg-" + msg + " info-" + extraInfo);
        }

        @Override
        public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
            Log.e(TAG, "[Player] onError: player-" + player + " code-" + code + " msg-" + msg + " info-" + extraInfo);
        }

        @Override
        public void onSnapshotComplete(V2TXLivePlayer v2TXLivePlayer, Bitmap bitmap) {
        }

        @Override
        public void onVideoPlaying(V2TXLivePlayer player, boolean firstPlay, Bundle extraInfo) {
            Log.e(TAG, "[Player] onVideoPlaying  firstPlay -> " + firstPlay);
            stopLoadingAnimation();
//            Bundle params = new Bundle();
//            params.putString(TXLiveConstants.EVT_DESCRIPTION,
//                    mContext.getResources().getString(R.string.liveplayer_warning_checkout_res_url));
//            mLogInfoWindow.setLogText(null, params, LogInfoWindow.CHECK_RTMP_URL_OK);
        }

        @Override
        public void onVideoLoading(V2TXLivePlayer player, Bundle extraInfo) {
            Log.e(TAG, "[Player] onVideoLoading");
            startLoadingAnimation();
        }

        @Override
        public void onAudioPlaying(V2TXLivePlayer player, boolean firstPlay, Bundle extraInfo) {
            Log.e(TAG, "[Player] onAudioPlaying  firstPlay -> " + firstPlay);
            stopLoadingAnimation();
        }

        @Override
        public void onAudioLoading(V2TXLivePlayer player, Bundle extraInfo) {
            Log.e(TAG, "[Player] onAudioLoading");
            startLoadingAnimation();
        }

        @Override
        public void onPlayoutVolumeUpdate(V2TXLivePlayer player, int volume) {
            //            Log.i(TAG, "onPlayoutVolumeUpdate: player-" + player +  ", volume-" + volume);
        }

        @Override
        public void onStatisticsUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayerStatistics statistics) {
        }

    }

    private void setCacheStrategy(int cacheStrategy) {
        if (mCacheStrategy == cacheStrategy) {
            return;
        }
        mCacheStrategy = cacheStrategy;
        switch (cacheStrategy) {
            case Constants.CACHE_STRATEGY_FAST:
                mLivePlayer.setCacheParams(Constants.CACHE_TIME_FAST, Constants.CACHE_TIME_FAST);
                break;
            case Constants.CACHE_STRATEGY_SMOOTH:
                mLivePlayer.setCacheParams(Constants.CACHE_TIME_SMOOTH, Constants.CACHE_TIME_SMOOTH);
                break;
            case Constants.CACHE_STRATEGY_AUTO:
                mLivePlayer.setCacheParams(Constants.CACHE_TIME_FAST, Constants.CACHE_TIME_SMOOTH);
                break;
            default:
                break;
        }
    }

    private void setRenderMode(V2TXLiveDef.V2TXLiveFillMode renderMode) {
        mRenderMode = renderMode;
        mLivePlayer.setRenderFillMode(renderMode);
    }

    private V2TXLiveDef.V2TXLiveFillMode getRenderMode() {
        return mRenderMode;
    }

    private void setRenderRotation(V2TXLiveDef.V2TXLiveRotation renderRotation) {
        mRenderRotation = renderRotation;
        mLivePlayer.setRenderRotation(renderRotation);
    }

    private V2TXLiveDef.V2TXLiveRotation getRenderRotation() {
        return mRenderRotation;
    }

    private void showVideoLog(boolean enable) {
        mVideoView.showLog(enable);
    }

    private void destroy() {
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay();
            mLivePlayer = null;
        }
        if (mLivePusher != null) {
            mLivePusher.stopPush();
            mLivePusher = null;
        }

        if (mVideoView != null) {
            mVideoView.onDestroy();
            mVideoView = null;
        }
        if (mVideoView1 != null) {
            mVideoView1.onDestroy();
            mVideoView1 = null;
        }

        if(!mButtonLinkMic.isEnabled()){
            V2TIMManager.getInstance().sendGroupTextMessage(IMProtocol.SignallingDefine.closeJoinAnchor, mRoomId + "", V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                }

                @Override
                public void onSuccess(V2TIMMessage v2TIMMessage) {
                }
            });
        }


    }

    private int checkPlayURL(final String playURL) {
        if (TextUtils.isEmpty(playURL)) {
            return Constants.PLAY_STATUS_EMPTY_URL;
        }

        if (!playURL.startsWith(Constants.URL_PREFIX_HTTP) && !playURL.startsWith(Constants.URL_PREFIX_HTTPS)
                && !playURL.startsWith(Constants.URL_PREFIX_RTMP) && !playURL.startsWith("/")) {
            return Constants.PLAY_STATUS_INVALID_URL;
        }

        boolean isLiveRTMP = playURL.startsWith(Constants.URL_PREFIX_RTMP);
        boolean isLiveFLV = (playURL.startsWith(Constants.URL_PREFIX_HTTP) || playURL.startsWith(Constants.URL_PREFIX_HTTPS)) && playURL.contains(Constants.URL_SUFFIX_FLV);

        if (mActivityPlayType == Constants.ACTIVITY_TYPE_LIVE_PLAY) {
            if (isLiveRTMP) {
                return Constants.PLAY_STATUS_SUCCESS;
            }
            if (isLiveFLV) {
                return Constants.PLAY_STATUS_SUCCESS;
            }
            return Constants.PLAY_STATUS_INVALID_URL;
        }

        if (mActivityPlayType == Constants.ACTIVITY_TYPE_REALTIME_PLAY) {
            if (!isLiveRTMP) {
                return Constants.PLAY_STATUS_INVALID_RTMP_URL;
            }
            if (!playURL.contains(Constants.URL_TX_SECRET)) {
                return Constants.PLAY_STATUS_INVALID_SECRET_RTMP_URL;
            }
            return Constants.PLAY_STATUS_SUCCESS;
        }
        return Constants.PLAY_STATUS_INVALID_URL;
    }


    /**
     * 这里用到的了EventBus框架
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGiftNoticeEvent(GiftNoticeEvent event) {
        handleGiftMsg(event.getModel().extInfo.get("userName") + "送出" + event.getModel().giveDesc);
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


    private void notifyMsg(final TCChatEntity entity) {
        if (entity.getContent().contains(IMProtocol.SignallingDefine.closeJoinAnchor)) {
            //老师关闭了连麦
            mButtonLinkMic.setEnabled(true);
            mButtonLinkMic.setBackgroundResource(R.mipmap.trtcliveroom_linkmic_on);
            ll_link.setVisibility(View.GONE);
            mIsBeingLinkMic = false;
            if (mLivePusher != null) {
                mLivePusher.stopPush();
                mLivePusher = null;
            }
            mIsBeingLinkMic = false;
        } else if (entity.getContent().contains(IMProtocol.SignallingDefine.agreeJoinAnchor)) { //主播同意连麦后，所有观众接收到消息，并打开连麦观众的播放流
            String[] datas = entity.getContent().split("!#!");
            if (!datas[1].equals(SPUtils.getString(Parameter.USER_ID))) {
                startPlay(datas[2]);
                ll_link.setVisibility(View.VISIBLE);
                mIsBeingLinkMic = true;
            }
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


    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        liveCommentDialog.show();
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


    private void startPush(String pushUrl) {
        mLivePusher = new V2TXLivePusherImpl(this, V2TXLiveDef.V2TXLiveMode.TXLiveMode_RTC);
        iv_close.setVisibility(View.VISIBLE);
        mLivePusher.setRenderView(mVideoView1);
        mLivePusher.startCamera(true);
        int ret = mLivePusher.startPush(pushUrl);
        Log.i(TAG, "startPush return: " + ret);
        mLivePusher.startMicrophone();
    }

    //进入连麦推流
    private void startPlay(String data) {
        if (mLivePlayer1 == null) {
            mLivePlayer1 = new V2TXLivePlayerImpl(this);
            mLivePlayer1.setRenderView(mVideoView1);
        }
        iv_close.setVisibility(View.GONE);
        int result = mLivePlayer1.startPlay(data);
        Log.d(TAG, "startPlay : " + result);
    }


    /**
     * 信令监听器
     */
    private final class LiveV2TIMSignalingListener extends V2TIMSignalingListener {

        @Override
        public void onReceiveNewInvitation(final String inviteID, final String inviter, String groupID, List<String> inviteeList, String data) {
            String[] datas = data.split("!#!");
            if (IMProtocol.SignallingDefine.closeJoinAnchor.equals(datas[0])) {
                ToastUtil.toastLongMessage("关闭了连麦");

            }

        }

        @Override
        public void onInviteeAccepted(String inviteID, String invitee, String data) {
            if (null != invitepromptDialog) {
                invitepromptDialog.dismiss();
            }
            //主播同意了连麦
            startPush(pushUrl);
            ll_link.setVisibility(View.VISIBLE);
        }

        @Override
        public void onInviteeRejected(final String inviteID, final String invitee, String data) {
            //主播拒绝连麦
            if (null != invitepromptDialog) {
                invitepromptDialog.dismiss();
            }
            ToastUtil.toastLongMessage("老师拒绝了您的连麦");
            mButtonLinkMic.setEnabled(true);
            mButtonLinkMic.setBackgroundResource(R.mipmap.trtcliveroom_linkmic_on);
        }

        private void onLinkMicResponseResult(String inviteID, boolean agree) {
            ToastUtil.toastLongMessage("onLinkMicResponseResult");
        }

        private void onPkResponseResult(final String inviteID, final String userId, boolean agree) {
            ToastUtil.toastLongMessage("onPkResponseResult");
        }

        @Override
        public void onInvitationCancelled(String inviteID, String inviter, String data) {
            mButtonLinkMic.setEnabled(true);
            mButtonLinkMic.setBackgroundResource(R.mipmap.trtcliveroom_linkmic_on);
        }

        @Override
        public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
            ToastUtil.toastLongMessage("onInvitationTimeout");
            TRTCLogger.i(TAG, String.format("onInvitationTimeout enter, inviteID=%s, inviteeList=%s", inviteID, inviteeList));
        }
    }

}