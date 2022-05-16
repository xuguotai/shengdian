package com.tryine.sdgq.common.mall.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.AddCircleActivity;
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.adapter.CircleItemAdapter;
import com.tryine.sdgq.common.circle.adapter.CircleTabBtnAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.presenter.BannerPresenter;
import com.tryine.sdgq.common.circle.presenter.CirclePresenter;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.activity.GoodsSearchActivity;
import com.tryine.sdgq.common.mall.activity.MallCartActivity;
import com.tryine.sdgq.common.mall.activity.MallTypeListActivity;
import com.tryine.sdgq.common.mall.adapter.MallGoodsAdapter;
import com.tryine.sdgq.common.mall.adapter.MallXlGoodsAdapter;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.presenter.MallHomePresenter;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.banner.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品
 *
 * @author：qujingfeng
 * @time：2020.08.05 11:14
 */


public class GoodTypeFragment extends BaseFragment implements MallHomeView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_goodData)
    RecyclerView rc_goodData;
    List<GoodsBean> goodsBeanLists = new ArrayList<>();
    CommonAdapter goodsAdapter;

    MallHomePresenter mallHomePresenter;

    int pageNum = 1;
    String status = "0";

    @Override
    public int getlayoutId() {
        return R.layout.fragment_goodtype;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWhiteBar();
        smartRefresh();
        initViews();
    }

    private void initViews() {
        status = getArguments().getString("status");

        mallHomePresenter = new MallHomePresenter(mContext);
        mallHomePresenter.attachView(this);
        slRefreshLayout.autoRefresh();


        goodsAdapter = new CommonAdapter(mContext, R.layout.item_good_type, goodsBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                GoodsBean data = goodsBeanLists.get(position);
                if (!TextUtils.isEmpty(data.getImgUrl())) {
                    GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl().split("!#!")[0], holder.getView(R.id.iv_cover));
                } else {
                    GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl(), holder.getView(R.id.iv_cover));
                }

                holder.setText(R.id.tv_goodName, data.getName());
                ImageView iv_price = holder.getView(R.id.iv_price);

                if (data.getBeanType() == 0) {
                    iv_price.setBackgroundResource(R.mipmap.ic_jdz);
                    holder.setText(R.id.tv_price, data.getMarketPrice() + " 金豆");
                } else {
                    iv_price.setBackgroundResource(R.mipmap.ic_ydz);
                    holder.setText(R.id.tv_price, data.getMarketPrice() + " 银豆");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoodsDetailActivity.start(mContext, goodsBeanLists.get(position).getId());
                    }
                });

            }
        };
        rc_goodData.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rc_goodData.setAdapter(goodsAdapter);


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
                if (status.equals("0")) {
                    mallHomePresenter.getproductlist(pageNum);
                } else if (status.equals("1")) {
                    mallHomePresenter.getcakeslist(pageNum);
                } else if (status.equals("2")) {
                    mallHomePresenter.getpreferentiallist(pageNum);
                }

            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                if (status.equals("0")) {
                    mallHomePresenter.getproductlist(pageNum);
                } else if (status.equals("1")) {
                    mallHomePresenter.getcakeslist(pageNum);
                } else if (status.equals("2")) {
                    mallHomePresenter.getpreferentiallist(pageNum);
                }
            }
        });
    }


    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

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
            rc_goodData.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_goodData.setVisibility(View.VISIBLE);
        }
        goodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList) {

    }

    @Override
    public void onFailed(String message) {

    }
}