package com.tryine.sdgq.common.circle.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.AddCircleActivity;
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.activity.TopicDetailActivity;
import com.tryine.sdgq.common.circle.adapter.CircleItemAdapter;
import com.tryine.sdgq.common.circle.adapter.CircleTabBtnAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.presenter.CirclePresenter;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.activity.SignUpActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.adapter.MallGoodsAdapter;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 琴友圈-发现
 *
 * @author: zhangshuaijun
 * @time: 2021-11-23 16:24
 */
public class FindFragment extends BaseFragment implements CircleView {

    List<String> tabBeanList = new ArrayList<String>();
    @BindView(R.id.rv_tabbtn)
    public RecyclerView rv_tabbtn;
    CircleTabBtnAdapter circleTabBtnAdapter;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_circleData)
    RecyclerView rc_circleData;
    List<CircleBean> circleBeanLists = new ArrayList<>();
    CircleItemAdapter circleItemAdapter;

    CirclePresenter circlePresenter;

    int pageNum = 1;
    String moduleType = "0"; // 查询模块 0-发现 1-关注
    String searchType = "0"; //查询类型 0-最新 1-最火 2-明星 3-附近 4-观看视频

    @Override
    public int getlayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWhiteBar();
        smartRefresh();
        initViews();
    }

    private void initViews() {
        moduleType = getArguments().getString("moduleType");

        if ("1".equals(moduleType)) {
            rv_tabbtn.setVisibility(View.GONE);
        }

        circlePresenter = new CirclePresenter(mContext);
        circlePresenter.attachView(this);
        slRefreshLayout.autoRefresh();

        tabBeanList.add("最新");
        tabBeanList.add("最火");
        tabBeanList.add("附近");
        tabBeanList.add("视频");
        circleTabBtnAdapter = new CircleTabBtnAdapter(getContext(), tabBeanList);
        rv_tabbtn.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_tabbtn.setAdapter(circleTabBtnAdapter);
        circleTabBtnAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                searchType = position + "";
                pageNum = 1;
                circlePresenter.getCircleList(moduleType, searchType, SPUtils.getConfigString(Parameter.LOCATION, ""), pageNum, "");
                circleTabBtnAdapter.setSelectedTabPosition(position);
                circleItemAdapter.setSearchType(position);
            }
        });


        circleItemAdapter = new CircleItemAdapter(getContext(), circleBeanLists);
        rc_circleData.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rc_circleData.setAdapter(circleItemAdapter);
        circleItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_giveCount) {
                    circlePresenter.setGive(circleBeanLists.get(position).getId(), circleBeanLists.get(position).getIsGive(), position);
                } else {
                    CircleDetailActivity.start(mContext, circleBeanLists.get(position).getId());
                }
            }
        });


    }

    @OnClick({R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                AddCircleActivity.start(mContext);
                break;

        }
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
                circlePresenter.getCircleList(moduleType, searchType, SPUtils.getConfigString(Parameter.LOCATION, ""), pageNum, "");
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                circlePresenter.getCircleList(moduleType, searchType, SPUtils.getConfigString(Parameter.LOCATION, ""), pageNum, "");
            }
        });
    }


    @Override
    public void onAddCircleSuccess() {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {

    }


    @Override
    public void onGetTopicListSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetHdListSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            circleBeanLists.clear();
        }
        circleBeanLists.addAll(circleBeanList);
        if (circleBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_circleData.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_circleData.setVisibility(View.VISIBLE);
        }
        circleItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetsignatureSuccess(String signature) {

    }

    @Override
    public void onGetFansBeanSuccess(List<FansBean> fansBeanList, int pages) {

    }

    @Override
    public void onFocusSuccess(String isFocus) {

    }

    @Override
    public void onGiveSuccess(String type, int position) {
        if (type.equals("1")) {
            circleBeanLists.get(position).setGiveCount(Integer.parseInt(circleBeanLists.get(position).getGiveCount()) + 1 + "");
        } else {
            circleBeanLists.get(position).setGiveCount(Integer.parseInt(circleBeanLists.get(position).getGiveCount()) - 1 + "");
        }

        circleBeanLists.get(position).setIsGive(type);
        circleItemAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFailed(String message) {

    }
}
