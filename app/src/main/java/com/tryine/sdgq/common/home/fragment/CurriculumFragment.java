package com.tryine.sdgq.common.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.adapter.CurriculunCommentAdapter;
import com.tryine.sdgq.common.home.adapter.HomeTeachGridChildAdapter;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CommentBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.TeacherDetailPresenter;
import com.tryine.sdgq.common.home.view.TeacherDetailView;
import com.tryine.sdgq.common.mine.adapter.DateListAdapter;
import com.tryine.sdgq.common.mine.bean.DateBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 教师详情-课表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-26 13:09
 */
public class CurriculumFragment extends BaseFragment implements TeacherDetailView {


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.tv_qhxq)
    TextView tv_qhxq;

    @BindView(R.id.rv_dete)
    RecyclerView rv_dete;
    @BindView(R.id.rv_coursedate)
    RecyclerView rv_coursedate;
    @BindView(R.id.ll_notdata)
    LinearLayout ll_notdata;
    @BindView(R.id.tv_notdata)
    TextView tv_notdata;
    List<CourseTimeBean> courseTimeBeanLists = new ArrayList<>();


    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;
    List<CommentBean> commentBeanLists = new ArrayList<>();
    CurriculunCommentAdapter curriculunCommentAdapter;

    List<CampusBean> campusBeanLists = new ArrayList<>(); //校区


    String teacherId;

    TeacherDetailPresenter teacherDetailPresenter;

    int pageNum = 1;

    TeacherBean teacherBean;
    String startDate;

    UserBean userBean;

    int selectPosition = 0;


    @Override
    public int getlayoutId() {
        return R.layout.fragment_curriculum;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initCourseViews();
        smartRefresh();
        startDate = DateTimeHelper.formatToString(new Date(), "yyyy-MM-dd");
        teacherId = getArguments().getString("teacherId");

        teacherDetailPresenter = new TeacherDetailPresenter(mContext);
        teacherDetailPresenter.attachView(this);
        teacherDetailPresenter.getCommentlist(pageNum, teacherId);
//        teacherDetailPresenter.getFicationList();

    }


    public void setTeacherDetail(TeacherBean teacherBean) {
        this.teacherBean = teacherBean;
        teacherDetailPresenter.getTeacherCoureList(teacherBean.getCampusId(), startDate, teacherId, selectPosition);
    }


    private void initViews() {
        curriculunCommentAdapter = new CurriculunCommentAdapter(getContext(), commentBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_comment.setLayoutManager(lin);
        rv_comment.setAdapter(curriculunCommentAdapter);
        curriculunCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!commentBeanLists.get(position).getTopStatus().equals("1")) {
                    PersonalHomePageActivity.start(mContext, commentBeanLists.get(position).getUserId());
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
                if (null != teacherBean) {
                    teacherDetailPresenter.getTeacherCoureList(teacherBean.getCampusId(), startDate, teacherId, selectPosition);
                }
                dateListAdapter.setselectedTabPosition(position);
            }
        });


    }

//    @OnClick({R.id.ll_xq})
//    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.ll_xq:
//                if (null != campusBeanLists) {
//                    CampusDialog campusDialog = new CampusDialog(getActivity(), campusBeanLists);
//                    campusDialog.show();
//                    campusDialog.setOnItemClickListener(new CampusDialog.OnItemClickListener() {
//                        @Override
//                        public void resultReason(CampusBean homeMenuBean) {
//                            selectCampusBean = homeMenuBean;
//                            tv_qhxq.setText(homeMenuBean.getName());
//                            teacherDetailPresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, teacherId, selectPosition);
//                        }
//                    });
//                }
//                break;
//        }
//    }

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
                        if (courseTimeBean.getPiratesTeacherVoList().get(position).getCourseStatus().equals("0")) {
                            PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                                    "是否预约当前课程", "确认", "取消");
                            promptDialog.show();
                            promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                                @Override
                                public void insure() {
                                    teacherDetailPresenter.appointment(courseTimeBean.getPiratesTeacherVoList().get(position).getId());
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
                });

            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_coursedate.setLayoutManager(lin);
        rv_coursedate.setAdapter(homeTeachGridAdapter);


    }


    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                teacherDetailPresenter.getCommentlist(pageNum, teacherId);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                teacherDetailPresenter.getCommentlist(pageNum, teacherId);
            }
        });
    }


    @Override
    public void onGetCommentSuccess(List<CommentBean> commentBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            commentBeanLists.clear();
        }
        commentBeanLists.addAll(commentBeanList);
        if (commentBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rv_comment.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rv_comment.setVisibility(View.VISIBLE);
        }
        curriculunCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetVideoModelListSuccess(List<VideoModel> videoModelLists, int pages) {

    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> videoModelLists, int pages) {

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList, String selectDate, String sysDate, int position) {
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
            if (null == courseTimeBeanList || null != courseTimeBeanList && courseTimeBeanList.size() == 0) {
                List<String> dates = new ArrayList<>();
                dates.add("09:00");
                dates.add("10:00");
                dates.add("11:00");
                dates.add("12:00");
                dates.add("13:00");
                dates.add("14:00");
                dates.add("15:00");
                dates.add("16:00");
                this.courseTimeBeanLists.addAll(addCourse(dates));
            } else {
                this.courseTimeBeanLists.addAll(courseTimeBeanList);
            }

            if (null != courseTimeBeanLists && courseTimeBeanLists.size() > 0) {
                rv_coursedate.setVisibility(View.VISIBLE);
                ll_notdata.setVisibility(View.GONE);
            } else {
                rv_coursedate.setVisibility(View.GONE);
            }

            homeTeachGridAdapter.notifyDataSetChanged();

        }

    }


    @Override
    public void onGetTeacherBeanSuccess(TeacherBean teacherBean) {

    }

    @Override
    public void onGetTeacherBeanSuccess() {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {
//        this.campusBeanLists = campusBeanList;
//        //默认取第一个校区
//        if (null != campusBeanLists && campusBeanLists.size() > 0) {
//            selectCampusBean = campusBeanLists.get(0);
//            tv_qhxq.setText(selectCampusBean.getName());
//            teacherDetailPresenter.getTeacherCoureList(selectCampusBean.getId(), startDate, teacherId, selectPosition);
//        }
    }

    @Override
    public void onAppointmentSuccess() {
        ToastUtil.toastLongMessage("预约成功");
        teacherDetailPresenter.getTeacherCoureList(teacherBean.getCampusId(), startDate, teacherId, selectPosition);
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }

    /**
     * 当前日期没有课程，手动添加本地课程数据
     */
    private List<CourseTimeBean> addCourse(List<String> dates) {

        List<CourseTimeBean> courseTimeBeanList = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            CourseTimeBean courseTimeBean = new CourseTimeBean();
            courseTimeBean.setTime(dates.get(i));
            List<CourseBean> piratesTeacherVoList = new ArrayList<>();
            CourseBean courseBean = new CourseBean();
            courseBean.setAvatar(teacherBean.getHeadImg());
            courseBean.setName(teacherBean.getName());
            courseBean.setCouresName(teacherBean.getCouresCatsName());
            courseBean.setCourseStatus("4");
            piratesTeacherVoList.add(courseBean);
            courseTimeBean.setPiratesTeacherVoList(piratesTeacherVoList);
            courseTimeBeanList.add(courseTimeBean);

        }

        return courseTimeBeanList;

    }


}
