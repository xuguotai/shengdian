package com.tryine.sdgq.common.mine.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.mine.activity.MineTyCardActivity;
import com.tryine.sdgq.common.mine.adapter.CollectCircleAdapter;
import com.tryine.sdgq.common.mine.adapter.RewardAdapter;
import com.tryine.sdgq.common.mine.adapter.TyCardListAdapter;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.common.mine.presenter.RewarPresenter;
import com.tryine.sdgq.common.mine.view.RewarView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的打赏/我的礼物
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class RewardFragment extends BaseFragment implements RewarView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<RewarBean> rewarBeanLists = new ArrayList<>();
    RewardAdapter rewardAdapter;

    int pageNum = 1;
    String type;//类型  0:我的打赏 1:我的礼物
    String giftType;//豆子类型 0:金豆礼物 1:银豆礼物

    RewarPresenter rewarPresenter;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_reward_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getString("type");
        giftType = getArguments().getString("giftType");
        initViews();
        rewarPresenter = new RewarPresenter(mContext);
        rewarPresenter.attachView(this);
        rewarPresenter.getGiftRecordList(pageNum, giftType, type);

    }

    protected void initViews() {
        setWhiteBar();
        smartRefresh();
        rewardAdapter = new RewardAdapter(getContext(), rewarBeanLists, type);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(rewardAdapter);
        rewardAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
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
                rewarPresenter.getGiftRecordList(pageNum, giftType, type);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                rewarPresenter.getGiftRecordList(pageNum, giftType, type);
            }
        });
    }

    @Override
    public void onGetRewarBeanListSuccess(List<RewarBean> rewarBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            rewarBeanLists.clear();
        }
        rewarBeanLists.addAll(rewarBeanList);
        if (rewarBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        rewardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPayRecordBeanListSuccess(List<PayRecordBean> payRecordBeanList, int pages) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
