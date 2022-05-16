package com.tryine.sdgq.common.home.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.home.activity.BuyCourseHomeActivity;
import com.tryine.sdgq.common.home.activity.CancelReserveCourseActivity;
import com.tryine.sdgq.common.home.activity.OnlineCourseRecordActivity;
import com.tryine.sdgq.common.home.activity.ReserveCourseActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerCourseViewPager;
import com.tryine.sdgq.view.dialog.PauseCourseDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的线下课程
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class MineofflineCourseFragment extends BaseFragment implements CourseView {

    @BindView(R.id.banner)
    BannerCourseViewPager bannerView;
    @BindView(R.id.tv_cancelCount)
    TextView tv_cancelCount;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.ll_notdata)
    LinearLayout ll_notdata;
    @BindView(R.id.tv_qkk)
    TextView tv_qkk;
    @BindView(R.id.switch_zt)
    Switch switch_zt;
    @BindView(R.id.ll_lqf)
    LinearLayout ll_lqf;
    @BindView(R.id.tv_lqf)
    TextView tv_lqf;

    CoursePresenter coursePresenter;

    List<CourseBean> courseBeanList = new ArrayList<>();
    int position = 0;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_mineofflinecourse;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        coursePresenter = new CoursePresenter(mContext);
        coursePresenter.attachView(this);
        coursePresenter.getMyCourse();
    }

    protected void initViews() {
        setWhiteBar();
        tv_qkk.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_qkk.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void initBanner(List<CourseBean> courseBeanList) {
        coursePresenter.getCancelled(courseBeanList.get(position).getCouresId(), 0);
        switch_zt.setChecked(courseBeanList.get(position).getStatus().equals("0") ? true : false);

        if (courseBeanList.get(position).getIsCost() == 1) {
            ll_lqf.setVisibility(View.VISIBLE);
            tv_lqf.setText(courseBeanList.get(position).getAdditionalName()+ " 已交纳"  + courseBeanList.get(position).getAdditionalAmount() + "元");
        } else {
            ll_lqf.setVisibility(View.GONE);
        }

        bannerView.initBanner(courseBeanList, false)//关闭3D画廊效果
                .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .finishConfig()//这句必须加
                .addRoundCorners(UIUtils.dip2px(5))//圆角
                .addPoint(courseBeanList.size())//添加指示器
                .addOnPageSelected(new BannerCourseViewPager.OnPageSelectedListener() {
                    @Override
                    public void onPageSelected(int positions) {
                        position = positions;
                        coursePresenter.getCancelled(courseBeanList.get(position).getCouresId(), positions);
                        switch_zt.setChecked(courseBeanList.get(position).getStatus().equals("0") ? true : false);
                        if (courseBeanList.get(position).getIsCost() == 1) {
                            ll_lqf.setVisibility(View.VISIBLE);
                            tv_lqf.setText(courseBeanList.get(position).getAdditionalName() + " 已交纳" + courseBeanList.get(position).getAdditionalAmount() + "元");
                        } else {
                            ll_lqf.setVisibility(View.GONE);
                        }
                    }
                })
                .addBannerListener(new BannerCourseViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int positions) {
                        position = positions;
                    }
                });


    }

    @OnClick({R.id.ll_yy, R.id.ll_offlineCourse, R.id.ll_cancel, R.id.tv_qkk, R.id.ll_zt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_yy:
                if (null != courseBeanList && courseBeanList.size() > 0) {
                    ReserveCourseActivity.start(mContext, courseBeanList.get(position).getCouresId(), courseBeanList.get(position).getAddrDes());
                }
                break;
            case R.id.ll_offlineCourse:
                if (null != courseBeanList && courseBeanList.size() > 0) {
                    OnlineCourseRecordActivity.start(mContext, "2", courseBeanList.get(position).getCouresId(), courseBeanList.get(position).getAddrDes());
                }
                break;
            case R.id.ll_cancel:
                if (null != courseBeanList && courseBeanList.size() > 0) {
                    CancelReserveCourseActivity.start(mContext, courseBeanList.get(position).getCouresId(), courseBeanList.get(position).getAddrDes());
                }
                break;
            case R.id.tv_qkk:
                BuyCourseHomeActivity.start(getContext());
                break;
            case R.id.ll_zt:
                if (null != courseBeanList && courseBeanList.size() > 0 && !courseBeanList.get(position).getStatus().equals("0")) {
                    coursePresenter.getselectsuspended(courseBeanList.get(position).getId(), position);
                }else{
                    ToastUtil.toastLongMessage("请联系客服");
                }
                break;
        }
    }


    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {
        if (null != courseBeanList && courseBeanList.size() > 0) {
            this.courseBeanList = courseBeanList;
            initBanner(courseBeanList);
            nestedScrollView.setVisibility(View.VISIBLE);
            ll_notdata.setVisibility(View.GONE);
        } else {
            nestedScrollView.setVisibility(View.GONE);
            ll_notdata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList, int pages) {

    }

    @Override
    public void onGetcancelledSuccess(int count, int positions) {
        if (position == positions) {
            tv_cancelCount.setText(count + "次");
        }
    }

    @Override
    public void onAddCampusSuccess() {

    }

    @Override
    public void onAddCourseDataSuccess() {

    }

    @Override
    public void onCancellationSuccess() {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {

    }

    @Override
    public void onGetDetailinfoSuccess(String classContent, String problemContent, String homeworkContent, String nextContent, String attachmentUrl, String videoUrl) {

    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onselectsuspendedSuccess(int selectsuspended, int positions) {
        PauseCourseDialog pauseCourseDialog = new PauseCourseDialog(mContext, selectsuspended, courseBeanList.get(position));
        pauseCourseDialog.show();
        pauseCourseDialog.setOnItemClickListener(new PauseCourseDialog.OnItemClickListener() {
            @Override
            public void suspended(String id, String beanType, String beanNumber, String suspendedNum) {
                coursePresenter.suspended(id, beanType, beanNumber + "", suspendedNum);
            }
        });
    }

    @Override
    public void onSuspendedSuccess() {
        ToastUtil.toastLongMessage("暂停成功");
        coursePresenter.getMyCourse();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != courseBeanList && courseBeanList.size() > 0) {
            coursePresenter.getCancelled(courseBeanList.get(position).getCouresId(), position);
        }
    }
}
