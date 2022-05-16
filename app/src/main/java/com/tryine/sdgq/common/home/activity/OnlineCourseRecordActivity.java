package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
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
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.OnlineCourseRecordAdapter;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 线上课程记录
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 16:48
 */
public class OnlineCourseRecordActivity extends BaseActivity implements CourseView {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<CourseBean> courseBeanLists = new ArrayList<>();
    OnlineCourseRecordAdapter onlineCourseRecordAdapter;

    String type = "1";//1线上 2线下

    int pageNum = 1;

    CoursePresenter coursePresenter;

    String courseId, addrDes;

    public static void start(Context context, String type, String courseId, String addrDes) {
        Intent intent = new Intent();
        intent.setClass(context, OnlineCourseRecordActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("courseId", courseId);
        intent.putExtra("addrDes", addrDes);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_onlinecourserecord;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        courseId = getIntent().getStringExtra("courseId");
        addrDes = getIntent().getStringExtra("addrDes");
        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);


        type = getIntent().getStringExtra("type");
        tv_title.setText("1".equals(type) ? "线上课程记录" : "线下上课记录");
        if ("1".equals(type)) {

        } else {
            coursePresenter.getHavelist(courseId, pageNum);
        }
        initViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if ("1".equals(type)) {

        } else {
            coursePresenter.getHavelist(courseId, pageNum);
        }
    }

    protected void initViews() {
        onlineCourseRecordAdapter = new OnlineCourseRecordAdapter(this, courseBeanLists, type, addrDes);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(onlineCourseRecordAdapter);
        onlineCourseRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_pj) {
                    CourseCommentActivity.start(mContext, courseBeanLists.get(position).getId());
                } else if (view.getId() == R.id.tv_ktzl) {
                    if (courseBeanLists.get(position).getIsStart().equals("4") || courseBeanLists.get(position).getIsStart().equals("5")) {
                        LookCourseDataActivity.start(mContext, courseBeanLists.get(position).getId());
                    }else{
                        ToastUtil.toastLongMessage("老师还未上传资料，无法查看");
                    }
                }

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
                coursePresenter.getHavelist(courseId, pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                coursePresenter.getHavelist(courseId, pageNum);
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
        onlineCourseRecordAdapter.notifyDataSetChanged();
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
