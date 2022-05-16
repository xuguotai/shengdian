package com.tryine.sdgq.common.circle.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerVideoId;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.rtmp.TXLiveConstants;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.VideoModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频全屏播放
 * @author: zhangshuaijun
 * @time: 2021-12-20 13:47
 */
public class VideoPlayFullScreenActivity extends BaseActivity implements SuperPlayerView.OnSuperPlayerViewCallback {
    @BindView(R.id.rl_bar)
    RelativeLayout rl_bar;
    @BindView(R.id.superVodPlayerView)
    SuperPlayerView mSuperPlayerView;

    String videoUrl;

    public static void start(Context context, String videoUrl) {
        Intent intent = new Intent();
        intent.setClass(context, VideoPlayFullScreenActivity.class);
        intent.putExtra("videoUrl", videoUrl);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_videoplay_full;
    }

    @Override
    protected void init() {
        setBlackBar();
        initSuperVodGlobalSetting();
        videoUrl = getIntent().getStringExtra("videoUrl");
        mSuperPlayerView.setPlayerViewCallback(this);
        mSuperPlayerView.setLoop(true);
        mSuperPlayerView.showOrHideBackBtn(false);

        VideoModel videoModel = new VideoModel();
        videoModel.setVideoUrl(videoUrl);

        playVideoModel(videoModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSuperPlayerView.getPlayerState() == SuperPlayerDef.PlayerState.PLAYING
                || mSuperPlayerView.getPlayerState() == SuperPlayerDef.PlayerState.PAUSE) {
            if (!mSuperPlayerView.isShowingVipView()) {
                mSuperPlayerView.onResume();
            }
            if (mSuperPlayerView.getPlayerMode() == SuperPlayerDef.PlayerMode.FLOAT) {
                mSuperPlayerView.switchPlayMode(SuperPlayerDef.PlayerMode.WINDOW);
            }
        }
    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }

    @Override
    public void onStartFullScreenPlay() {
        rl_bar.setVisibility(View.GONE);
        mSuperPlayerView.showOrHideBackBtn(true);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.black)
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色
                .navigationBarColor(R.color.black)
                .init();
    }

    @Override
    public void onStopFullScreenPlay() {
        setBlackBar();
        mSuperPlayerView.showOrHideBackBtn(false);
        rl_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickHandleVip() {

    }

    @Override
    public void onClickFloatCloseBtn() {

    }

    @Override
    public void onClickSmallReturnBtn() {

    }

    @Override
    public void onStartFloatWindowPlay() {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPlayEnd() {

    }

    @Override
    public void onError(int code) {

    }

    /**
     * 初始化超级播放器全局配置
     */
    private void initSuperVodGlobalSetting() {
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = true;
        // 设置悬浮窗的初始位置和宽高
        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
        rect.x = 0;
        rect.y = 0;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        //需要修改为自己的时移域名
        prefs.playShiftDomain = "liteavapp.timeshift.qcloud.com";
    }


    private void playVideoModel(VideoModel videoModel) {
        final SuperPlayerModel superPlayerModelV3 = new SuperPlayerModel();
        superPlayerModelV3.appId = videoModel.appid;
        superPlayerModelV3.playAction = 1;
        superPlayerModelV3.vipWatchMode = videoModel.vipWatchModel;
        if (!TextUtils.isEmpty(videoModel.getVideoUrl())) {
            if (isSuperPlayerVideo(videoModel)) {
                playSuperPlayerVideo(videoModel);
                return;
            } else {
                superPlayerModelV3.title = videoModel.title;
                superPlayerModelV3.url = videoModel.getVideoUrl();

                superPlayerModelV3.multiURLs = new ArrayList<>();
                if (videoModel.multiVideoURLs != null) {
                    for (VideoModel.VideoPlayerURL modelURL : videoModel.multiVideoURLs) {
                        superPlayerModelV3.multiURLs.add(new SuperPlayerModel.SuperPlayerURL(modelURL.url, modelURL.title));
                    }
                }
            }
        } else if (!TextUtils.isEmpty(videoModel.fileid)) {
            superPlayerModelV3.videoId = new SuperPlayerVideoId();
            superPlayerModelV3.videoId.fileId = videoModel.fileid;
            superPlayerModelV3.videoId.pSign = videoModel.pSign;
        }
        superPlayerModelV3.playAction = videoModel.playAction;
        superPlayerModelV3.placeholderImage = videoModel.placeholderImage;
        superPlayerModelV3.coverPictureUrl = videoModel.coverPictureUrl;
        superPlayerModelV3.duration = videoModel.duration;
        mSuperPlayerView.playWithModel(superPlayerModelV3);
    }


    private boolean isSuperPlayerVideo(VideoModel videoModel) {
        return videoModel.getVideoUrl().startsWith("txsuperplayer://play_vod");
    }


    private boolean playSuperPlayerVideo(VideoModel videoModel) {
        final SuperPlayerModel model = new SuperPlayerModel();
        String videoUrl = videoModel.getVideoUrl();
        String appIdStr = getValueByName(videoUrl, "appId");
        boolean rst = true;
        try {
            model.appId = appIdStr.equals("") ? 0 : Integer.valueOf(appIdStr);
            SuperPlayerVideoId videoId = new SuperPlayerVideoId();
            videoId.fileId = getValueByName(videoUrl, "fileId");
            videoId.pSign = getValueByName(videoUrl, "psign");
            model.videoId = videoId;
            mSuperPlayerView.playWithModel(model);
        } catch (Exception e) {
            rst = false;
        }
        return rst;
    }


    private String getValueByName(String url, String name) { //txsuperplayer://play_vod?v=4&appId=1400295357&fileId=5285890796599775084&pcfg=Default
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.startsWith(name + "=")) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            mSuperPlayerView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuperPlayerView.release();
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            mSuperPlayerView.resetPlayer();
        }
    }
}
