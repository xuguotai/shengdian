package com.tryine.sdgq.common.mine.activity;

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
import com.tryine.sdgq.common.mine.adapter.MineCourseChildRecordAdapter;
import com.tryine.sdgq.common.mine.bean.TecheCasBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoRecordBean;
import com.tryine.sdgq.common.mine.presenter.MineCoursePresenter;
import com.tryine.sdgq.common.mine.view.MineCourseView;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.roundImageView.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-02-19 14:32
 */
public class AttendClassRecordActivity extends BaseActivity implements MineCourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_head)
    RoundImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    int pageNum = 1;

    TecheCasinfoBean techeCasinfoBean;
    MineCoursePresenter mineCoursePresenter;
    MineCourseChildRecordAdapter mineCourseChildRecordAdapter;

    List<TecheCasinfoRecordBean> techeCasinfoRecordBeanLists = new ArrayList<>();

    public static void start(Context context, TecheCasinfoBean techeCasinfoBean) {
        Intent intent = new Intent();
        intent.setClass(context, AttendClassRecordActivity.class);
        intent.putExtra("techeCasinfoBean", techeCasinfoBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_attendclassrecord;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("上课记录");
        mineCoursePresenter = new MineCoursePresenter(mContext);
        mineCoursePresenter.attachView(this);
        techeCasinfoBean = (TecheCasinfoBean) getIntent().getSerializableExtra("techeCasinfoBean");
        initViews();
        mineCoursePresenter.gettecheCasinfodetial(pageNum, techeCasinfoBean.getUserId());

    }

    private void initViews() {
        GlideEngine.createGlideEngine().loadImage(mContext, techeCasinfoBean.getImgUrl()
                , iv_head);
        tv_name.setText(techeCasinfoBean.getName());

        mineCourseChildRecordAdapter = new MineCourseChildRecordAdapter(this, techeCasinfoRecordBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(mineCourseChildRecordAdapter);
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
                mineCoursePresenter.gettecheCasinfodetial(pageNum, techeCasinfoBean.getUserId());
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                mineCoursePresenter.gettecheCasinfodetial(pageNum, techeCasinfoBean.getUserId());
            }
        });
    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }


    @Override
    public void onGetTecheCasBeanListSuccess(List<TecheCasBean> techeCasBeans, int pages) {

    }

    @Override
    public void onGetTecheCasinfoBeanListSuccess(List<TecheCasinfoBean> techeCasinfoBeans, int pages) {

    }

    @Override
    public void onGetRecordListSuccess(List<TecheCasinfoRecordBean> techeCasinfoRecordBeans, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            techeCasinfoRecordBeanLists.clear();
        }
        techeCasinfoRecordBeanLists.addAll(techeCasinfoRecordBeans);
        if (techeCasinfoRecordBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        mineCourseChildRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
