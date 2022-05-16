package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.CampusCourseAdapter;
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
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.NoticeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-21 13:42
 */
public class NoticeDetailActivity extends BaseActivity implements HomeView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_noticeTitle)
    TextView tv_noticeTitle;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.rv_courseData)
    RecyclerView rv_courseData;
    CampusCourseAdapter campusCourseAdapter;

    HomePresenter homePresenter;

    List<CourseBean> courseBeanLists = new ArrayList<>();

    AnnouncementBean announcementBean;

    public static void start(Context context, AnnouncementBean announcementBean) {
        Intent intent = new Intent();
        intent.setClass(context, NoticeDetailActivity.class);
        intent.putExtra("announcementBean", announcementBean);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_notice;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("公告详情");
        announcementBean = (AnnouncementBean) getIntent().getSerializableExtra("announcementBean");

        homePresenter = new HomePresenter(this);
        homePresenter.attachView(this);

        initView();

    }

    private void initView() {
        if (null != announcementBean) {
            homePresenter.getCourinfoList(announcementBean.getCouresIdList(), 1);

            GlideEngine.createGlideEngine().loadImage(mContext, announcementBean.getImgUrl()
                    , iv_cover);
            tv_noticeTitle.setText("- " + announcementBean.getName() + " -");
            tv_content.setText(announcementBean.getContent());

            campusCourseAdapter = new CampusCourseAdapter(this, courseBeanLists,1);
            rv_courseData.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            rv_courseData.setAdapter(campusCourseAdapter);
            campusCourseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    CourseDateilActivity.start(mContext, courseBeanLists.get(position));
                }
            });
        }
    }


    @OnClick({R.id.iv_black,R.id.tv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_more:
                BuyCourseHomeActivity.start(mContext);
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
    public void onGetCourseTimeBeanListSuccess(List<CourseTimeBean> courseTimeBeanList, String selectDate, String sysDate, int position) {

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

    }

    @Override
    public void onGetAnnouncementBeanListSuccess(List<AnnouncementBean> announcementBeanList, int pages) {

    }

    @Override
    public void onAppointmentSuccess() {

    }

    @Override
    public void onSaveorderSuccess(String orderId) {

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {
        this.courseBeanLists.clear();
        this.courseBeanLists.addAll(courseBeanList);
        campusCourseAdapter.notifyDataSetChanged();
    }



    @Override
    public void onGetInvitefriendsSuccess(String title, String userId, String avatar) {

    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {

    }

    @Override
    public void onBuyCourseSuccess() {

    }
}
