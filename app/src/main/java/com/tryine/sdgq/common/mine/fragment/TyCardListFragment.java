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
import com.tryine.sdgq.common.home.activity.AllCourseActivity;
import com.tryine.sdgq.common.home.activity.CampusDetailActivity;
import com.tryine.sdgq.common.home.adapter.CampusItemAdapter;
import com.tryine.sdgq.common.mine.activity.MineTyCardActivity;
import com.tryine.sdgq.common.mine.activity.TyCardZZActivity;
import com.tryine.sdgq.common.mine.adapter.TyCardListAdapter;
import com.tryine.sdgq.common.mine.bean.CardBean;
import com.tryine.sdgq.common.mine.bean.ExperienceBean;
import com.tryine.sdgq.common.mine.presenter.TyCardPresenter;
import com.tryine.sdgq.common.mine.view.TyCardView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author: zhangshuaijun
 * @time: 2021-11-29 17:19
 */
public class TyCardListFragment extends BaseFragment implements TyCardView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<CardBean> cardBeanLists = new ArrayList<>();
    TyCardListAdapter tyCardListAdapter;

    TyCardPresenter tyCardPresenter;

    int pageNum = 1;
    int status = 1;


    @Override
    public int getlayoutId() {
        return R.layout.fragment_tycard_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        status = getArguments().getInt("status");
        smartRefresh();
        initViews();
        tyCardPresenter = new TyCardPresenter(mContext);
        tyCardPresenter.attachView(this);
        slRefreshLayout.autoRefresh();
    }

    protected void initViews() {
        tyCardListAdapter = new TyCardListAdapter(getContext(), cardBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(tyCardListAdapter);
        tyCardListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_zzhy) {
                    TyCardZZActivity.start(mContext, cardBeanLists.get(position).getId());
                } else if (view.getId() == R.id.tv_detail) {
                    MineTyCardActivity.start(mContext, cardBeanLists.get(position));
                }else if (view.getId() == R.id.tv_jxsy) {
                    AllCourseActivity.startTy(mContext, cardBeanLists.get(position).getId());
                }else if (view.getId() == R.id.ljsy) {
                    AllCourseActivity.startTy(mContext, cardBeanLists.get(position).getId());
                }
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
                tyCardPresenter.getMyCardList(status, pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                tyCardPresenter.getMyCardList(status, pageNum);
            }
        });
    }


    @Override
    public void onGetCardBeanListSuccess(List<CardBean> cardBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            cardBeanLists.clear();
        }
        cardBeanLists.addAll(cardBeanList);
        if (cardBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        tyCardListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetExperienceBeanSuccess(List<ExperienceBean> experienceBeanLists, int pages) {

    }

    @Override
    public void onGetCardBeanListSuccess() {

    }

    @Override
    public void onForwardingSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
