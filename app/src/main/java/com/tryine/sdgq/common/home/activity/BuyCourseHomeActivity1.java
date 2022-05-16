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
import com.tryine.sdgq.common.home.adapter.CourseListAdapter;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.CampusDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购买课程首页
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 10:32
 */
public class BuyCourseHomeActivity1 extends BaseActivity implements CourseView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.tv_qhxq)
    TextView tv_qhxq;

    List<CampusBean> campusBeanLists = new ArrayList<>();
    List<CourseBean> courseBeanLists = new ArrayList<>();
    CourseListAdapter courseListAdapter;
    CampusBean selectCampusBean;//选中的校区

    CoursePresenter coursePresenter;

    int pageNum = 1;



    @Override
    public int getLayoutId() {
        return R.layout.activity_coursehome_buy;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();

        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);
        coursePresenter.getFicationList();

        courseListAdapter = new CourseListAdapter(this, courseBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(courseListAdapter);
        courseListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_xq) {
                    CourseDateilActivity.start(mContext, courseBeanLists.get(position));
                } else if (view.getId() == R.id.tv_submit) {
                    SignUpActivity.start(mContext, courseBeanLists.get(position));
                }
            }
        });

    }


    @OnClick({R.id.iv_black, R.id.tv_qhxq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_qhxq:
                if (null != campusBeanLists) {
                    CampusDialog campusDialog = new CampusDialog(this, campusBeanLists);
                    campusDialog.show();
                    campusDialog.setOnItemClickListener(new CampusDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(CampusBean homeMenuBean) {
                            selectCampusBean = homeMenuBean;
                            tv_qhxq.setText(homeMenuBean.getName());
                            pageNum = 1;
//                            coursePresenter.getCourseList(selectCampusBean.getId(), pageNum);
                        }
                    });
                }
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
//                coursePresenter.getCourseList(selectCampusBean.getId(), pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
//                coursePresenter.getCourseList(selectCampusBean.getId(), pageNum);
            }
        });
    }


    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            courseBeanLists.clear();
        }
        courseBeanLists.addAll(courseBeanList);
        if (courseBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        courseListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {
        this.campusBeanLists = campusBeanList;
        //默认取第一个校区
        if (null != campusBeanLists && campusBeanLists.size() > 0) {
            selectCampusBean = campusBeanLists.get(0);
            tv_qhxq.setText(selectCampusBean.getName());
//            coursePresenter.getCourseList(selectCampusBean.getId(), pageNum);
        }
    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList, int pages) {

    }

    @Override
    public void onGetcancelledSuccess(int count,int positions) {

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
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        ToastUtil.toastLongMessage(message);
    }
}
