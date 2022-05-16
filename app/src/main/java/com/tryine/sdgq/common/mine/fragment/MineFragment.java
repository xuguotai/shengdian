package com.tryine.sdgq.common.mine.fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
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
import com.tryine.sdgq.common.home.activity.AddCourseDataActivity;
import com.tryine.sdgq.common.home.adapter.HomeTeachGridChildAdapter1;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.activity.IllustrateActivity;
import com.tryine.sdgq.common.live.activity.push.CameraPushMainActivity;
import com.tryine.sdgq.common.live.tencent.liteav.basic.GenerateTestUserSig;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModel;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModelManager;
import com.tryine.sdgq.common.mall.activity.OrderListHomeActivity;
import com.tryine.sdgq.common.mine.activity.AboutActivity;
import com.tryine.sdgq.common.mine.activity.BargainOrderListHomeActivity;
import com.tryine.sdgq.common.mine.activity.CollectListHomeActivity;
import com.tryine.sdgq.common.mine.activity.CreateTeacherActivity;
import com.tryine.sdgq.common.mine.activity.FeedbackActivity;
import com.tryine.sdgq.common.mine.activity.InviteFriendsActivity;
import com.tryine.sdgq.common.mine.activity.MessageListActivity;
import com.tryine.sdgq.common.mine.activity.MineCircleHomeActivity;
import com.tryine.sdgq.common.mine.activity.MineCourseListActivity;
import com.tryine.sdgq.common.mine.activity.MineKtzlHomeActivity;
import com.tryine.sdgq.common.mine.activity.MyRewardListActivity;
import com.tryine.sdgq.common.live.activity.OpenLiveRoomActivity;
import com.tryine.sdgq.common.mine.activity.PayRecordListActivity;
import com.tryine.sdgq.common.mine.activity.SbListHomeActivity;
import com.tryine.sdgq.common.mine.activity.TaskHomeActivity;
import com.tryine.sdgq.common.mine.activity.TyCardHomeActivity;
import com.tryine.sdgq.common.mine.activity.UploadVideoActivity;
import com.tryine.sdgq.common.mine.adapter.DateListAdapter;
import com.tryine.sdgq.common.mine.adapter.MineMenuAdapter;
import com.tryine.sdgq.common.mine.bean.DateBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.bean.KtzlBean;
import com.tryine.sdgq.common.mine.bean.TaskBean;
import com.tryine.sdgq.common.mine.presenter.MinePresenter;
import com.tryine.sdgq.common.mine.presenter.TaskPresenter;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.mine.view.TaskView;
import com.tryine.sdgq.common.mine.wallet.MyWalletActivity;
import com.tryine.sdgq.common.mine.activity.PersonalActivity;
import com.tryine.sdgq.common.mine.activity.SettingActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.event.IMMessageCountEvent;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.PermissionsUtils;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.GradeTextView;
import com.tryine.sdgq.view.MyNestedScrollView;
import com.tryine.sdgq.view.TaskScrollView;
import com.tryine.sdgq.view.dialog.KefuDialog;
import com.tryine.sdgq.view.dialog.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 *
 * @author：qujingfeng
 * @time：2020.08.05 11:14
 */


