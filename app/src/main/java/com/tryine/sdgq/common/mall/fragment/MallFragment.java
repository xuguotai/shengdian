package com.tryine.sdgq.common.mall.fragment;


import android.os.Bundle;
import android.view.View;

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
import com.tryine.sdgq.common.circle.presenter.BannerPresenter;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
import com.tryine.sdgq.common.home.adapter.HomeMenuAdapter;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.presenter.HomePresenter;
import com.tryine.sdgq.common.mall.activity.GoodTypeListHomeActivity;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.activity.GoodsSearchActivity;
import com.tryine.sdgq.common.mall.activity.MallCartActivity;
import com.tryine.sdgq.common.mall.activity.MallTypeListActivity;
import com.tryine.sdgq.common.mall.adapter.MallGoodsAdapter;
import com.tryine.sdgq.common.mall.adapter.MallXlGoodsAdapter;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.presenter.MallHomePresenter;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.util.AdvertisingJumpUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商城
 * @author：qujingfeng
 * @time：2020.08.05 11:14
 */


public class MallFragment extends BaseFragment implements BannerView, MallHomeView {

    @BindView(R.id.banner)
    BannerViewPager bannerView;

    @BindView(R.id.rv_xlgoods)
    RecyclerView rv_xlgoods;
    List<GoodsBean> xlgoodsList = new ArrayList<>();
    MallXlGoodsAdapter mallXlGoodsAdapter;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;
    List<GoodsBean> goodsLists = new ArrayList<>();
    MallGoodsAdapter mallGoodsAdapter;

    BannerPresenter bannerPresenter;

    MallHomePresenter mallHomePresenter;

    int pageNum = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_mall;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWhiteBar();
        smartRefresh();
        bannerPresenter = new BannerPresenter(mContext);
        bannerPresenter.attachView(this);
        bannerPresenter.getBannerList(3);

        mallHomePresenter = new MallHomePresenter(mContext);
        mallHomePresenter.attachView(this);
        mallHomePresenter.selectlimitgoodslist();
        mallHomePresenter.selectgoodslist(pageNum);

        initViews();
    }


    private void initViews(){
        mallGoodsAdapter = new MallGoodsAdapter(getContext(), goodsLists);
        rv_goods.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_goods.setAdapter(mallGoodsAdapter);
        mallGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsDetailActivity.start(mContext, goodsLists.get(position).getId());
            }
        });
    }


    private void initBanner(List<BannerBean> bannerBeanList) {
        bannerView.initBanner(bannerBeanList, false)//关闭3D画廊效果
                .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addStartTimer(bannerBeanList.size() > 1 ? 5 : 10000)//自动轮播5秒间隔
                .finishConfig()//这句必须加
                .addPoint(bannerBeanList.size())//添加指示器
                .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                        BannerBean bannerBean = bannerBeanList.get(position);
                        AdvertisingJumpUtils.advertisingJump(getActivity(), bannerBean);
                    }
                });
    }


    @OnClick({R.id.tv_wc,R.id.tv_yq,R.id.tv_fw,R.id.tv_bh,R.id.iv_cart,R.id.ll_search_page,R.id.iv_xp,R.id.iv_rx,R.id.iv_th})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cart:
                MallCartActivity.start(getContext());
                break;
            case R.id.tv_wc:
                MallTypeListActivity.start(getContext(),0);
                break;
            case R.id.tv_yq:
                MallTypeListActivity.start(getContext(),1);
                break;
            case R.id.tv_fw:
                MallTypeListActivity.start(getContext(),2);
                break;
            case R.id.tv_bh:
                MallTypeListActivity.start(getContext(),3);
                break;
            case R.id.ll_search_page:
                GoodsSearchActivity.start(getContext());
                break;
            case R.id.iv_xp:
                GoodTypeListHomeActivity.start(getContext(),0);
                break;
            case R.id.iv_rx:
                GoodTypeListHomeActivity.start(getContext(),1);
                break;
            case R.id.iv_th:
                GoodTypeListHomeActivity.start(getContext(),2);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
                mallHomePresenter.selectgoodslist(pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                mallHomePresenter.selectgoodslist(pageNum);
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setWhiteBar();
        }

    }

    @Override
    public void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList) {
        if (null != bannerBeanList && bannerBeanList.size() > 0) {
            initBanner(bannerBeanList);
        }
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
            goodsLists.clear();
        }
        goodsLists.addAll(goodsBeanList);
        mallGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList) {
        this.xlgoodsList.clear();
        this.xlgoodsList.addAll(goodsBeanList);

        mallXlGoodsAdapter = new MallXlGoodsAdapter(getContext(), xlgoodsList);
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
        rv_xlgoods.setLayoutManager(lin);
        rv_xlgoods.setAdapter(mallXlGoodsAdapter);
        mallXlGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsDetailActivity.start(mContext, xlgoodsList.get(position).getId());
            }
        });

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
