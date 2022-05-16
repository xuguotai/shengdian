package com.tryine.sdgq.common.mine.fragment;

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
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mine.adapter.CollectVideoAdapter;
import com.tryine.sdgq.common.mine.adapter.CollectgoodAdapter;
import com.tryine.sdgq.common.mine.presenter.CollectPresenter;
import com.tryine.sdgq.common.mine.view.CollectView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的收藏-商品
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class CollectGoodFragment extends BaseFragment implements CollectView {


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<GoodsBean> goodsBeanLists = new ArrayList<>();
    CollectgoodAdapter collectgoodAdapter;

    CollectPresenter collectPresenter;

    int pageNum = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_collect_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        smartRefresh();
        collectPresenter = new CollectPresenter(mContext);
        collectPresenter.attachView(this);
        collectPresenter.getCollectGoodList(pageNum);
    }

    protected void initViews() {
        setWhiteBar();
        collectgoodAdapter = new CollectgoodAdapter(getContext(), goodsBeanLists);
        rc_data.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rc_data.setAdapter(collectgoodAdapter);
        collectgoodAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.tv_sc){
                    collectPresenter.setCollectGood(goodsBeanLists.get(position).getId());
                }else{
                    GoodsDetailActivity.start(mContext,goodsBeanLists.get(position).getId());
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
                collectPresenter.getCollectGoodList(pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                collectPresenter.getCollectGoodList(pageNum);
            }
        });
    }


    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {

    }

    @Override
    public void onGetVideoModelListSuccess(List<VideoModel> circleBeanList, int pages) {

    }

    @Override
    public void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            goodsBeanLists.clear();
        }
        goodsBeanLists.addAll(goodsBeanList);
        if (goodsBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        collectgoodAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCollectSuccess() {
        ToastUtil.toastLongMessage("取消成功");
        pageNum = 1;
        collectPresenter.getCollectGoodList(pageNum);
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
