package com.tryine.sdgq.common.home.fragment;

import android.os.Bundle;
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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.home.activity.BargainOrderDetailActivity;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.BargainOrderDetailBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.presenter.BargainPresenter;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.CampusDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 砍价专区-线下课程
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class BargainOfflineFragment extends BaseFragment implements BargainView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    @BindView(R.id.tv_qhxq)
    TextView tv_qhxq;

    List<CampusBean> campusBeanLists = new ArrayList<>();
    CampusBean selectCampusBean;//选中的校区

    List<BargainBean> bargainBeanLists = new ArrayList<>();
    HomeBargainAdapter homeBargainAdapter;

    BargainPresenter bargainPresenter;

    int pageNum = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_bargainoffline_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefresh();
        initViews();
    }

    protected void initViews() {
        setWhiteBar();

        bargainPresenter = new BargainPresenter(mContext);
        bargainPresenter.attachView(this);
        bargainPresenter.getBargainCampusList();

        homeBargainAdapter = new HomeBargainAdapter(getContext(), bargainBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(homeBargainAdapter);
        homeBargainAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                bargainPresenter.saveorder(bargainBeanLists.get(position).getId(),"1");
            }
        });

    }

    @OnClick({R.id.ll_xq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_xq:
                if (null != campusBeanLists) {
                    CampusDialog campusDialog = new CampusDialog(getActivity(), campusBeanLists);
                    campusDialog.show();
                    campusDialog.setOnItemClickListener(new CampusDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(CampusBean homeMenuBean) {
                            selectCampusBean = homeMenuBean;
                            tv_qhxq.setText(homeMenuBean.getName());
                            pageNum = 1;
                            bargainPresenter.getBargainAreaList(pageNum, selectCampusBean.getId(), "1");
                        }
                    });
                }
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
                if (null != selectCampusBean) {
                    bargainPresenter.getBargainAreaList(pageNum, selectCampusBean.getId(), "1");
                }

            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                if (null != selectCampusBean) {
                    bargainPresenter.getBargainAreaList(pageNum, selectCampusBean.getId(), "1");
                }
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
        homeBargainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCampusBeanSuccess(List<CampusBean> campusBeanList) {
        this.campusBeanLists = campusBeanList;
        //默认取第一个校区
        if (null != campusBeanLists && campusBeanLists.size() > 0) {
            selectCampusBean = campusBeanLists.get(0);
            tv_qhxq.setText(selectCampusBean.getName());
            pageNum = 1;
            bargainPresenter.getBargainAreaList(pageNum, selectCampusBean.getId(), "1");
        }
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
