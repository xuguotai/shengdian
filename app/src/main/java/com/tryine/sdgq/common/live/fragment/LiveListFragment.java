package com.tryine.sdgq.common.live.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.activity.LiveCourseBuyDetailActivity;
import com.tryine.sdgq.common.live.activity.LiveCourseDetailActivity;
import com.tryine.sdgq.common.live.adapter.LiveAdapter;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.presenter.LiveHomePresenter;
import com.tryine.sdgq.common.live.view.LiveHomeView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 圣典视频列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:13
 */
public class LiveListFragment extends BaseFragment implements LiveHomeView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<LiveBean> liveBeanLists = new ArrayList<>();
    LiveAdapter liveAdapter;

    LiveHomePresenter liveHomePresenter;
    String typeId;
    int pageNum = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_live_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        smartRefresh();

        typeId = getArguments().getString("typeId");

        liveHomePresenter = new LiveHomePresenter(mContext);
        liveHomePresenter.attachView(this);
        liveHomePresenter.getSearchlivecourse(pageNum, "", typeId);
    }

    protected void initViews() {
        setWhiteBar();

        liveAdapter = new LiveAdapter(getContext(), liveBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(liveAdapter);
        liveAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LiveCourseDetailActivity.start(mContext, liveBeanLists.get(position).getId());

            }
        });

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
                liveHomePresenter.getSearchlivecourse(pageNum, "", typeId);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                liveHomePresenter.getSearchlivecourse(pageNum, "", typeId);
            }
        });
    }


    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetLiveBeanListSuccess(List<LiveBean> liveBeanLists) {

    }

    @Override
    public void onGetLiveBeanListSuccess(List<LiveBean> liveBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            liveBeanLists.clear();
        }
        liveBeanLists.addAll(liveBeanList);
        if (liveBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        liveAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBuyCourseSuccess() {

    }


    @Override
    public void onFailed(String message) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        ToastUtil.toastLongMessage(message);
    }
}
