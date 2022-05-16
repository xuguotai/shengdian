package com.tryine.sdgq.common.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mall.adapter.GoodsCommentAdapter;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.bean.GoodsCommentBean;
import com.tryine.sdgq.common.mall.presenter.GoodsDetailPresenter;
import com.tryine.sdgq.common.mall.view.GoodsDetailView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-03-17 13:37
 */
public class GoodsCommentListActivity extends BaseActivity implements GoodsDetailView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<GoodsCommentBean> goodsCommentBeans = new ArrayList<>();
    GoodsDetailPresenter goodsDetailPresenter;
    int pageNum = 1;

    GoodsCommentAdapter goodsCommentAdapter;

    String goodsId;

    public static void start(Context context, String goodsId) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsCommentListActivity.class);
        intent.putExtra("goodsId", goodsId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goodscomment_list;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("全部评价");

        goodsId = getIntent().getStringExtra("goodsId");

        goodsDetailPresenter = new GoodsDetailPresenter(this);
        goodsDetailPresenter.attachView(this);
        goodsDetailPresenter.getcommentslist(goodsId, pageNum);


        goodsCommentAdapter = new GoodsCommentAdapter(this, goodsCommentBeans);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(goodsCommentAdapter);

    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
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
                goodsDetailPresenter.getcommentslist(goodsId, pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                goodsDetailPresenter.getcommentslist(goodsId, pageNum);
            }
        });
    }


    @Override
    public void onGetGoodsDetailSuccess(GoodsBean goodsBean) {

    }

    @Override
    public void onGetGoodsCommentListSuccess(List<GoodsCommentBean> goodsCommentBean, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            goodsCommentBeans.clear();
        }
        goodsCommentBeans.addAll(goodsCommentBean);
        if (goodsCommentBeans.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        goodsCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onaddCarSuccess() {

    }

    @Override
    public void onGiveSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
