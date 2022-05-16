package com.tryine.sdgq.common.mine.fragment;

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
import com.tryine.sdgq.common.home.activity.BargainOrderDetailActivity;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.BargainOrderDetailBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.presenter.BargainPresenter;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.common.mine.adapter.MineBargainAdapter;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的砍价订单
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class MineBargainFragment extends BaseFragment implements BargainView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<BargainBean> bargainBeanLists = new ArrayList<>();
    MineBargainAdapter mineBargainAdapter;

    BargainPresenter bargainPresenter;

    int pageNum = 1;

    String status;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_bargain_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        status = getArguments().getString("status");
        smartRefresh();
        initViews();

        bargainPresenter = new BargainPresenter(mContext);
        bargainPresenter.attachView(this);
        bargainPresenter.getBargainOrderlist(pageNum, status,"");
    }

    protected void initViews() {

        mineBargainAdapter = new MineBargainAdapter(getContext(), bargainBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(mineBargainAdapter);
        mineBargainAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BargainOrderDetailActivity.start(mContext, bargainBeanLists.get(position).getId());
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
                bargainPresenter.getBargainOrderlist(pageNum, status,"");
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                bargainPresenter.getBargainOrderlist(pageNum, status,"");
            }
        });
    }


    @Override
    public void onGetBargainBeanListSuccess(List<BargainBean> bargainBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            bargainBeanLists.clear();
        }
        bargainBeanLists.addAll(bargainBeanList);
        if (bargainBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        mineBargainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCampusBeanSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onSaveorderSuccess(String orderId) {
        BargainOrderDetailActivity.start(mContext, orderId);
    }


    @Override
    public void onGetBargainOrderDetailBeanSuccess(BargainOrderDetailBean bargainOrderDetailBean) {

    }

    @Override
    public void onBargainBuySuccess() {

    }


    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
