package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.PersonalSettingActivity;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.fragment.PersonalPageVideoFragment;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CommentBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.fragment.TeacherCircleListFragment;
import com.tryine.sdgq.common.home.fragment.TeacherCourseListFragment;
import com.tryine.sdgq.common.home.fragment.TeacherVideoListFragment;
import com.tryine.sdgq.common.home.fragment.CurriculumFragment;
import com.tryine.sdgq.common.home.presenter.TeacherDetailPresenter;
import com.tryine.sdgq.common.home.view.TeacherDetailView;
import com.tryine.sdgq.common.mine.adapter.TeacherTabAdapter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 教师详情
 *
 * @author: zhangshuaijun
 * @time: 2021-11-26 11:08
 */
public class TeacherDetailHomeActivity extends BaseActivity implements TeacherDetailView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_jyjy)
    TextView tv_jyjy;
    @BindView(R.id.tv_ssxq)
    TextView tv_ssxq;
    @BindView(R.id.tv_pf)
    TextView tv_pf;


    @BindView(R.id.rv_tab)
    public RecyclerView rv_tab;
    TeacherTabAdapter tabAdapter;

    @BindView(R.id.vp_view)
    ViewPager vpView;

    String teacherId;
    TeacherBean teacherBean;

    String teacherUserId; //老师用户id

    TeacherDetailPresenter teacherDetailPresenter;
    CurriculumFragment curriculumFragment;
    TeacherVideoListFragment teacherVideoListFragment;
    TeacherCircleListFragment teacherCircleListFragment;
    TeacherCourseListFragment teacherCourseListFragment;

    int teacherType = 2; //老师类型 0-线上老师 1-线下老师 2-线上线下老师    -1

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentManager fragmentManager;
    List<String> tabBeanList = new ArrayList<String>();
    int selectedTabPosition = 0;

    public static void start(Context context, String teacherId) {
        Intent intent = new Intent();
        intent.setClass(context, TeacherDetailHomeActivity.class);
        intent.putExtra("teacherId", teacherId);
        context.startActivity(intent);
    }

    public static void start(Context context, String teacherId, int teacherType, String teacherUserId, String headImg, String userName) {
        Intent intent = new Intent();
        intent.setClass(context, TeacherDetailHomeActivity.class);
        intent.putExtra("teacherId", teacherId);
        intent.putExtra("teacherType", teacherType);
        intent.putExtra("teacherUserId", teacherUserId);
        intent.putExtra("headImg", headImg);
        intent.putExtra("userName", userName);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_teacher_detailhome;
    }

    @Override
    protected void init() {
        tv_title.setText("教师详情");
        teacherId = getIntent().getStringExtra("teacherId");
        teacherUserId = getIntent().getStringExtra("teacherUserId");
        String headImg = getIntent().getStringExtra("headImg");
        String userName = getIntent().getStringExtra("userName");
        teacherType = getIntent().getIntExtra("teacherType", 2);

        teacherDetailPresenter = new TeacherDetailPresenter(this);
        teacherDetailPresenter.attachView(this);
        teacherDetailPresenter.getTeacherdetail(teacherId);

        if (teacherType == 0) {
            tabBeanList.add("视频");
            tabBeanList.add("琴友圈");
            tabBeanList.add("线上课程");
        } else if (teacherType == 1) {
            tabBeanList.add("课表");
            tabBeanList.add("视频");
            tabBeanList.add("琴友圈");
        } else if (teacherType == 2) {
            tabBeanList.add("课表");
            tabBeanList.add("视频");
            tabBeanList.add("琴友圈");
            tabBeanList.add("线上课程");
        }


        tabAdapter = new TeacherTabAdapter(this, tabBeanList);
        rv_tab.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_tab.setAdapter(tabAdapter);
        tabAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                vpView.setCurrentItem(position);
                tabAdapter.setSelectedTabPosition(position);
            }
        });
        fragmentManager = getSupportFragmentManager();

        curriculumFragment = new CurriculumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("teacherId", teacherId);
        curriculumFragment.setArguments(bundle);

        teacherVideoListFragment = new TeacherVideoListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("selectType", "1");
        teacherVideoListFragment.setArguments(bundle1);

        teacherCircleListFragment = new TeacherCircleListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("teacherId", teacherId);
        teacherCircleListFragment.setArguments(bundle2);

        teacherCourseListFragment = new TeacherCourseListFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("teacherId", teacherId);
        teacherCourseListFragment.setArguments(bundle3);


        if (teacherType == 0) {
            fragmentList.add(teacherVideoListFragment);
            fragmentList.add(teacherCircleListFragment);
            fragmentList.add(teacherCourseListFragment);
        } else if (teacherType == 1) {
            fragmentList.add(curriculumFragment);
            fragmentList.add(teacherVideoListFragment);
            fragmentList.add(teacherCircleListFragment);
        } else if (teacherType == 2) {
            fragmentList.add(curriculumFragment);
            fragmentList.add(teacherVideoListFragment);
            fragmentList.add(teacherCircleListFragment);
            fragmentList.add(teacherCourseListFragment);
        }

        vpView.setAdapter(new MainPagerAdapter(fragmentManager));
        vpView.setCurrentItem(0);
        vpView.setOffscreenPageLimit(12);
        vpView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedTabPosition = position;
                tabAdapter.notifyDataSetChanged();
                tabAdapter.setSelectedTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        if (teacherType == 0) { //线上老师,直接用老师用户id查
            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, headImg
                    , iv_head);
            tv_name.setText(userName);
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
    public void onGetCommentSuccess(List<CommentBean> commentBeanList, int pages) {

    }

    @Override
    public void onGetVideoModelListSuccess(List<VideoModel> videoModelLists, int pages) {

    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> videoModelLists, int pages) {

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList, String selectDate, String sysDate, int position) {

    }

    @Override
    public void onGetTeacherBeanSuccess(TeacherBean teacherBean) {
        if (null != teacherBean) {

            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, teacherBean.getHeadImg()
                    , iv_head);
            tv_name.setText(teacherBean.getName());
            curriculumFragment.setTeacherDetail(teacherBean);
            teacherVideoListFragment.setUserId(teacherBean.getUserId());
            teacherCircleListFragment.setUserId(teacherBean.getUserId());
            teacherCourseListFragment.setUserId(teacherBean.getUserId());
            tv_jyjy.setText("教育经验：" + teacherBean.getEducationLayer() + "年");
            tv_ssxq.setText("所属校区：" + teacherBean.getCampusName());
            tv_pf.setText("评分：" + teacherBean.getStar() + " 分");
        }


    }

    @Override
    public void onGetTeacherBeanSuccess() {
        if (!TextUtils.isEmpty(teacherUserId)) {
            teacherVideoListFragment.setUserId(teacherUserId);
            teacherCircleListFragment.setUserId(teacherUserId);
            teacherCourseListFragment.setUserId(teacherUserId);
        }
    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onAppointmentSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    class MainPagerAdapter extends FragmentPagerAdapter {
        private String[] titles = new String[]{};

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

}