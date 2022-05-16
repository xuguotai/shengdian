package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.adapter.MineCourseclassAdapter;
import com.tryine.sdgq.common.mine.adapter.RewardAdapter;
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
 * 我的课程
 * @author: zhangshuaijun
 * @time: 2022-02-19 13:32
 */
public class MineCourseListActivity extends BaseActivity implements MineCourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    MineCoursePresenter mineCoursePresenter;
    MineCourseclassAdapter mineCourseclassAdapter;

    List<TecheCasBean> techeCasBeanLists = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MineCourseListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_minecourse;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("我的课程");
        mineCoursePresenter = new MineCoursePresenter(mContext);
        mineCoursePresenter.attachView(this);
        mineCoursePresenter.gettecheCaslist();

        mineCourseclassAdapter = new MineCourseclassAdapter(this, techeCasBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(mineCourseclassAdapter);
        mineCourseclassAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MineCourseChildListActivity.start(mContext,techeCasBeanLists.get(position).getId());
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
        techeCasBeanLists.clear();
        techeCasBeanLists.addAll(techeCasBeans);
        mineCourseclassAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetTecheCasinfoBeanListSuccess(List<TecheCasinfoBean> techeCasinfoBeans, int pages) {

    }

    @Override
    public void onGetRecordListSuccess(List<TecheCasinfoRecordBean> techeCasinfoRecordBeans, int pages) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
