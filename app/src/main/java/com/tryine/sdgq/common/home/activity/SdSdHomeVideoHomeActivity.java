package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.presenter.BannerPresenter;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.home.adapter.SdVideoHomeMenuAdapter;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SdVideoHomePresenter;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.util.AdvertisingJumpUtils;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerVideoViewPager;
import com.tryine.sdgq.view.banner.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圣典视频首页
 *
 * @author: zhangshuaijun
 * @time: 2021-11-25 10:35
 */
public class SdSdHomeVideoHomeActivity extends BaseActivity implements SdHomeVideoView, BannerView {

    @BindView(R.id.banner)
    BannerViewPager bannerView;
    @BindView(R.id.banner_live)
    BannerVideoViewPager bannerViewLive;

    @BindView(R.id.tv_videoTitle)
    TextView tv_videoTitle;

    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;
    List<HomeMenuBean> homeMenuBeanList = new ArrayList<>();
    SdVideoHomeMenuAdapter sdVideoHomeMenuAdapter;


    @BindView(R.id.rv_videoData)
    RecyclerView rv_videoData;
    List<VideoModel> zhVideoList = new ArrayList<>();
    SdHomeVideoAdapter sdHomeVideoAdapter;

    BannerPresenter bannerPresenter;
    SdVideoHomePresenter sdVideoHomePresenter;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SdSdHomeVideoHomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_sdhome;
    }

    @Override
    protected void init() {
        setWhiteBar();
        bannerPresenter = new BannerPresenter(mContext);
        bannerPresenter.attachView(this);
        bannerPresenter.getBannerList(1);

        sdVideoHomePresenter = new SdVideoHomePresenter(this);
        sdVideoHomePresenter.attachView(this);
        sdVideoHomePresenter.getVideoTypeList();
        sdVideoHomePresenter.getVideoList();
        sdVideoHomePresenter.getZhVideoList();

        initViews();

    }

    @OnClick({R.id.iv_black, R.id.tv_more,R.id.tv_more1,R.id.iv_mfVideo,R.id.iv_ffVideo,R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_more:
                SdVideoListActivity.start(mContext,0);
                break;
            case R.id.tv_more1:
                SdVideoListActivity.start(mContext,0);
                break;
            case R.id.iv_mfVideo://免费视频
                SdVideoListActivity.start(mContext,0);
                break;
            case R.id.iv_ffVideo://付费视频
                SdVideoListActivity.start(mContext,1);
                break;
            case R.id.iv_search://搜索视频
                VideoSearchActivity.start(mContext);
                break;
        }
    }


    private void initBanner(List<BannerBean> bannerBeanList) {
        bannerView.initBanner(bannerBeanList, false)//关闭3D画廊效果
                .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addStartTimer(bannerBeanList.size() > 1 ? 5 : 10000)//自动轮播5秒间隔
                .finishConfig()//这句必须加
                .addPoint(bannerBeanList.size())//添加指示器
                .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                        BannerBean bannerBean = bannerBeanList.get(position);
                        AdvertisingJumpUtils.advertisingJump(SdSdHomeVideoHomeActivity.this, bannerBean);
                    }
                });

    }

    private void initViews() {
        sdVideoHomeMenuAdapter = new SdVideoHomeMenuAdapter(this, homeMenuBeanList);
        rv_menu.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_menu.setAdapter(sdVideoHomeMenuAdapter);
        sdVideoHomeMenuAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SdVideoListActivity.start(mContext, position + 2);
            }
        });

        sdHomeVideoAdapter = new SdHomeVideoAdapter(this, zhVideoList,"0");
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_videoData.setLayoutManager(lin);
        rv_videoData.setAdapter(sdHomeVideoAdapter);
        sdHomeVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SdVideoDetailActivity.start(mContext,zhVideoList.get(position).getId());
            }
        });
    }


    private void initRmBanner(List<VideoModel> videoModelList) {

        tv_videoTitle.setText(videoModelList.get(0).title);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        float scale = getResources().getDisplayMetrics().density;
        int width = dm.widthPixels - (int) (40 * scale + 0.5f);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bannerViewLive.getLayoutParams();
        lp.width = dm.widthPixels;
        lp.height = width / 2;
        bannerViewLive.setLayoutParams(lp);

        bannerViewLive.initBanner(videoModelList, true)//关闭3D画廊效果
                .addPageMargin(-10, 50)//参数1page之间的间距,参数2中间item距离边界的间距
                .addStartTimer(videoModelList.size() > 1 ? 5 : 10000)//自动轮播5秒间隔
                .finishConfig()//这句必须加
                .addRoundCorners(UIUtils.dip2px(5))//圆角
                .addBannerListener(new BannerVideoViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                        SdVideoDetailActivity.start(mContext,videoModelList.get(position).getId());
                    }
                }).addOnPageSelected(new BannerVideoViewPager.OnPageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                tv_videoTitle.setText(videoModelList.get(position).title);
            }
        });

    }


    @Override
    public void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList) {
        if (null != bannerBeanList && bannerBeanList.size() > 0) {
            initBanner(bannerBeanList);
        }
    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanLists) {
        this.homeMenuBeanList.clear();
        this.homeMenuBeanList.addAll(homeMenuBeanLists);
        sdVideoHomeMenuAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList) {
        if (null != videoModelList && videoModelList.size() > 0) {
            initRmBanner(videoModelList);
        }
    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList, int pages) {

    }

    @Override
    public void onGetZhVideoListSuccess(List<VideoModel> videoModelList) {
        this.zhVideoList.clear();
        this.zhVideoList.addAll(videoModelList);
        sdHomeVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetVideoDetailSuccess(VideoModel videoModel, List<VideoModel> videoModelList,List<SheetMusicBean> sheetMusicBeans) {

    }

    @Override
    public void onUnlockVideoSuccess() {

    }

    @Override
    public void onFocusSuccess(String isFocus) {

    }

    @Override
    public void onCollectSuccess(String isCollect) {

    }

    @Override
    public void onBuypiaonscoreSuccess() {

    }


    @Override
    public void onFailed(String message) {

    }
}
