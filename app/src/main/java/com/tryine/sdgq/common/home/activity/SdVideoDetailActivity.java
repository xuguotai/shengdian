package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerVideoId;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.model.VipWatchModel;
import com.tencent.rtmp.TXLiveConstants;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.home.adapter.SdVideoHomeMenuAdapter;
import com.tryine.sdgq.common.home.adapter.VideoSheetMusicAdapter;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SdVideoHomePresenter;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.common.mine.adapter.TabAdapter1;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.banner.BannerViewPager;
import com.tryine.sdgq.view.banner.VideoPlayerController;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.ShareDialog;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.inter.listener.OnPlayerTypeListener;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;
import org.yczbj.ycvideoplayerlib.utils.VideoPlayerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圣典视频详情
 *
 * @author: zhangshuaijun
 * @time: 2021-11-25 10:35
 */
public class SdVideoDetailActivity extends BaseActivity implements SuperPlayerView.OnSuperPlayerViewCallback, SdHomeVideoView {

    @BindView(R.id.superVodPlayerView)
    SuperPlayerView mSuperPlayerView;

    @BindView(R.id.rl_bar)
    RelativeLayout rl_bar;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.ll_sheetMusic)
    LinearLayout ll_sheetMusic;
    @BindView(R.id.ll_hj)
    LinearLayout ll_hj;
    @BindView(R.id.ll_js)
    LinearLayout ll_js;

    List<SheetMusicBean> sheetMusicBeanList = new ArrayList<SheetMusicBean>();

    @BindView(R.id.tv_collect)
    TextView tv_collect;
    @BindView(R.id.tv_gz)
    TextView tv_gz;
    @BindView(R.id.tv_hj)
    TextView tv_hj;
    @BindView(R.id.tv_videoTitle)
    TextView tv_videoTitle;
    @BindView(R.id.tv_look)
    TextView tv_look;
    @BindView(R.id.iv_teacherHeadImg)
    CircleImageView iv_teacherHeadImg;
    @BindView(R.id.tv_teacherName)
    TextView tv_teacherName;
    @BindView(R.id.iv_goldenBean)
    ImageView iv_goldenBean;
    @BindView(R.id.tv_goldenBean)
    TextView tv_goldenBean;

    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    VideoSheetMusicAdapter videoSheetMusicAdapter;

    @BindView(R.id.rv_hjdata)
    RecyclerView rv_hjdata;
    List<VideoModel> videoModelLists = new ArrayList<VideoModel>();
    SdHomeVideoAdapter sdHomeVideoAdapter;

    SdVideoHomePresenter sdVideoHomePresenter;

    String videoId;

    VideoModel videoModel;

    public static void start(Context context, String videoId) {
        Intent intent = new Intent();
        intent.setClass(context, SdVideoDetailActivity.class);
        intent.putExtra("videoId", videoId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_dateil;
    }

    @Override
    protected void init() {
        setWhiteBar();
        initSuperVodGlobalSetting();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        videoId = getIntent().getStringExtra("videoId");

        sdVideoHomePresenter = new SdVideoHomePresenter(this);
        sdVideoHomePresenter.attachView(this);
        sdVideoHomePresenter.getVideodetail(videoId);

        initViews();
    }


    private void initViews() {
        mSuperPlayerView.setPlayerViewCallback(this);

        videoSheetMusicAdapter = new VideoSheetMusicAdapter(this, sheetMusicBeanList);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
        rv_data.setLayoutManager(lin);
        rv_data.setAdapter(videoSheetMusicAdapter);
        videoSheetMusicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!sheetMusicBeanList.get(position).getIsUnlock().equals("1") && !sheetMusicBeanList.get(position).getSilverBean().equals("0")) {
                    PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                            "需支付" + sheetMusicBeanList.get(position).getSilverBean() + "银豆解锁", "确认", "取消");
                    promptDialog.show();
                    promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                        @Override
                        public void insure() {
                            sdVideoHomePresenter.buypiaonscore(sheetMusicBeanList.get(position).getId());
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                } else {


                    SheetMusicDetailActivity.start(mContext, sheetMusicBeanList.get(position).getId());
                }
            }
        });


        sdHomeVideoAdapter = new SdHomeVideoAdapter(this, videoModelLists,"0");
        LinearLayoutManager lin1 = new LinearLayoutManager(this);
        lin1.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_hjdata.setLayoutManager(lin1);
        rv_hjdata.setAdapter(sdHomeVideoAdapter);
        sdHomeVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                videoId = videoModelLists.get(position).getId();
                sdVideoHomePresenter.getVideodetail(videoModelLists.get(position).getId());
            }
        });

    }

    @OnClick({R.id.iv_black, R.id.ll_js, R.id.tv_collect, R.id.tv_gz, R.id.iv_teacherHeadImg, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_teacherHeadImg:
                if (null != videoModel) {
                    PersonalHomePageActivity.start(mContext, videoModel.getUserId());
                }
                break;
            case R.id.ll_js:
                if (null == videoModel) {
                    sdVideoHomePresenter.getVideodetail(videoId);
                    return;
                }
                String title = videoModel.getBeanType() == 0 ? "金豆解锁" : "银豆解锁";
                PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                        "需支付" + videoModel.getGoldenBean() + title, "确认", "取消");
                promptDialog.show();
                promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public void insure() {
                        sdVideoHomePresenter.unlockvideo(videoId);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.tv_collect:
                if (null != videoModel) {
                    sdVideoHomePresenter.setCollect(videoModel.getId(), videoModel.getIsCollect());
                }
                break;
            case R.id.tv_gz:
                if (null != videoModel) {
                    sdVideoHomePresenter.setFocus(videoModel.getUserId(), videoModel.getIsFocus());

                }
                break;
            case R.id.iv_share:
                if (null != videoModel) {
                    ShareDialog shareDialog = new ShareDialog(mContext, 1, videoModel.getId(), "0");
                    shareDialog.show();
                    shareDialog.setShareListener(new ShareDialog.OnShareListener() {
                        @Override
                        public void insure(int shareType, ShareInfoBean bean) {
                            ShareUtils.share(SdVideoDetailActivity.this, shareType, bean);
                        }
                    });
                }
                break;

        }
    }


    /**
     * 显示视频详情内容
     *
     * @param videoModel
     */
    private void initVideoDetail(VideoModel videoModel) {
        tv_videoTitle.setText(videoModel.title);
        tv_look.setText(videoModel.getPlayCount());
        tv_teacherName.setText(videoModel.getTeacherName());
        if (videoModel.getBeanType() == 0) {
            iv_goldenBean.setBackgroundResource(R.mipmap.ic_jdz);
            tv_goldenBean.setText(videoModel.getGoldenBean() + " 金豆解锁视频");
        } else {
            iv_goldenBean.setBackgroundResource(R.mipmap.ic_ydz);
            tv_goldenBean.setText(videoModel.getGoldenBean() + " 银豆解锁视频");
        }

        tv_collect.setText(videoModel.getCollectCount() + "");

        if (videoModel.getUserId().equals(SPUtils.getString(Parameter.USER_ID))) {
            tv_gz.setVisibility(View.GONE);
        } else {
            tv_gz.setVisibility(View.VISIBLE);
        }

        if ("0".equals(videoModel.getIsFocus())) {
            tv_gz.setText("+ 关注");
            tv_gz.setBackgroundResource(R.mipmap.ic_home_yy);
        } else {
            tv_gz.setText("已关注");
            tv_gz.setBackgroundResource(R.mipmap.ic_home_yyy);
        }


        if ("0".equals(videoModel.getIsCollect())) {
            setdrawableTop(R.mipmap.ic_comment_sc);
        } else {
            setdrawableTop(R.mipmap.ic_comment_sc_pre);
        }

        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, videoModel.getTeacherHeadImg(), iv_teacherHeadImg);
        if (videoModel.getVideoType().equals("1")) {
            tv_hj.setVisibility(View.VISIBLE);
        } else {
            tv_hj.setVisibility(View.GONE);
        }

        if (videoModel.getIsUnLock().equals("0")) {
            VipWatchModel vipWatchModel = new VipWatchModel("可免费观看前" + videoModel.getLookTime() + "秒", videoModel.getLookTime());
            videoModel.vipWatchModel = vipWatchModel;
            ll_js.setVisibility(View.VISIBLE);
        } else {
            ll_js.setVisibility(View.GONE);
        }


        playVideoModel(videoModel);
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
        rect.width = 0;
        rect.height = 0;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        //需要修改为自己的时移域名
        prefs.playShiftDomain = "liteavapp.timeshift.qcloud.com";
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
    public void onStartFullScreenPlay() {
        rl_bar.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        ll_js.setVisibility(View.GONE);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.black)
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色
                .navigationBarColor(R.color.black)
                .init();
    }

    @Override
    public void onStopFullScreenPlay() {
        setWhiteBar();
        scrollView.setVisibility(View.VISIBLE);
        rl_bar.setVisibility(View.VISIBLE);

        if (null != videoModel) {
            if (videoModel.getIsUnLock().equals("0")) {
                ll_js.setVisibility(View.VISIBLE);
            } else {
                ll_js.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClickHandleVip() {
        if (null == videoModel) {
            sdVideoHomePresenter.getVideodetail(videoId);
            return;
        }
        String title = videoModel.getBeanType() == 0 ? "金豆解锁" : "银豆解锁";
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                "需支付" + videoModel.getGoldenBean() + title, "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                sdVideoHomePresenter.unlockvideo(videoId);
            }

            @Override
            public void cancel() {

            }
        });
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

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList, int pages) {

    }

    @Override
    public void onGetZhVideoListSuccess(List<VideoModel> videoModelList) {

    }

    @Override
    public void onGetVideoDetailSuccess(VideoModel videoModel, List<VideoModel> videoModelList, List<SheetMusicBean> sheetMusicBeans) {
        if (null != videoModel) {
            this.videoModel = videoModel;
            initVideoDetail(videoModel);
        }

        if (null != videoModelList && videoModelList.size() > 0) {
            this.videoModelLists.clear();
            this.videoModelLists.addAll(videoModelList);
            sdHomeVideoAdapter.notifyDataSetChanged();
            ll_hj.setVisibility(View.VISIBLE);
        } else {
            ll_hj.setVisibility(View.GONE);
        }

        if (null != sheetMusicBeans && sheetMusicBeans.size() > 0) {
            ll_sheetMusic.setVisibility(View.VISIBLE);
            this.sheetMusicBeanList.clear();
            this.sheetMusicBeanList.addAll(sheetMusicBeans);
            videoSheetMusicAdapter.notifyDataSetChanged();
        } else {
            ll_sheetMusic.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUnlockVideoSuccess() {
        ToastUtil.toastLongMessage("解锁成功");
        sdVideoHomePresenter.getVideodetail(videoId);
    }

    @Override
    public void onFocusSuccess(String isFocus) {
        videoModel.setIsFocus(isFocus);
        if ("0".equals(isFocus)) {
            tv_gz.setText("+ 关注");
            tv_gz.setBackgroundResource(R.mipmap.ic_home_yy);
        } else {
            tv_gz.setText("已关注");
            tv_gz.setBackgroundResource(R.mipmap.ic_home_yyy);
        }
    }

    @Override
    public void onCollectSuccess(String isCollect) {
        videoModel.setIsCollect(isCollect);
        if ("0".equals(videoModel.getIsCollect())) {
            setdrawableTop(R.mipmap.ic_comment_sc);
            videoModel.setCollectCount(videoModel.getCollectCount() - 1);
            tv_collect.setText(videoModel.getCollectCount() + "");

        } else {
            setdrawableTop(R.mipmap.ic_comment_sc_pre);
            videoModel.setCollectCount(videoModel.getCollectCount() + 1);
            tv_collect.setText(videoModel.getCollectCount() + "");
        }
    }

    @Override
    public void onBuypiaonscoreSuccess() {
        ToastUtil.toastLongMessage("解锁成功");
        sdVideoHomePresenter.getVideodetail(videoId);
    }

    private void setdrawableTop(int id) {
        Drawable drawable = getResources().getDrawable(id);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        tv_collect.setCompoundDrawables(drawable, null, null, null);
    }


    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
