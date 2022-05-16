package com.tryine.sdgq.common.circle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.adapter.CircleItemAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.presenter.CirclePresenter;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子搜索搜索
 *
 * @author: zhangshuaijun
 * @time: 2021-12-31 13:11
 */
public class CircleSearchFragment extends BaseFragment implements CircleView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_goods)
    RecyclerView rc_circleData;

    List<CircleBean> circleBeanLists = new ArrayList<>();
    CircleItemAdapter circleItemAdapter;

    int pageNum = 1;

    CirclePresenter circlePresenter;

    String searchStr;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CircleSearchFragment.class);
        context.startActivity(intent);
    }

    @Override
    public int getlayoutId() {
        return R.layout.fragment_search_circle;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefresh();
        circlePresenter = new CirclePresenter(mContext);
        circlePresenter.attachView(this);
        initViews();
    }


    public void setSearchStr(String searchStr){
        if(!TextUtils.isEmpty(searchStr)) {
            this.searchStr = searchStr;
            pageNum = 1;
            circlePresenter.getCircleList("0", "1", "", pageNum, searchStr);
        }
    }

    protected void initViews() {

        circleItemAdapter = new CircleItemAdapter(mContext, circleBeanLists);
        rc_circleData.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rc_circleData.setAdapter(circleItemAdapter);
        circleItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CircleDetailActivity.start(mContext, circleBeanLists.get(position).getId());
            }
        });

    }

    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setEnableRefresh(false);
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(!TextUtils.isEmpty(searchStr)) {
                    pageNum++;
                    circlePresenter.getCircleList("0", "1", "", pageNum, searchStr);
                }
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

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


}
