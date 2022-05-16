package com.tryine.sdgq.common.mine.activity;

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
import com.tryine.sdgq.common.mine.adapter.PayRecordAdapter;
import com.tryine.sdgq.common.mine.adapter.RewardAdapter;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.common.mine.presenter.RewarPresenter;
import com.tryine.sdgq.common.mine.view.RewarView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.DateDialog;
import com.tryine.sdgq.view.dialog.DatePickerDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付记录
 *
 * @author: zhangshuaijun
 * @time: 2022-01-11 13:58
 */
public class PayRecordListActivity extends BaseActivity implements RewarView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    @BindView(R.id.tv_date)
    TextView tv_date;

    List<PayRecordBean> payRecordBeanLists = new ArrayList<>();
    PayRecordAdapter payRecordAdapter;

    int pageNum = 1;
    String searchDate = "";

    RewarPresenter rewarPresenter;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PayRecordListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_list_payrecord;
    }

    @Override
    protected void init() {
        tv_title.setText("支付记录");
        setWhiteBar();
        smartRefresh();
        initViews();
        rewarPresenter = new RewarPresenter(mContext);
        rewarPresenter.attachView(this);
        slRefreshLayout.autoRefresh();
    }

    protected void initViews() {
        payRecordAdapter = new PayRecordAdapter(this, payRecordBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(payRecordAdapter);
    }

    @OnClick({R.id.iv_black, R.id.ll_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_date:
                showDatePickerDialog();
                break;
        }
    }

    /**
     * 时间选择dialog
     **/
    private void showDatePickerDialog() {
        DateDialog dateDialog = new DateDialog(this);
        dateDialog.show();
        dateDialog.setSelectDateListener(new DateDialog.OnSelectDateListener() {
            @Override
            public void getDate(String date) {
                tv_date.setText(date);
                searchDate = date;
                slRefreshLayout.autoRefresh();
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
                rewarPresenter.getPayList(pageNum, searchDate);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                rewarPresenter.getPayList(pageNum, searchDate);
            }
        });
    }


    @Override
    public void onGetRewarBeanListSuccess(List<RewarBean> rewarBeanList, int pages) {

    }

    @Override
    public void onGetPayRecordBeanListSuccess(List<PayRecordBean> payRecordBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            payRecordBeanLists.clear();
        }
        payRecordBeanLists.addAll(payRecordBeanList);
        if (payRecordBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        payRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
