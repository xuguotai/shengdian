package com.tryine.sdgq.common.home.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.home.activity.AllCourseActivity;
import com.tryine.sdgq.common.home.activity.BargainHomeActivity;
import com.tryine.sdgq.common.home.activity.BargainOrderDetailActivity;
import com.tryine.sdgq.common.home.activity.BuyCourseHomeActivity;
import com.tryine.sdgq.common.home.activity.CampusListActivity;
import com.tryine.sdgq.common.home.activity.H5WebViewJsActivity;
import com.tryine.sdgq.common.home.activity.HdActivity;
import com.tryine.sdgq.common.home.activity.MineCourseHomeActivity;
import com.tryine.sdgq.common.home.activity.NoticeDetailActivity;
import com.tryine.sdgq.common.home.activity.NoticeListActivity;
import com.tryine.sdgq.common.home.activity.SdSdHomeVideoHomeActivity;
import com.tryine.sdgq.common.home.activity.SdSheetMusicListActivity;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.activity.TeacherDetailHomeActivity;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
import com.tryine.sdgq.common.home.adapter.HomeMenuAdapter;
import com.tryine.sdgq.common.home.adapter.HomeTeachAdapter;
import com.tryine.sdgq.common.home.adapter.HomeTeachGridChildAdapter;
import com.tryine.sdgq.common.home.bean.AnnouncementBean;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.presenter.HomePresenter;
import com.tryine.sdgq.common.home.view.HomeView;
import com.tryine.sdgq.common.live.activity.LiveListActivity;
import com.tryine.sdgq.common.live.activity.push.LivePlayerMainActivity;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.tencent.liteav.basic.GenerateTestUserSig;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModel;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModelManager;
import com.tryine.sdgq.common.mine.activity.MessageListActivity;
import com.tryine.sdgq.common.mine.adapter.DateListAdapter;
import com.tryine.sdgq.common.mine.bean.DateBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.AdvertisingJumpUtils;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.MyNestedScrollView;
import com.tryine.sdgq.view.NoticeView;
import com.tryine.sdgq.view.banner.BannerHomeLiveViewPager;
import com.tryine.sdgq.view.banner.BannerHomeVideoViewPager;
import com.tryine.sdgq.view.banner.BannerViewPager;
import com.tryine.sdgq.view.dialog.BuyBannerLiveCourseDialog;
import com.tryine.sdgq.view.dialog.CampusDialog;
import com.tryine.sdgq.view.dialog.HzDialog;
import com.tryine.sdgq.view.dialog.PromptDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeFragment extends BaseFragment implements HomeView {

    @BindView(R.id.rl_top)
    LinearLayout rl_top;
    @BindView(R.id.nestedScrollView)
    MyNestedScrollView nestedScrollView;

    @BindView(R.id.banner)
    BannerViewPager bannerView;
    @BindView(R.id.bannervideo)
    BannerHomeVideoViewPager bannervideo;
    @BindView(R.id.ll_live)
    LinearLayout ll_live;
    @BindView(R.id.banner_live)
    BannerHomeLiveViewPager bannerViewLive;
    @BindView(R.id.rl_live)
    RelativeLayout rl_live;
    @BindView(R.id.img_live)
    ImageView img_live;
    @BindView(R.id.tv_liveStatus)
    TextView tv_liveStatus;

    @BindView(R.id.nv_notice)
    NoticeView nv_notice;

    @BindView(R.id.tv_liveName)
    TextView tv_liveName;
    @BindView(R.id.tv_liveTime)
    TextView tv_liveTime;


    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;
    List<HomeMenuBean> homeMenuBeanList = new ArrayList<>();
    HomeMenuAdapter homeMenuAdapter;

    @BindView(R.id.rv_teachData)
    RecyclerView rv_teachData;
    List<CourseBean> courseBeanList = new ArrayList<>();
    HomeTeachAdapter homeTeachAdapter;

    @BindView(R.id.tv_qhxq)
    TextView tv_qhxq;

    @BindView(R.id.ll_kj)
    LinearLayout ll_kj;
    @BindView(R.id.rv_bargainData)
    RecyclerView rv_bargainData;
    List<BargainBean> bargainBeanLists = new ArrayList<>();
    HomeBargainAdapter homeBargainAdapter;

    boolean ceilingType = false;
    int rlheadHeight = 0;

    HomePresenter homePresenter;
    List<CampusBean> campusBeanLists = new ArrayList<>(); //校区
    CampusBean selectCampusBean;//选中的校区

    @BindView(R.id.rv_dete)
    RecyclerView rv_dete;
    @BindView(R.id.rv_coursedate)
    RecyclerView rv_coursedate;
    @BindView(R.id.ll_notdata)
    LinearLayout ll_notdata;
    @BindView(R.id.tv_notdata)
    TextView tv_notdata;
    List<CourseTimeBean> courseTimeBeanLists = new ArrayList<>();

    String startDate;

    int selectPosition = 0;

    UserBean userBean;


    @Override
    public int getlayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.transparent).
                statusBarDarkFont(false).
                navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();
        rl_top.setAlpha(0);
        startDate = DateTimeHelper.formatToString(new Date(), "yyyy-MM-dd");
        homePresenter = new HomePresenter(mContext);
        homePresenter.attachView(this);
        homePresenter.getBannerList();
        homePresenter.getFicationList();
        homePresenter.getrecentlylist();
        homePresenter.getAnnouncement(1);
        homePresenter.userdetail();
        homePresenter.getHomeLive();
        homePresenter.getHomeKjList();
        homePresenter.getHomeVideo();
        homePresenter.invitefriends();

        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);

        initViews();
        initCourseViews();

    }


    @OnClick({R.id.iv_notice, R.id.tv_qbkj, R.id.ll_xq, R.id.tv_allcourse, R.id.tv_morelive, R.id.nv_notice, R.id.tv_mvideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notice:
                NoticeListActivity.start(getContext());
                break;
            case R.id.tv_qbkj:
                BargainHomeActivity.start(getContext());
                break;
            case R.id.ll_xq:
                if (null != campusBeanLists) {
                    CampusDialog campusDialog = new CampusDialog(getActivity(), campusBeanLists);
                    campusDialog.show();
                    campusDialog.setOnItemClickListener(new CampusDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(CampusBean homeMenuBean) {
                            selectCampusBean = homeMenuBean;
                            tv_qhxq.setText(homeMenuBean.getName());
                            homePresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, selectPosition);
                        }
                    });
                }
                break;
            case R.id.tv_allcourse:
                if (null != selectCampusBean) {
                    AllCourseActivity.start(mContext, selectCampusBean.getId());
                }
                break;
            case R.id.tv_morelive:
                LiveListActivity.start(mContext);
                break;
            case R.id.nv_notice:
                NoticeDetailActivity.start(mContext, nv_notice.getAnnouncementBean());
                break;
            case R.id.tv_mvideo:
                SdSdHomeVideoHomeActivity.start(mContext);
                break;
        }
    }


    private void initBanner(List<BannerBean> bannerBeanList) {
        bannerView.initBanner(bannerBeanList, false)//关闭3D画廊效果
                .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addStartTimer(8)//自动轮播
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

    private void initVideoBanner(List<BannerBean> bannerBeanList) {
        bannervideo.initBanner(bannerBeanList, false)//关闭3D画廊效果
                .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addStartTimer(8)//自动轮播
                .finishConfig()//这句必须加
                .addPoint(bannerBeanList.size())//添加指示器
                .addBannerListener(new BannerHomeVideoViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                        SdVideoDetailActivity.start(mContext, bannerBeanList.get(position).getId());
                    }
                });
    }

    private void initViews() {
        bannerView.post(new Runnable() {
            @Override
            public void run() {
                rlheadHeight = bannerView.getHeight();
            }
        });

        nestedScrollView.setMyNestedScrollView(new MyNestedScrollView.OnMyScrollChanged() {
            @Override
            public void onScroll(int left, int top, int oldLeft, int oldTop) {
                if (top <= 0) {
                    rl_top.setAlpha(0);
                    ceilingType = false;
                    ImmersionBar.with(getActivity()).statusBarColor(R.color.transparent).
                            statusBarDarkFont(true).
                            navigationBarColor(R.color.white)
                            .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                            .navigationBarDarkIcon(true).init();
                } else if (top >= rlheadHeight) {
                    ceilingType = true;
                    rl_top.setAlpha(top);
                    ImmersionBar.with(getActivity())
                            .statusBarColor(R.color.white)
                            .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                            .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                            .navigationBarColor(R.color.white)
                            .navigationBarDarkIcon(true)
                            .init();
                }

            }
        });


        HomeMenuBean homeMenuBean = new HomeMenuBean();
        homeMenuBean.setTitle("课程购买");
        homeMenuBean.setImgId(R.mipmap.ic_home_menu_kck);
        homeMenuBeanList.add(homeMenuBean);

        HomeMenuBean homeMenuBean1 = new HomeMenuBean();
        homeMenuBean1.setTitle("圣典琴谱");
        homeMenuBean1.setImgId(R.mipmap.ic_home_menu_qp);
        homeMenuBeanList.add(homeMenuBean1);

        HomeMenuBean homeMenuBean2 = new HomeMenuBean();
        homeMenuBean2.setTitle("圣典视频");
        homeMenuBean2.setImgId(R.mipmap.ic_home_menu_sp);
        homeMenuBeanList.add(homeMenuBean2);

        HomeMenuBean homeMenuBean3 = new HomeMenuBean();
        homeMenuBean3.setTitle("活动中心");
        homeMenuBean3.setImgId(R.mipmap.ic_home_menu_lw);
        homeMenuBeanList.add(homeMenuBean3);

        HomeMenuBean homeMenuBean4 = new HomeMenuBean();
        homeMenuBean4.setTitle("小游戏");
        homeMenuBean4.setImgId(R.mipmap.ic_home_menu_yx);
        homeMenuBeanList.add(homeMenuBean4);

        HomeMenuBean homeMenuBean5 = new HomeMenuBean();
        homeMenuBean5.setTitle("校区展示");
        homeMenuBean5.setImgId(R.mipmap.ic_home_menu_xq);
        homeMenuBeanList.add(homeMenuBean5);

        HomeMenuBean homeMenuBean6 = new HomeMenuBean();
        homeMenuBean6.setTitle("我的课程");
        homeMenuBean6.setImgId(R.mipmap.ic_home_menu_kc);
        homeMenuBeanList.add(homeMenuBean6);

        HomeMenuBean homeMenuBean7 = new HomeMenuBean();
        homeMenuBean7.setTitle("我的消息");
        homeMenuBean7.setImgId(R.mipmap.ic_home_menu_xx);
        homeMenuBeanList.add(homeMenuBean7);


        homeMenuAdapter = new HomeMenuAdapter(getContext(), homeMenuBeanList);
        rv_menu.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_menu.setAdapter(homeMenuAdapter);
        homeMenuAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    BuyCourseHomeActivity.start(getContext());
                } else if (position == 1) {
                    SdSheetMusicListActivity.start(getContext());
                } else if (position == 2) {
                    SdSdHomeVideoHomeActivity.start(getContext());
                } else if (position == 3) {
                    HdActivity.start(mContext);
                } else if (position == 4) {
                    H5WebViewJsActivity.start(mContext, "http://ht.2007shengdian.cn/#/game/tree?userId=" + SPUtils.getString(Parameter.USER_ID));
                } else if (position == 5) {
                    CampusListActivity.start(getContext());
                } else if (position == 6) {
                    MineCourseHomeActivity.start(getContext());
                } else if (position == 7) {
                    MessageListActivity.start(getContext());
                }


            }
        });


        List<DateBean> dateBeanList = DateTimeHelper.getDateList();
        DateListAdapter dateListAdapter = new DateListAdapter(getContext(), dateBeanList);
        rv_dete.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        rv_dete.setAdapter(dateListAdapter);
        dateListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startDate = dateBeanList.get(position).getDate();
                selectPosition = position;
                if (null != selectCampusBean) {
                    homePresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, selectPosition);
                }
                dateListAdapter.setselectedTabPosition(position);
            }
        });


        homeTeachAdapter = new HomeTeachAdapter(getContext(), courseBeanList);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
        rv_teachData.setLayoutManager(lin);
        rv_teachData.setAdapter(homeTeachAdapter);
        homeTeachAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TeacherDetailHomeActivity.start(mContext, courseBeanList.get(position).getTeacherId());
            }
        });

        homeBargainAdapter = new HomeBargainAdapter(getContext(), bargainBeanLists);
        LinearLayoutManager lin1 = new LinearLayoutManager(getContext());
        lin1.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_bargainData.setLayoutManager(lin1);
        rv_bargainData.setAdapter(homeBargainAdapter);
        homeBargainAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                homePresenter.saveorder(bargainBeanLists.get(position).getId(), bargainBeanLists.get(position).getType());
            }
        });


    }

    CommonAdapter homeTeachGridAdapter;

    private void initCourseViews() {

        homeTeachGridAdapter = new CommonAdapter(mContext, R.layout.item_home_teach_grid, courseTimeBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                CourseTimeBean courseTimeBean = (CourseTimeBean) o;
                holder.setText(R.id.tv_time, courseTimeBean.getTime());
                RecyclerView rv_teachData = holder.getView(R.id.rv_teachData);

                HomeTeachGridChildAdapter homeTeachGridChildAdapter = new HomeTeachGridChildAdapter(mContext, courseTimeBean.getPiratesTeacherVoList());
                LinearLayoutManager lin = new LinearLayoutManager(mContext);
                lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
                rv_teachData.setLayoutManager(lin);
                rv_teachData.setAdapter(homeTeachGridChildAdapter);
                homeTeachGridChildAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        if (view.getId() == R.id.iv_head) {
                            TeacherDetailHomeActivity.start(mContext, courseTimeBean.getPiratesTeacherVoList().get(position).getTeacherId());
                        } else if (courseTimeBean.getPiratesTeacherVoList().get(position).getClassLayer() <= 0) {
                            if (courseTimeBean.getPiratesTeacherVoList().get(position).getCourseStatus().equals("0")) {
                                PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                                        "是否预约当前课程", "确认", "取消");
                                promptDialog.show();
                                promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                                    @Override
                                    public void insure() {
                                        homePresenter.appointment(courseTimeBean.getPiratesTeacherVoList().get(position).getId());
                                    }

                                    @Override
                                    public void cancel() {

                                    }
                                });
                            } else if (courseTimeBean.getPiratesTeacherVoList().get(position).getCourseStatus().equals("1")) {
                                ToastUtil.toastLongMessage("已预约");
                            } else if (courseTimeBean.getPiratesTeacherVoList().get(position).getCourseStatus().equals("2")) {
                                ToastUtil.toastLongMessage("停课");
                            } else if (courseTimeBean.getPiratesTeacherVoList().get(position).getCourseStatus().equals("3")) {
                                ToastUtil.toastLongMessage("完成");
                            } else if (courseTimeBean.getPiratesTeacherVoList().get(position).getCourseStatus().equals("4")) {
                                ToastUtil.toastLongMessage("课程已被预约");
                            }
                        }
                    }
                });

            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_coursedate.setLayoutManager(lin);
        rv_coursedate.setAdapter(homeTeachGridAdapter);

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
                    liveId = liveBeanList.get(0).getLiveId();
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
                                homePresenter.buyLivecourse(liveBeanList.get(0).getId(), userBean.getMobile(), ids);
                            }
                        });
                    }
                }
            });

        } else {
            if(!isShowBanner){
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
                                liveId = liveBeanList.get(position).getLiveId();
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
                                            homePresenter.buyLivecourse(liveBeanList.get(position).getId(), userBean.getMobile(), ids);

                                        }
                                    });
                                }
                            }
                        });
            }else{
                bannerViewLive.refreshBanner(liveBeanList);
            }


        }


    }


    int showStatus = 2;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            homePresenter.getRefreshHomeLive();
            homePresenter.getAnnouncement(1);
            homePresenter.getHomeLive();
            homePresenter.getHomeKjList();
            homePresenter.invitefriends();

            showStatus = 2;
            if (ceilingType) {
                ImmersionBar.with(getActivity())
                        .statusBarColor(R.color.white)
                        .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(false)
                        .navigationBarDarkIcon(true)
                        .init();
            } else {
                ImmersionBar.with(this)
                        .statusBarColor(R.color.transparent).
                        statusBarDarkFont(true).
                        navigationBarColor(R.color.white)
                        .navigationBarDarkIcon(true)
                        .fitsSystemWindows(false)
                        .init();
            }
        } else {
            showStatus = 1;
        }
    }

    @Override
    public void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList) {
        if (null != bannerBeanList && bannerBeanList.size() > 0) {
            initBanner(bannerBeanList);
        }
    }

    @Override
    public void onGetVideoListSuccess(List<BannerBean> bannerBeanList) {
        if (null != bannerBeanList && bannerBeanList.size() > 0) {
            initVideoBanner(bannerBeanList);
        }
    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetBargainBeanListSuccess(List<BargainBean> bargainBeanList) {
        this.bargainBeanLists.clear();
        this.bargainBeanLists.addAll(bargainBeanList);

        if (null != bargainBeanLists && bargainBeanLists.size() > 0 && !userBean.getType().equals("3")) {
            ll_kj.setVisibility(View.VISIBLE);
            homeBargainAdapter.notifyDataSetChanged();
        } else {
            ll_kj.setVisibility(View.GONE);
        }

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseTimeBean> courseBeanLists) {
        this.courseTimeBeanLists.clear();
        this.courseTimeBeanLists.addAll(courseBeanLists);
        if (null != courseTimeBeanLists && courseTimeBeanLists.size() > 0) {
            rv_coursedate.setVisibility(View.VISIBLE);
            ll_notdata.setVisibility(View.GONE);
        } else {
            rv_coursedate.setVisibility(View.GONE);
            ll_notdata.setVisibility(View.VISIBLE);
        }
        homeTeachGridAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetCourseTimeBeanListSuccess(List<CourseTimeBean> courseTimeBeanList, String selectDate, String sysDate, int position) {
        if (position == 4) {
            if (DateTimeHelper.isShowCourse(startDate, sysDate, userBean)) {
                this.courseTimeBeanLists.clear();
                this.courseTimeBeanLists.addAll(courseTimeBeanList);
                if (null != courseTimeBeanLists && courseTimeBeanLists.size() > 0) {
                    rv_coursedate.setVisibility(View.VISIBLE);
                    ll_notdata.setVisibility(View.GONE);
                } else {
                    rv_coursedate.setVisibility(View.GONE);
                    ll_notdata.setVisibility(View.VISIBLE);
                    tv_notdata.setText("没有可预约的课程");
                }
                homeTeachGridAdapter.notifyDataSetChanged();
            } else {
                this.courseTimeBeanLists.clear();
                rv_coursedate.setVisibility(View.GONE);
                ll_notdata.setVisibility(View.VISIBLE);
                tv_notdata.setText(userBean.getIsVip() == 1 ? "21:05 开始预约" : "21:30 开始预约");
                homeTeachGridAdapter.notifyDataSetChanged();
            }

        } else {
            this.courseTimeBeanLists.clear();
            this.courseTimeBeanLists.addAll(courseTimeBeanList);
            if (null != courseTimeBeanLists && courseTimeBeanLists.size() > 0) {
                rv_coursedate.setVisibility(View.VISIBLE);
                ll_notdata.setVisibility(View.GONE);
            } else {
                rv_coursedate.setVisibility(View.GONE);
                ll_notdata.setVisibility(View.VISIBLE);
                tv_notdata.setText("没有可预约的课程");
            }
            homeTeachGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetJqCourseBeanListSuccess(List<CourseBean> courseBeanList) {
        this.courseBeanList.clear();
        this.courseBeanList.addAll(courseBeanList);
        homeTeachAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetLiveBeanListSuccess(List<LiveBean> liveBeanLists) {
        this.liveBeanList.clear();
        this.liveBeanList.addAll(liveBeanLists);
        try {
            if (null != liveBeanList && liveBeanList.size() > 0) {
                ll_live.setVisibility(View.VISIBLE);
                initLiveBanner();
            } else {
                ll_live.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onGetRefreshLiveBeanListSuccess(List<LiveBean> liveBeanList) {
        bannerViewLive.refreshBanner(liveBeanList);
    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {
        this.campusBeanLists = campusBeanList;
        if (TextUtils.isEmpty(SPUtils.getString("receiptId"))) {
            //默认取第一个校区
            if (null != campusBeanLists && campusBeanLists.size() > 0) {
                selectCampusBean = campusBeanLists.get(0);
                tv_qhxq.setText(selectCampusBean.getName());
            }
        } else {
            for (CampusBean campusBean : campusBeanList) {
                if (campusBean.getId().equals(SPUtils.getString("receiptId"))) {
                    this.selectCampusBean = campusBean;
                    tv_qhxq.setText(selectCampusBean.getName());
                }
            }
        }
        homePresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, selectPosition);

    }

    @Override
    public void onGetAnnouncementBeanListSuccess(List<AnnouncementBean> announcementBeanList, int pages) {
        nv_notice.start(announcementBeanList);
    }

    @Override
    public void onAppointmentSuccess() {
        ToastUtil.toastLongMessage("预约成功");
        homePresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, selectPosition);
    }

    @Override
    public void onSaveorderSuccess(String orderId) {
        BargainOrderDetailActivity.start(mContext, orderId);
    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetInvitefriendsSuccess(String title, String id, String avatar) {
        HzDialog hzDialog = new HzDialog(mContext, title, avatar);
        hzDialog.show();
        hzDialog.setOnItemClickListener(new HzDialog.OnItemClickListener() {
            @Override
            public void insure() {
                homePresenter.friendconsent(id, 1);
                H5WebViewJsActivity.start(mContext, "http://ht.2007shengdian.cn/#/game/tree?userId=" + SPUtils.getString(Parameter.USER_ID));
            }

            @Override
            public void cancel() {
                homePresenter.friendconsent(id, 2);
            }
        });

    }


    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {
        SPUtils.saveString(Parameter.USER_INFO, new Gson().toJson(userBean));
        final UserModel userModel = new UserModel();
        userModel.userId = userBean.getId();
        userModel.userName = userBean.getUserName();
        userModel.userAvatar = userBean.getAvatar();
        userModel.userSig = GenerateTestUserSig.genTestUserSig(userBean.getId());
        final UserModelManager manager = UserModelManager.getInstance();
        manager.setUserModel(userModel);
    }

    @Override
    public void onBuyCourseSuccess() {
        LivePlayerMainActivity.start(mContext, liveId);
        ToastUtil.toastLongMessage("购买成功");
        homePresenter.getHomeLive();
    }

}
