package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.CampusItemAdapter;
import com.tryine.sdgq.common.home.adapter.NoticeListAdapter;
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
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 公告列表
 *
 * @author: zhangshuaijun
 * @time: 2021-12-01 13:48
 */
public class NoticeListActivity extends BaseActivity implements HomeView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<AnnouncementBean> announcementBeanLists = new ArrayList<>();
    NoticeListAdapter noticeListAdapter;

    int pageNum = 1;
    HomePresenter homePresenter;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, NoticeListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_list;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("公告列表");
        smartRefresh();

        homePresenter = new HomePresenter(this);
        homePresenter.attachView(this);
        homePresenter.getAnnouncement(pageNum);

        initViews();
    }


    protected void initViews() {
        setWhiteBar();

        noticeListAdapter = new NoticeListAdapter(this, announcementBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(noticeListAdapter);
        noticeListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NoticeDetailActivity.start(mContext,announcementBeanLists.get(position));
            }
        });

    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }

    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                homePresenter.getAnnouncement(pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                homePresenter.getAnnouncement(pageNum);
            }
        });
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
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            announcementBeanLists.clear();
        }
        announcementBeanLists.addAll(announcementBeanList);
        if (announcementBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        noticeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAppointmentSuccess() {

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
