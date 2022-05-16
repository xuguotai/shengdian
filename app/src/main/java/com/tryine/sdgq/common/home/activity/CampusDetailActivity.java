package com.tryine.sdgq.common.home.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tryine.sdgq.Application;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.CampusCourseAdapter;
import com.tryine.sdgq.common.home.adapter.CampusTeacherAdapter;
import com.tryine.sdgq.common.home.adapter.HomeTeachAdapter;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.FullScreenImg.AssImgPreviewActivity;
import com.tryine.sdgq.view.banner.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 校区详情
 *
 * @author: zhangshuaijun
 * @time: 2021-11-26 09:47
 */
public class CampusDetailActivity extends BaseActivity implements CourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.banner)
    BannerViewPager bannerView;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_businessHours)
    TextView tv_businessHours;
    @BindView(R.id.tv_contact)
    TextView tv_contact;
    @BindView(R.id.tv_addrDes)
    TextView tv_addrDes;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_headmasterName)
    TextView tv_headmasterName;

    List<CourseBean> courseBeanLists = new ArrayList<>();
    @BindView(R.id.rv_courseData)
    RecyclerView rv_courseData;
    CampusCourseAdapter campusCourseAdapter;

    List<TeacherBean> teacherBeanLists = new ArrayList<>();
    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    CampusTeacherAdapter campusTeacherAdapter;

    CampusBean campusBean;

    CoursePresenter coursePresenter;

    public static void start(Context context, CampusBean campusBean) {
        Intent intent = new Intent();
        intent.setClass(context, CampusDetailActivity.class);
        intent.putExtra("campusBean", campusBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_campus_dateil;
    }


    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("校区详情");
        campusBean = (CampusBean) getIntent().getSerializableExtra("campusBean");
        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);
        coursePresenter.getCampusDetailCourseList(campusBean.getId(), 1);
        coursePresenter.getTeacherlist(campusBean.getId(),1);
        initBanner();
        initViews();
    }

    private void initBanner() {
        List<BannerBean> bannerBeanList = new ArrayList<>();
        BannerBean bannerBean = new BannerBean();
        bannerBean.setImgUrl(campusBean.getCoverUrl());
        bannerBeanList.add(bannerBean);

        bannerView.initBanner(bannerBeanList, false)//关闭3D画廊效果
                .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addStartTimer(bannerBeanList.size() > 1 ? 5 : 10000)//自动轮播5秒间隔
                .finishConfig()//这句必须加
                .addRoundCorners(UIUtils.dip2px(5))//圆角
                .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                            List<String> imgList = new ArrayList<>();
                            for (int i = 0; i < bannerBeanList.size(); i++) {
                                imgList.add(bannerBeanList.get(i).getImgUrl());
                            }
                            Intent intent = new Intent(mContext, AssImgPreviewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("goodImages", new ArrayList<>(imgList));
                            bundle.putInt("currentIndex", position);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                    }
                });

    }


    private void initViews() {

        campusCourseAdapter = new CampusCourseAdapter(this, courseBeanLists,0);
        rv_courseData.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_courseData.setAdapter(campusCourseAdapter);
        campusCourseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CourseDateilActivity.start(mContext, courseBeanLists.get(position));
            }
        });

        campusTeacherAdapter = new CampusTeacherAdapter(this, teacherBeanLists);
        LinearLayoutManager lin1 = new LinearLayoutManager(this);
        lin1.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
        rv_data.setLayoutManager(lin1);
        rv_data.setAdapter(campusTeacherAdapter);
        campusTeacherAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TeacherDetailHomeActivity.start(mContext, teacherBeanLists.get(position).getId());
            }
        });


        tv_name.setText(campusBean.getName());
        tv_businessHours.setText(campusBean.getBusinessHours());
        tv_contact.setText(campusBean.getContact()+"(拨打电话)");
        tv_addrDes.setText(campusBean.getAddrDes());
        tv_headmasterName.setText(campusBean.getHeadmasterName());
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext,campusBean.getHeadmasterUrl(),iv_head);
    }


    @OnClick({R.id.iv_black, R.id.tv_more, R.id.tv_dh,R.id.tv_more1,R.id.tv_contact,R.id.ll_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_more:
                if (null != campusBean) {
                    BuyCourseHomeActivity.start(mContext, campusBean.getId());
                }
                break;
            case R.id.tv_dh:
                if (null != campusBean) {
                    ShopPositionActivity.start(mContext, campusBean.getLonLatTencent(), campusBean.getAddrDes());
                }
                break;
            case R.id.tv_more1:
                if (null != campusBean) {
                    TeacherListActivity.start(mContext, campusBean);
                }
                break;
            case R.id.tv_contact:
                if (null != campusBean) {
                    callPhone(campusBean.getContact());
                }
                break;
            case R.id.ll_wx:
                if (null != campusBean) {
                    //获取剪贴板管理器
                    ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("text", campusBean.getHeadmasterWeact());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtil.toastShortMessage("已复制微信号，请去微信添加校长");
                }
                break;
        }
    }


    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {
        this.courseBeanLists.clear();
        this.courseBeanLists.addAll(courseBeanList);
        campusCourseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList ,int pages) {
        this.teacherBeanLists.clear();
        this.teacherBeanLists.addAll(teacherBeanList);
        campusTeacherAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetcancelledSuccess(int count, int positions) {

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

    }

    @Override
    public void onSuspendedSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
