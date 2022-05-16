package com.tryine.sdgq.common.mine.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.adapter.CircleTabBtnAdapter;
import com.tryine.sdgq.common.home.activity.AddCourseDataActivity;
import com.tryine.sdgq.common.home.activity.MineOnlineCourseListActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.common.live.activity.LiveTypeListActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.LiveRoomManager;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoom;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDef;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.audience.TCAudienceActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCConstants;
import com.tryine.sdgq.common.mine.bean.KtzlBean;
import com.tryine.sdgq.common.mine.bean.TaskBean;
import com.tryine.sdgq.common.mine.presenter.TaskPresenter;
import com.tryine.sdgq.common.mine.view.TaskView;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的课堂资料
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class MineKtzlFragment extends BaseFragment implements TaskView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;


    List<KtzlBean> ktzlBeanLists = new ArrayList<>();

    TaskPresenter taskPresenter;

    CommonAdapter commonAdapter;

    int pageNum = 1;
    int searchType = 0;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_minektzl;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchType = getArguments().getInt("type", 0);
        initViews();
        smartRefresh();
        taskPresenter = new TaskPresenter(mContext);
        taskPresenter.attachView(this);
        taskPresenter.getSelectclassroomdatalist(pageNum, searchType);
    }

    protected void initViews() {

        commonAdapter = new CommonAdapter(mContext, R.layout.item_ktzl, ktzlBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                KtzlBean ktzlBean = (KtzlBean) o;
                holder.setText(R.id.tv_name, ktzlBean.getUserName());
                holder.setText(R.id.tv_couresCatsName, ktzlBean.getCouresCatsName());
                holder.setText(R.id.tv_couresName, ktzlBean.getCouresName());
                holder.setText(R.id.tv_startTime, ktzlBean.getStartTime());
                GlideEngine.createGlideEngine().loadUserHeadImage(mContext, ktzlBean.getAvatar()
                        , holder.getView(R.id.iv_head));

                holder.setOnClickListener(R.id.tv_update, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ktzlBeanLists.get(position).getUploadStatus().equals("0")) {
                            AddCourseDataActivity.start(mContext, ktzlBeanLists.get(position).getId());
                        }
                    }
                });
                TextView tv_update = holder.getView(R.id.tv_update);
                TextView tv_ysc = holder.getView(R.id.tv_ysc);

                if (ktzlBeanLists.get(position).getUploadStatus().equals("0")) {
                    tv_update.setVisibility(View.VISIBLE);
                    tv_ysc.setVisibility(View.GONE);
                } else {
                    tv_update.setVisibility(View.GONE);
                    tv_ysc.setVisibility(View.VISIBLE);
                }

            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(commonAdapter);
    }


    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                taskPresenter.getSelectclassroomdatalist(pageNum, searchType);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                taskPresenter.getSelectclassroomdatalist(pageNum, searchType);
            }
        });
    }


    @Override
    public void onGetTaskBeanListSuccess(List<TaskBean> taskBeanLists) {

    }

    @Override
    public void onSigninSuccess() {

    }

    @Override
    public void onReceiveSuccess() {

    }

    @Override
    public void onGetContinuesignSuccess(int count) {

    }

    @Override
    public void onResume() {
        super.onResume();
        pageNum = 1;
        taskPresenter.getSelectclassroomdatalist(pageNum, searchType);
    }

    @Override
    public void onGetKtzlBeanSuccess(List<KtzlBean> ktzlBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            ktzlBeanLists.clear();
        }
        ktzlBeanLists.addAll(ktzlBeanList);
        if (ktzlBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
    }
}
