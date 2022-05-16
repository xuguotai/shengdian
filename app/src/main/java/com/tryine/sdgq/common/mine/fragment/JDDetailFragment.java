package com.tryine.sdgq.common.mine.fragment;

import android.os.Bundle;
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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.mine.adapter.JDDetailRecordAdapter;
import com.tryine.sdgq.common.mine.adapter.TabAdapter;
import com.tryine.sdgq.common.mine.adapter.TabBtnAdapter;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.presenter.RechargPresenter;
import com.tryine.sdgq.common.mine.view.RechargeView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author: zhangshuaijun
 * @time: 2021-11-22 10:34
 */
public class JDDetailFragment extends BaseFragment implements RechargeView {

    List<String> tabBeanList = new ArrayList<String>();
    @BindView(R.id.rv_tabbtn)
    public RecyclerView rv_tabbtn;
    TabBtnAdapter tabBtnAdapter;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    @BindView(R.id.tv_goldenBean)
    TextView tv_goldenBean;
    @BindView(R.id.tv_goldenBeanobtain)
    TextView tv_goldenBeanobtain;
    @BindView(R.id.tv_goldenBeanconp)
    TextView tv_goldenBeanconp;
    @BindView(R.id.tv_goldenBean1)
    TextView tv_goldenBean1;
    @BindView(R.id.tv_goldenBeanobtain1)
    TextView tv_goldenBeanobtain1;
    @BindView(R.id.tv_goldenBeanconp1)
    TextView tv_goldenBeanconp1;

    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    List<PayRecordBean> payRecordBeanLists = new ArrayList<>();
    JDDetailRecordAdapter jdDetailRecordAdapter;

    String beanType = "0";

    String type = "全部";

    int pageNum = 1;

    RechargPresenter rechargPresenter;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_jd_detail;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        beanType = getArguments().getString("beanType");
        initViews();
        smartRefresh();

        rechargPresenter = new RechargPresenter(mContext);
        rechargPresenter.attachView(this);
        rechargPresenter.getUserWallet();
        pageNum = 1;
        rechargPresenter.getWalletList(beanType, "全部", pageNum);
    }


    protected void initViews() {
        setWhiteBar();

        tabBeanList.add("全部");
        tabBeanList.add("获得");
        tabBeanList.add("消耗");
        tabBeanList.add("转赠");
        tabBtnAdapter = new TabBtnAdapter(getContext(), tabBeanList);
        rv_tabbtn.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_tabbtn.setAdapter(tabBtnAdapter);
        tabBtnAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                tabBtnAdapter.setSelectedTabPosition(position);
                pageNum = 1;
                type = tabBeanList.get(position);
                rechargPresenter.getWalletList(beanType, tabBeanList.get(position), pageNum);
            }
        });


        jdDetailRecordAdapter = new JDDetailRecordAdapter(getContext(), payRecordBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(jdDetailRecordAdapter);
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
                rechargPresenter.getWalletList(beanType, type, pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                rechargPresenter.getWalletList(beanType, type, pageNum);
            }
        });
    }


    @Override
    public void onRechargeSuccess(String param, int payType) {

    }

    @Override
    public void onCreateOrderSuccess(String orderNo) {

    }

    @Override
    public void onGetUserbeanSuccess(int goldenBean, int silverBean) {

    }

    @Override
    public void onGetUserWalletSuccess(int goldenBean, int goldenBeanobtain, int goldenBeanconp, int silverBean, int silverBeanbtain, int silverBeanconp) {
        if ("0".equals(beanType)) {
            tv_goldenBean.setText(goldenBean + "");
            tv_goldenBeanobtain.setText(goldenBeanobtain + "");
            tv_goldenBeanconp.setText(goldenBeanconp + "");
            tv_goldenBean1.setText("当前SD金豆余额");
            tv_goldenBeanobtain1.setText("累计获得SD金豆");
            tv_goldenBeanconp1.setText("累计消耗SD金豆");
            ll_root.setBackgroundResource(R.mipmap.ic_jd_detail_bg);
        } else {
            tv_goldenBean.setText(silverBean + "");
            tv_goldenBeanobtain.setText(silverBeanbtain + "");
            tv_goldenBeanconp.setText(silverBeanconp + "");
            tv_goldenBean1.setText("当前SD银豆余额");
            tv_goldenBeanobtain1.setText("累计获得SD银豆");
            tv_goldenBeanconp1.setText("累计消耗SD银豆");
            ll_root.setBackgroundResource(R.mipmap.ic_yd_detail_bg);
        }

    }

    @Override
    public void onGetWalletListSuccess(List<PayRecordBean> payRecordBeanList, int pages) {
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
        jdDetailRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetProportionSuccess(String teaBean, String realStatus, String miniWithdraw, String serviceCharge, String proportion) {

    }

    @Override
    public void onWithdrawSuccess() {

    }

    @Override
    public void onGetProtocolSuccess(String agreement) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
