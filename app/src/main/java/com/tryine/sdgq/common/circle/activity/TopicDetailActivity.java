package com.tryine.sdgq.common.circle.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.adapter.CircleItemAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.presenter.TopicPresenter;
import com.tryine.sdgq.common.circle.view.TopicView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 话题详情
 *
 * @author: zhangshuaijun
 * @time: 2021-11-23 18:36
 */
public class TopicDetailActivity extends BaseActivity implements TopicView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title1)
    TextView tv_title1;
    @BindView(R.id.tv_topicDesc)
    TextView tv_topicDesc;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_topicData)
    RecyclerView rc_topicData;
    List<CircleBean> circleBeanLists = new ArrayList<>();
    CircleItemAdapter circleItemAdapter;

    TopicPresenter topicPresenter;
    String id;//话题id
    int pageNum = 1;

    TopicBean topicBean;

    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, TopicDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic_dateil;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("话题详情");
        id = getIntent().getStringExtra("id");
        topicPresenter = new TopicPresenter(this);
        topicPresenter.attachView(this);
        topicPresenter.getTopicDetail(id);
        topicPresenter.getCircleList(id, pageNum);

        initViews();
    }


    private void initViews() {
        circleItemAdapter = new CircleItemAdapter(this, circleBeanLists);
        rc_topicData.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rc_topicData.setAdapter(circleItemAdapter);
        circleItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CircleDetailActivity.start(mContext, circleBeanLists.get(position).getId());
            }
        });

    }

    @OnClick({R.id.iv_black, R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_add:
                if (null != topicBean) {
                    AddCircleActivity.start(mContext, topicBean);
                }else{
                    AddCircleActivity.start(mContext);
                }
                break;
        }
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
                topicPresenter.getCircleList(id, pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                topicPresenter.getCircleList(id, pageNum);
            }
        });
    }

    @Override
    public void onGetTopicDetailSuccess(TopicBean topicBean) {
        this.topicBean = topicBean;
        tv_title1.setText(topicBean.getName());
        tv_topicDesc.setText(topicBean.getTopicDesc());

    }

    @Override
    public void onGetTopicListSuccess(List<TopicBean> topicBeanList, int pages) {

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
            rc_topicData.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_topicData.setVisibility(View.VISIBLE);
        }
        circleItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
