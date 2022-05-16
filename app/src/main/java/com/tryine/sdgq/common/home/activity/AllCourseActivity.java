package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
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
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.mine.adapter.DateListAdapter;
import com.tryine.sdgq.common.mine.bean.DateBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.CampusDialog;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-28 13:07
 */
public class AllCourseActivity extends BaseActivity implements HomeView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    HomePresenter homePresenter;
    List<CampusBean> campusBeanLists = new ArrayList<>(); //校区
    CampusBean selectCampusBean;//选中的校区


    List<CourseTimeBean> courseTimeBeanLists = new ArrayList<>();

    @BindView(R.id.tv_qhxq)
    TextView tv_qhxq;

    @BindView(R.id.calendarLayout)
    LinearLayout calendarLayout;

    @BindView(R.id.rv_dete)
    RecyclerView rv_dete;
    @BindView(R.id.rv_coursedate)
    RecyclerView rv_coursedate;
    @BindView(R.id.ll_notdata)
    LinearLayout ll_notdata;
    @BindView(R.id.tv_notdata)
    TextView tv_notdata;

    List<CourseBean> courseBeanList = new ArrayList<>();
    DateListAdapter dateListAdapter;
    HomeTeachAdapter homeTeachAdapter;

    String startDate;

    String experienceId;//体验卡id

    String receiptId;

    UserBean userBean;

    int selectPosition = 0;

    public static void start(Context context, String receiptId) {
        Intent intent = new Intent();
        intent.setClass(context, AllCourseActivity.class);
        intent.putExtra("receiptId", receiptId);
        context.startActivity(intent);
    }

    public static void startTy(Context context, String experienceId) {
        Intent intent = new Intent();
        intent.setClass(context, AllCourseActivity.class);
        intent.putExtra("experienceId", experienceId);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_all_course;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("全部课程");
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);

        receiptId = getIntent().getStringExtra("receiptId");
        experienceId = getIntent().getStringExtra("experienceId"); //不为空，表示使用体验卡约课

        homePresenter = new HomePresenter(mContext);
        homePresenter.attachView(this);
        homePresenter.getFicationList();
        startDate = DateTimeHelper.formatToString(new Date(), "yyyy-MM-dd");

        initCourseViews();

    }

    CommonAdapter homeTeachGridAdapter;

    private void initCourseViews() {

        List<DateBean> dateBeanList = DateTimeHelper.getDateList();
        DateListAdapter dateListAdapter = new DateListAdapter(this, dateBeanList);
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
                            PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                                    "是否预约当前课程", "确认", "取消");
                            promptDialog.show();
                            promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                                @Override
                                public void insure() {
                                    if (TextUtils.isEmpty(experienceId)) {
                                        homePresenter.appointment(courseTimeBean.getPiratesTeacherVoList().get(position).getId());
                                    } else {
                                        homePresenter.appointment1(courseTimeBean.getPiratesTeacherVoList().get(position).getId(), "1", experienceId);
                                    }
                                }

                                @Override
                                public void cancel() {

                                }
                            });
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


    @OnClick({R.id.iv_black, R.id.ll_xq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_xq:
                if (null != campusBeanLists) {
                    CampusDialog campusDialog = new CampusDialog(AllCourseActivity.this, campusBeanLists);
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
        }
    }


    @Override
    public void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList) {

    }

    @Override
    public void onGetVideoListSuccess(List<BannerBean> bannerBeanList) {

    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetBargainBeanListSuccess(List<BargainBean> bargainBeanList) {

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList) {

    }

    @Override
    public void onGetCourseTimeBeanListSuccess(List<CourseTimeBean> courseTimeBeanList, String startDate, String sysDate, int position) {
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
    public void onGetJqCourseBeanListSuccess(List<CourseBean> courseTimeBeanList) {

    }

    @Override
    public void onGetLiveBeanListSuccess(List<LiveBean> liveBeanList) {

    }

    @Override
    public void onGetRefreshLiveBeanListSuccess(List<LiveBean> liveBeanList) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {
        this.campusBeanLists = campusBeanList;
        if (TextUtils.isEmpty(receiptId)) {
            //默认取第一个校区
            if (null != campusBeanLists && campusBeanLists.size() > 0) {
                selectCampusBean = campusBeanLists.get(0);
                tv_qhxq.setText(selectCampusBean.getName());
            }
        } else {
            for (CampusBean campusBean : campusBeanList) {
                if (campusBean.getId().equals(receiptId)) {
                    this.selectCampusBean = campusBean;
                    tv_qhxq.setText(selectCampusBean.getName());
                }
            }
        }

        homePresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, selectPosition);

    }

    @Override
    public void onGetAnnouncementBeanListSuccess(List<AnnouncementBean> announcementBeanList, int pages) {

    }

    @Override
    public void onAppointmentSuccess() {
        ToastUtil.toastLongMessage("预约成功");
        homePresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, selectPosition);
    }

    @Override
    public void onSaveorderSuccess(String orderId) {

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetInvitefriendsSuccess(String title, String userId, String avatar) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {

    }

    @Override
    public void onBuyCourseSuccess() {

    }
}
