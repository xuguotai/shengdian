package com.tryine.sdgq.common.live.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.adapter.LiveAdapter;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.fragment.LiveListFragment;
import com.tryine.sdgq.common.live.presenter.LiveHomePresenter;
import com.tryine.sdgq.common.live.view.LiveHomeView;
import com.tryine.sdgq.common.mine.adapter.TabAdapter1;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 直播中列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-25 10:35
 */
public class LiveListActivity extends BaseActivity implements LiveHomeView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<LiveBean> liveBeanLists = new ArrayList<>();
    LiveAdapter liveAdapter;

    LiveHomePresenter liveHomePresenter;
    int pageNum = 1;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LiveListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_list1;
    }

    @Override
    protected void init() {
        setWhiteBar();
        initViews();
        smartRefresh();
        liveHomePresenter = new LiveHomePresenter(this);
        liveHomePresenter.attachView(this);
        liveHomePresenter.getSelectindexallcourselist(pageNum);
    }

    protected void initViews() {

        liveAdapter = new LiveAdapter(this, liveBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
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
                liveHomePresenter.getSelectindexallcourselist(pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                liveHomePresenter.getSelectindexallcourselist(pageNum);
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
