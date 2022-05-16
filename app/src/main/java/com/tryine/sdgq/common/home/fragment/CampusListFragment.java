package com.tryine.sdgq.common.home.fragment;

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
import com.tryine.sdgq.common.home.activity.CampusDetailActivity;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.adapter.CampusItemAdapter;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.presenter.CampusPresenter;
import com.tryine.sdgq.common.home.view.CampusView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 校区展示列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:13
 */
public class CampusListFragment extends BaseFragment implements CampusView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    List<CampusBean> campusBeanLists = new ArrayList<>();
    CampusItemAdapter campusItemAdapter;

    CampusPresenter campusPresenter;
    int pageNum = 1;

    String city;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_campus_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    protected void initViews() {
        setWhiteBar();
        smartRefresh();
        city = getArguments().getString("city");

        campusPresenter = new CampusPresenter(mContext);
        campusPresenter.attachView(this);
        campusPresenter.getFicationList(pageNum, SPUtils.getConfigString(Parameter.LOCATION,""), city, "");

        campusItemAdapter = new CampusItemAdapter(getContext(), campusBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(campusItemAdapter);
        campusItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CampusDetailActivity.start(mContext, campusBeanLists.get(position));
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
                campusPresenter.getFicationList(pageNum, SPUtils.getConfigString(Parameter.LOCATION,""), city, "");
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                campusPresenter.getFicationList(pageNum, SPUtils.getConfigString(Parameter.LOCATION,""), city, "");
            }
        });
    }


    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            campusBeanLists.clear();
        }
        campusBeanLists.addAll(campusBeanList);
        if (campusBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        campusItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
