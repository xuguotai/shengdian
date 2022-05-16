package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
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
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.adapter.MineCourseChildAdapter;
import com.tryine.sdgq.common.mine.adapter.MineCourseclassAdapter;
import com.tryine.sdgq.common.mine.bean.TecheCasBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoRecordBean;
import com.tryine.sdgq.common.mine.presenter.MineCoursePresenter;
import com.tryine.sdgq.common.mine.view.MineCourseView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的课程-学生列表
 *
 * @author: zhangshuaijun
 * @time: 2022-02-19 13:32
 */
public class MineCourseChildListActivity extends BaseActivity implements MineCourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    MineCoursePresenter mineCoursePresenter;
    MineCourseChildAdapter mineCourseChildAdapter;

    List<TecheCasinfoBean> techeCasBeanLists = new ArrayList<>();

    int pageNum = 1;
    String catsId;

    public static void start(Context context, String catsId) {
        Intent intent = new Intent();
        intent.setClass(context, MineCourseChildListActivity.class);
        intent.putExtra("catsId", catsId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_minecoursechild;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("我的课程-学生列表");
        catsId = getIntent().getStringExtra("catsId");
        mineCoursePresenter = new MineCoursePresenter(mContext);
        mineCoursePresenter.attachView(this);
        mineCoursePresenter.gettecheCasinfolist(pageNum, catsId);

        mineCourseChildAdapter = new MineCourseChildAdapter(this, techeCasBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(mineCourseChildAdapter);
        mineCourseChildAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AttendClassRecordActivity.start(mContext, techeCasBeanLists.get(position));
            }
        });

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
                mineCoursePresenter.gettecheCasinfolist(pageNum, catsId);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                mineCoursePresenter.gettecheCasinfolist(pageNum, catsId);
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
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            techeCasBeanLists.clear();
        }
        techeCasBeanLists.addAll(techeCasinfoBeans);
        if (techeCasBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        mineCourseChildAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetRecordListSuccess(List<TecheCasinfoRecordBean> techeCasinfoRecordBeans, int pages) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
