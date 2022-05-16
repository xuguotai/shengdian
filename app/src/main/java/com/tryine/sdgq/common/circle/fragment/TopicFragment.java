package com.tryine.sdgq.common.circle.fragment;

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
import com.tryine.sdgq.common.circle.activity.TopicDetailActivity;
import com.tryine.sdgq.common.circle.adapter.TopicListAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.presenter.BannerPresenter;
import com.tryine.sdgq.common.circle.presenter.TopicPresenter;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.circle.view.TopicView;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.mall.adapter.MallGoodsAdapter;
import com.tryine.sdgq.common.mine.adapter.JDDetailRecordAdapter;
import com.tryine.sdgq.util.AdvertisingJumpUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 琴友圈-话题
 *
 * @author: zhangshuaijun
 * @time: 2021-11-23 17:53
 */
public class TopicFragment extends BaseFragment implements BannerView, TopicView {


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    @BindView(R.id.banner)
    BannerViewPager bannerView;

    @BindView(R.id.ll_ry)
    LinearLayout ll_ry;
    @BindView(R.id.rc_topicData)
    RecyclerView rc_topicData;
    List<TopicBean> topicBeanLists = new ArrayList<>();
    TopicListAdapter topicListAdapter;

    BannerPresenter bannerPresenter;
    TopicPresenter topicPresenter;
    int pageNum = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_topic;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bannerPresenter = new BannerPresenter(mContext);
        bannerPresenter.attachView(this);
        bannerPresenter.getBannerList(4);

        topicPresenter = new TopicPresenter(mContext);
        topicPresenter.attachView(this);
        topicPresenter.getTopicList(pageNum);
        initViews();
        smartRefresh();
    }

    private void initViews() {
        topicListAdapter = new TopicListAdapter(getContext(), topicBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_topicData.setLayoutManager(lin);
        rc_topicData.setAdapter(topicListAdapter);
        topicListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TopicDetailActivity.start(mContext,topicBeanLists.get(position).getId());
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

    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                topicPresenter.getTopicList(pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                topicPresenter.getTopicList(pageNum);
            }
        });
    }

    @Override
    public void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList) {
        if (null != bannerBeanList && bannerBeanList.size() > 0) {
            initBanner(bannerBeanList);
        }
    }

    @Override
    public void onGetTopicDetailSuccess(TopicBean topicBean) {

    }

    @Override
    public void onGetTopicListSuccess(List<TopicBean> topicBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            topicBeanLists.clear();
        }
        topicBeanLists.addAll(topicBeanList);
        if (topicBeanLists.size() == 0) {
            ll_ry.setVisibility(View.GONE);
            rc_topicData.setVisibility(View.GONE);
        } else {
            ll_ry.setVisibility(View.VISIBLE);
            rc_topicData.setVisibility(View.VISIBLE);
        }
        topicListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
