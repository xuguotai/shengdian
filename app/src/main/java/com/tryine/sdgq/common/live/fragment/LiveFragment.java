package com.tryine.sdgq.common.live.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.presenter.BannerPresenter;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.activity.LiveCourseBuyDetailActivity;
import com.tryine.sdgq.common.live.activity.LiveCourseDetailActivity;
import com.tryine.sdgq.common.live.activity.LiveListActivity;
import com.tryine.sdgq.common.live.activity.LiveSearchActivity;
import com.tryine.sdgq.common.live.activity.LiveTypeListActivity;
import com.tryine.sdgq.common.live.activity.push.LivePlayerMainActivity;
import com.tryine.sdgq.common.live.adapter.LiveCourseAdapter;
import com.tryine.sdgq.common.live.adapter.LiveMenuAdapter;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.presenter.LiveHomePresenter;
import com.tryine.sdgq.common.live.view.LiveHomeView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.AdvertisingJumpUtils;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.banner.BannerHomeLiveViewPager;
import com.tryine.sdgq.view.banner.BannerViewPager;
import com.tryine.sdgq.view.dialog.BuyBannerLiveCourseDialog;
import com.tryine.sdgq.view.roundImageView.RoundImageView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 直播课
 *
 * @author：qujingfeng
 * @time：2020.08.05 11:14
 */


public class LiveFragment extends BaseFragment implements LiveHomeView, BannerView {

    @BindView(R.id.banner)
    BannerViewPager bannerView;

