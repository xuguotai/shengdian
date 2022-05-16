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
import com.tryine.sdgq.common.home.adapter.CampusListTeacherAdapter;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.common.live.activity.LiveSearchActivity;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-02-14 14:27
 */
public class TeacherListActivity extends BaseActivity implements CourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    List<TeacherBean> teacherBeanLists = new ArrayList<>();
    CampusListTeacherAdapter campusTeacherAdapter;

    CoursePresenter coursePresenter;
    int pageNum = 1;

    CampusBean campusBean;

    public static void start(Context context, CampusBean campusBean) {
        Intent intent = new Intent();
        intent.setClass(context, TeacherListActivity.class);
        intent.putExtra("campusBean", campusBean);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.ativity_list_teacher;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("教师列表");
        campusBean = (CampusBean) getIntent().getSerializableExtra("campusBean");
        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);
        coursePresenter.getTeacherlist(campusBean.getId(), pageNum);


        campusTeacherAdapter = new CampusListTeacherAdapter(this, teacherBeanLists);
        rc_data.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rc_data.setAdapter(campusTeacherAdapter);
        campusTeacherAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TeacherDetailHomeActivity.start(mContext, teacherBeanLists.get(position).getId());
            }
        });

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
                coursePresenter.getTeacherlist(campusBean.getId(), pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                coursePresenter.getTeacherlist(campusBean.getId(), pageNum);
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

    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList ,int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            teacherBeanLists.clear();
        }
        teacherBeanLists.addAll(teacherBeanList);
        if (teacherBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
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