public class MineFragment extends BaseFragment implements TaskView, MineView {

    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.nestedScrollView)
    MyNestedScrollView nestedScrollView;

    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    @BindView(R.id.tv_top_name)
    TextView tv_top_name;  //顶部姓名
    @BindView(R.id.iv_top_head)
    CircleImageView iv_top_head;  //顶部头像
    @BindView(R.id.iv_top_msg)
    ImageView iv_top_msg;


    @BindView(R.id.iv_head)
    CircleImageView iv_head;  //头像
    @BindView(R.id.iv_vip)
    ImageView iv_vip;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_msg)
    ImageView iv_msg;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @BindView(R.id.tv_dj)
    GradeTextView tv_dj;  //等级
    @BindView(R.id.tv_zr)
    TextView tv_zr;  //实名认证


    @BindView(R.id.nv_notice)
    TaskScrollView nv_notice;
    @BindView(R.id.tv_day_count)
    TextView tv_day_count; //签到天数

    @BindView(R.id.ll_scsp)
    LinearLayout ll_scsp; //上传视频
    @BindView(R.id.ll_kqzb)
    LinearLayout ll_kqzb; //开启直播
    @BindView(R.id.ll_zl)
    LinearLayout ll_zl; //填课堂资料


    @BindView(R.id.calendarLayout)
    LinearLayout calendarLayout;

    @BindView(R.id.rv_menu)
    RecyclerView rv_menu;
    List<HomeMenuBean> homeMenuBeanList = new ArrayList<>();
    MineMenuAdapter mineMenuAdapter;

    TaskPresenter taskPresenter;
    MinePresenter minePresenter;

    @BindView(R.id.rv_dete)
    RecyclerView rv_dete;
    @BindView(R.id.rv_coursedate)
    RecyclerView rv_coursedate;
    @BindView(R.id.ll_notdata)
    LinearLayout ll_notdata;

    DateListAdapter dateListAdapter;

    List<CourseTimeBean> courseTimeBeanLists = new ArrayList<>();

    boolean ceilingType = true;
    int rlheadHeight = 0;
    int showStatus = 2;

    String rewardDesc;

    UserBean userBean;

    String startDate;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.transparent).
                statusBarDarkFont(false).
                navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();
        rl_top.setAlpha(0);
        initViews();
        initCourseViews();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(MineFragment.this);
        }
    }

    @Override
    public int getlayoutId() {
        return R.layout.fragment_mine;
    }


    private void initViews() {
        startDate = DateTimeHelper.formatToString(new Date(), "yyyy-MM-dd");

        taskPresenter = new TaskPresenter(mContext);
        taskPresenter.attachView(this);
        taskPresenter.getContinuesign();
        taskPresenter.getTaskList();

        minePresenter = new MinePresenter(mContext);
        minePresenter.attachView(this);
        minePresenter.userdetail();
        minePresenter.getTeacherList(startDate);
        minePresenter.getaboutinfo();

        rl_head.post(new Runnable() {
            @Override
            public void run() {
                rlheadHeight = rl_head.getHeight();
            }
        });

        nestedScrollView.setMyNestedScrollView(new MyNestedScrollView.OnMyScrollChanged() {
            @Override
            public void onScroll(int left, int top, int oldLeft, int oldTop) {
                if (top <= 0) {
                    rl_top.setAlpha(0);
                    ceilingType = true;
                    ImmersionBar.with(getActivity()).statusBarColor(R.color.transparent).
                            statusBarDarkFont(true).
                            navigationBarColor(R.color.white)
                            .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                            .navigationBarDarkIcon(true).init();
                } else if (top >= rlheadHeight) {
                    ceilingType = false;
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

        mineMenuAdapter = new MineMenuAdapter(getContext(), homeMenuBeanList);
        rv_menu.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_menu.setAdapter(mineMenuAdapter);
        mineMenuAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (homeMenuBeanList.get(position).getTitle().equals("我的体验卡")) {
                    TyCardHomeActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("我的收藏")) {
                    CollectListHomeActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("成为老师")) {
                    CreateTeacherActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("我的解锁")) {
                    SbListHomeActivity.start(mContext);
                } else if (homeMenuBeanList.get(position).getTitle().equals("我的打赏")) {
                    MyRewardListActivity.start(getContext(), "0");
                } else if (homeMenuBeanList.get(position).getTitle().equals("我的礼物")) {
                    MyRewardListActivity.start(getContext(), "1");
                } else if (homeMenuBeanList.get(position).getTitle().equals("系统设置")) {
                    SettingActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("支付记录")) {
                    PayRecordListActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("关于我们")) {
                    AboutActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("我的圈子")) {
                    MineCircleHomeActivity.start(mContext, SPUtils.getString(Parameter.USER_ID), "0");
                } else if (homeMenuBeanList.get(position).getTitle().equals("砍价订单")) {
                    BargainOrderListHomeActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("投诉建议")) {
                    FeedbackActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("我的课程")) {
                    MineCourseListActivity.start(getContext());
                } else if (homeMenuBeanList.get(position).getTitle().equals("联系客服")) {
                    KefuDialog kefuDialog = new KefuDialog(mContext);
                    kefuDialog.show();
                    kefuDialog.setOnItemClickListener(new KefuDialog.OnItemClickListener() {
                        @Override
                        public void confirm() {
                            callPhone("40028335678");
                        }
                    });
                }
            }
        });


        List<DateBean> dateBeanList = DateTimeHelper.getDateList();
        dateListAdapter = new DateListAdapter(getContext(), dateBeanList);
        rv_dete.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        rv_dete.setAdapter(dateListAdapter);
        dateListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startDate = dateBeanList.get(position).getDate();
                minePresenter.getTeacherList(startDate);
                dateListAdapter.setselectedTabPosition(position);
            }
        });


    }


    private void initUserInfo() {
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);

        tv_name.setText(userBean.getUserName());
        tv_top_name.setText(userBean.getUserName());
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, userBean.getAvatar(), iv_head);
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, userBean.getAvatar(), iv_top_head);

        tv_dj.setData(userBean.getLevel());

        if (userBean.getSex().equals("0")) {
            iv_sex.setBackgroundResource(R.mipmap.ic_sex_1);
        } else if (userBean.getSex().equals("1")) {
            iv_sex.setBackgroundResource(R.mipmap.ic_sex_2);
        }

        iv_vip.setVisibility(userBean.getIsVip() == 0 ? View.GONE : View.VISIBLE);

        tv_zr.setText(userBean.getIsCertification().equals("0") ? "未实名" : "已认证");

        homeMenuBeanList.clear();
        addHomeMenuBean("我的圈子", R.mipmap.ic_personal_wdqz);
        addHomeMenuBean("我的收藏", R.mipmap.ic_personal_wdsc);
        addHomeMenuBean("我的解锁", R.mipmap.ic_personal_qpsp);
        addHomeMenuBean("我的体验卡", R.mipmap.ic_personal_wdtyk);
        if (userBean.getType().equals("1")) {
            addHomeMenuBean("成为老师", R.mipmap.ic_personal_cwls);
        }

        if (userBean.getType().equals("3")) {
            ll_scsp.setVisibility(View.VISIBLE);
            ll_kqzb.setVisibility(View.VISIBLE);
            ll_zl.setVisibility(View.VISIBLE);
            calendarLayout.setVisibility(View.VISIBLE);
            addHomeMenuBean("我的课程", R.mipmap.ic_personal_wdkc);
        }

        addHomeMenuBean("砍价订单", R.mipmap.ic_personal_kjdd);
        addHomeMenuBean("我的打赏", R.mipmap.ic_personal_ds);
        addHomeMenuBean("我的礼物", R.mipmap.ic_personal_wdlw);
        addHomeMenuBean("支付记录", R.mipmap.ic_personal_zfjl);
        addHomeMenuBean("联系客服", R.mipmap.ic_personal_kf);
        addHomeMenuBean("关于我们", R.mipmap.ic_personal_gywm);
        addHomeMenuBean("投诉建议", R.mipmap.ic_personal_tsjy);
        addHomeMenuBean("系统设置", R.mipmap.ic_personal_xtsz);


        mineMenuAdapter.notifyDataSetChanged();

    }

    CommonAdapter homeTeachGridAdapter;

    private void initCourseViews() {

        homeTeachGridAdapter = new CommonAdapter(mContext, R.layout.item_home_teach_grid, courseTimeBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                CourseTimeBean courseTimeBean = (CourseTimeBean) o;
                holder.setText(R.id.tv_time, courseTimeBean.getTime());
                RecyclerView rv_teachData = holder.getView(R.id.rv_teachData);

                HomeTeachGridChildAdapter1 homeTeachGridChildAdapter = new HomeTeachGridChildAdapter1(mContext, courseTimeBean.getPiratesTeacherVoList());
                LinearLayoutManager lin = new LinearLayoutManager(mContext);
                lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
                rv_teachData.setLayoutManager(lin);
                rv_teachData.setAdapter(homeTeachGridChildAdapter);

            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_coursedate.setLayoutManager(lin);
        rv_coursedate.setAdapter(homeTeachGridAdapter);
    }

    @OnClick({R.id.iv_head, R.id.ll_wallet, R.id.ll_yqm, R.id.tv_dfk, R.id.tv_dzt, R.id.tv_dpj, R.id.tv_ywc, R.id.ll_updateVide, R.id.tv_goTask,
            R.id.ll_tzl, R.id.tv_live, R.id.tv_allorder, R.id.iv_msg, R.id.iv_top_msg,R.id.tv_dj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                PersonalActivity.start(getContext());
                break;
            case R.id.ll_wallet://我的钱包
                MyWalletActivity.start(getContext());
                break;
            case R.id.ll_yqm://我的邀请码
                InviteFriendsActivity.start(getContext());
                break;
            case R.id.tv_allorder://全部订单
                OrderListHomeActivity.start(getContext(), 0);
                break;
            case R.id.tv_dfk://待付款
                OrderListHomeActivity.start(getContext(), 1);
                break;
            case R.id.tv_ywc://已完成
                OrderListHomeActivity.start(getContext(), 4);
                break;
            case R.id.tv_dzt://待自提
                OrderListHomeActivity.start(getContext(), 2);
                break;
            case R.id.tv_dpj://待评价
                OrderListHomeActivity.start(getContext(), 3);
                break;
            case R.id.ll_updateVide://上传视频
                UploadVideoActivity.start(getContext());
                break;
            case R.id.tv_goTask://做任务
                TaskHomeActivity.start(getContext());
                break;
            case R.id.ll_tzl://天资料
                MineKtzlHomeActivity.start(getContext());
                break;
            case R.id.tv_live://开直播
                PermissionsUtils.getInstance().chekPermissions(getActivity(), PERMISSIONS_STORAGE, permissionsResult);
                break;
            case R.id.iv_top_msg://消息中心
            case R.id.iv_msg://消息中心
                MessageListActivity.start(getContext());
                break;
            case R.id.tv_dj:
                IllustrateActivity.start(mContext,0,rewardDesc);
                break;
        }
    }


    private void addHomeMenuBean(String title, int imgId) {
        HomeMenuBean homeMenuBean = new HomeMenuBean();
        homeMenuBean.setTitle(title);
        homeMenuBean.setImgId(imgId);
        homeMenuBeanList.add(homeMenuBean);
    }

    @Override
    public void onResume() {
        super.onResume();
        minePresenter.userdetail();

        if (showStatus == 2) {
            if (ceilingType) {
                ImmersionBar.with(getActivity()).statusBarColor(R.color.transparent).
                        statusBarDarkFont(true).
                        navigationBarColor(R.color.white)
                        .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                        .navigationBarDarkIcon(true).init();
            } else {
                ImmersionBar.with(getActivity())
                        .statusBarColor(R.color.white)
                        .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                        .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                        .navigationBarColor(R.color.white)
                        .navigationBarDarkIcon(true)
                        .init();
            }
        }
    }


    @Override
    public void onGetTaskBeanListSuccess(List<TaskBean> taskBeanLists) {
        nv_notice.start(taskBeanLists);
    }

    @Override
    public void onSigninSuccess() {

    }

    @Override
    public void onReceiveSuccess() {

    }

    @Override
    public void onGetContinuesignSuccess(int count) {
        tv_day_count.setText(count + "");
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
        initUserInfo();
    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList) {
        this.courseTimeBeanLists.clear();
        this.courseTimeBeanLists.addAll(courseTimeBeanList);
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
    public void onGetFansBeanSuccess(List<FansBean> fansBeanList, int pages) {

    }

    @Override
    public void onFocusSuccess() {

    }

    @Override
    public void onUpdatepasswordSuccess() {

    }

    @Override
    public void onCodeSuccess() {

    }

    @Override
    public void onGetIsLiveSuccess(int isLive, int realStatus, int liveId,String trtcPushAddr) {
        if (isLive == 2) {
            PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                    "当前还有其他课程在直播，继续直播", "确认", "");
            promptDialog.show();
            promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                @Override
                public void insure() {
                    Intent intent = new Intent(mContext, CameraPushMainActivity.class);
                    intent.putExtra("intent_url_push", trtcPushAddr);
                    intent.putExtra("liveId", liveId);
                    startActivity(intent);
                }

                @Override
                public void cancel() {
                }
            });
        } else if (isLive == 0) {
            Intent intent = new Intent(mContext, OpenLiveRoomActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onGetaboutinfoSuccess(String rewardDesc) {
        this.rewardDesc = rewardDesc;

    }

    @Override
    public void onGetKtzlBeanSuccess(List<KtzlBean> ktzlBeanList, int pages) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showStatus = 2;
            if (ceilingType) {
                ImmersionBar.with(getActivity()).statusBarColor(R.color.transparent).
                        statusBarDarkFont(true).
                        navigationBarColor(R.color.white)
                        .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                        .navigationBarDarkIcon(true).init();
            } else {
                ImmersionBar.with(getActivity())
                        .statusBarColor(R.color.white)
                        .fitsSystemWindows(false)  //使用该属性,必须指定状态栏颜色
                        .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                        .navigationBarColor(R.color.white)
                        .navigationBarDarkIcon(true)
                        .init();
            }
        } else {
            showStatus = 1;
        }

    }

    /**
     * 私信消息总数量监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(IMMessageCountEvent event) {
        //设置消息数量角标
        if (event.count > 0) {
            iv_msg.setBackgroundResource(R.mipmap.ic_personal_msg_pre);
            iv_top_msg.setBackgroundResource(R.mipmap.ic_personal_msg_pre);
        } else {
            iv_msg.setBackgroundResource(R.mipmap.ic_personal_msg_w);
            iv_top_msg.setBackgroundResource(R.mipmap.ic_personal_msg);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MineFragment.this);
        //清空粘性事件的缓存
        EventBus.getDefault().removeAllStickyEvents();
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //权限通过，可以做其他事情
            minePresenter.getIsLive();
        }

        @Override
        public void forbitPermissons() {
            ToastUtil.toastLongMessage("权限不通过");
        }
    };


}