    @BindView(R.id.rv_liveMenu)
    RecyclerView rv_liveMenu;
    List<HomeMenuBean> LiveMenuBeanLists = new ArrayList<>();
    LiveMenuAdapter liveMenuAdapter;


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rv_liveCourse)
    RecyclerView rv_liveCourse;
    List<LiveBean> liveCourseList = new ArrayList<>();
    LiveCourseAdapter liveCourseAdapter;

    LiveHomePresenter liveHomePresenter;
    BannerPresenter bannerPresenter;

    @BindView(R.id.ll_live)
    LinearLayout ll_live;
    @BindView(R.id.rl_live)
    RelativeLayout rl_live;
    @BindView(R.id.banner_live)
    BannerHomeLiveViewPager bannerViewLive;
    @BindView(R.id.tv_liveName)
    TextView tv_liveName;
    @BindView(R.id.tv_liveTime)
    TextView tv_liveTime;
    @BindView(R.id.img_live)
    ImageView img_live;
    @BindView(R.id.tv_liveStatus)
    TextView tv_liveStatus;

    UserBean userBean;

    int pageNum = 1;


    @Override
    public int getlayoutId() {
        return R.layout.fragment_live;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWhiteBar();
        smartRefresh();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        liveHomePresenter = new LiveHomePresenter(mContext);
        liveHomePresenter.attachView(this);
        liveHomePresenter.getLiveMenuList();
//        liveHomePresenter.getLiveCourseList();
        liveHomePresenter.getLiveCourseList(pageNum);
        liveHomePresenter.getHomeLive();

        bannerPresenter = new BannerPresenter(mContext);
        bannerPresenter.attachView(this);
        bannerPresenter.getBannerList(2);


        initViews();
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
                        AdvertisingJumpUtils.advertisingJump(getActivity(), bannerBean);
                    }
                });
    }

    private void initViews() {

        liveMenuAdapter = new LiveMenuAdapter(getContext(), LiveMenuBeanLists);
        rv_liveMenu.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_liveMenu.setAdapter(liveMenuAdapter);
        liveMenuAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LiveTypeListActivity.start(mContext, position + 1);
            }
        });


        liveCourseAdapter = new LiveCourseAdapter(getContext(), liveCourseList);
        rv_liveCourse.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_liveCourse.setAdapter(liveCourseAdapter);
        liveCourseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LiveCourseDetailActivity.start(mContext, liveCourseList.get(position).getId());
            }
        });


    }


    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                liveHomePresenter.getLiveCourseList(pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                liveHomePresenter.getLiveCourseList(pageNum);
            }
        });
    }

    @OnClick({R.id.tv_more, R.id.tv_more1, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_more:
                LiveListActivity.start(mContext);
                break;
            case R.id.tv_more1:
                LiveTypeListActivity.start(mContext, 0);
                break;
            case R.id.iv_search:
                LiveSearchActivity.start(mContext);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {
        this.LiveMenuBeanLists.clear();
        this.LiveMenuBeanLists.addAll(homeMenuBeanList);
        liveMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetLiveBeanListSuccess(List<LiveBean> liveBeanLists) {
        this.liveBeanList.clear();
        this.liveBeanList.addAll(liveBeanLists);
        if (null != liveBeanList && liveBeanList.size() > 0) {
            ll_live.setVisibility(View.VISIBLE);
            initLiveBanner();
        } else {
            ll_live.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetLiveBeanListSuccess(List<LiveBean> liveBeanLists, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            liveCourseList.clear();
        }
        liveCourseList.addAll(liveBeanLists);
        if (liveCourseList.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rv_liveCourse.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rv_liveCourse.setVisibility(View.VISIBLE);
        }
        liveCourseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBuyCourseSuccess() {
        LivePlayerMainActivity.start(mContext, liveId);
        ToastUtil.toastLongMessage("购买成功");
        liveHomePresenter.getHomeLive();
    }

    @Override
    public void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList) {
        if (null != bannerBeanList && bannerBeanList.size() > 0) {
            initBanner(bannerBeanList);
        }
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            liveHomePresenter.getHomeLive();
            setWhiteBar();
        }
    }

    boolean isShowBanner = false;
    List<LiveBean> liveBeanList = new ArrayList<>();
    String liveId;

    /**
     * 显示直播Banner
     */
    private void initLiveBanner() {
        tv_liveName.setText(liveBeanList.get(0).getName());
        tv_liveTime.setText(liveBeanList.get(0).getStartTimeDesc());

        if (null != liveBeanList && liveBeanList.size() == 1) { //单个直播
            rl_live.setVisibility(View.VISIBLE);
            bannerViewLive.setVisibility(View.GONE);

            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, liveBeanList.get(0).getImgUrl()
                    , img_live);
            if (liveBeanList.get(0).getLiveStatus().equals("1")) {
                tv_liveStatus.setVisibility(View.VISIBLE);
            } else {
                tv_liveStatus.setVisibility(View.GONE);
            }

            img_live.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    liveId =  liveBeanList.get(0).getLiveId();
                    if (liveBeanList.get(0).getIsBuy().equals("1")) {
                        LivePlayerMainActivity.start(mContext, liveBeanList.get(0).getLiveId());
                    } else {
                        BuyBannerLiveCourseDialog buyBannerLiveCourseDialog = new BuyBannerLiveCourseDialog(mContext, liveBeanList.get(0));
                        buyBannerLiveCourseDialog.show();
                        buyBannerLiveCourseDialog.setOnItemClickListener(new BuyBannerLiveCourseDialog.OnItemClickListener() {
                            @Override
                            public void insure() {
                                JSONArray ids = new JSONArray();
                                ids.put(liveBeanList.get(0).getCourseDetailId());
                                liveHomePresenter.buyLivecourse(liveBeanList.get(0).getId(), userBean.getMobile(), ids);

                            }
                        });
                    }
                }
            });

        } else {
            if (!isShowBanner) {
                isShowBanner = true;
                rl_live.setVisibility(View.GONE);
                bannerViewLive.setVisibility(View.VISIBLE);

                bannerViewLive.initBanner(liveBeanList, true)//关闭3D画廊效果
                        .addPageMargin(-10, 30)//参数1page之间的间距,参数2中间item距离边界的间距
                        .addStartTimer(liveBeanList.size() > 1 ? 10 : 10000)//自动轮播5秒间隔
                        .finishConfig()//这句必须加
                        .addRoundCorners(UIUtils.dip2px(5))//圆角
                        .addOnPageSelected(new BannerHomeLiveViewPager.OnPageSelectedListener() {
                            @Override
                            public void onPageSelected(int position) {
                                tv_liveName.setText(liveBeanList.get(position).getName());
                                tv_liveTime.setText(liveBeanList.get(position).getStartTimeDesc());
                            }
                        })
                        .addBannerListener(new BannerHomeLiveViewPager.OnClickBannerListener() {
                            @Override
                            public void onBannerClick(int position) {
                                liveId =  liveBeanList.get(position).getLiveId();
                                if (liveBeanList.get(position).getIsBuy().equals("1")) {
                                    LivePlayerMainActivity.start(mContext, liveBeanList.get(position).getLiveId());
                                } else {
                                    BuyBannerLiveCourseDialog buyBannerLiveCourseDialog = new BuyBannerLiveCourseDialog(mContext, liveBeanList.get(position));
                                    buyBannerLiveCourseDialog.show();
                                    buyBannerLiveCourseDialog.setOnItemClickListener(new BuyBannerLiveCourseDialog.OnItemClickListener() {
                                        @Override
                                        public void insure() {
                                            JSONArray ids = new JSONArray();
                                            ids.put(liveBeanList.get(position).getCourseDetailId());
                                            liveHomePresenter.buyLivecourse(liveBeanList.get(position).getId(), userBean.getMobile(), ids);

                                        }
                                    });
                                }
                            }
                        });
            } else {
                bannerViewLive.refreshBanner(liveBeanList);
            }
        }


    }
}
