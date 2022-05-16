package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.AddCircleActivity;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.mine.adapter.CollectCircleAdapter;
import com.tryine.sdgq.common.mine.adapter.TaskAdapter;
import com.tryine.sdgq.common.mine.bean.KtzlBean;
import com.tryine.sdgq.common.mine.bean.TaskBean;
import com.tryine.sdgq.common.mine.presenter.TaskPresenter;
import com.tryine.sdgq.common.mine.view.TaskView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 任务广场
 *
 * @author: zhangshuaijun
 * @time: 2021-12-03 16:54
 */
public class TaskHomeActivity extends BaseActivity implements TaskView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    List<TaskBean> taskBeanLists = new ArrayList<>();
    TaskAdapter taskAdapter;

    TaskPresenter taskPresenter;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TaskHomeActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_taskhome;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("任务广场");

        taskPresenter = new TaskPresenter(this);
        taskPresenter.attachView(this);
        taskPresenter.getTaskList();

        taskAdapter = new TaskAdapter(this, taskBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(taskAdapter);
        taskAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskBean data = taskBeanLists.get(position);
                if (data.getName().contains("发帖")) {
                    if (data.getTaskStatus().equals("0")) {
                        AddCircleActivity.start(mContext);
                    } else if (data.getTaskStatus().equals("1")) {
                        taskPresenter.receive(data.getId());
                    }
                } else if (data.getName().contains("分享")) {
                    if (data.getTaskStatus().equals("0")) {

                    } else if (data.getTaskStatus().equals("1")) {
                        taskPresenter.receive(data.getId());
                    }
                } else if (data.getName().contains("赞")) {
                    if (data.getTaskStatus().equals("0")) {
                        AddCircleActivity.start(mContext);
                    } else if (data.getTaskStatus().equals("1")) {
                        taskPresenter.receive(data.getId());
                    }
                } else if (data.getName().contains("签到")) {
                    if (data.getTaskStatus().equals("0")) {
                        taskPresenter.signin();
                    } else if (data.getTaskStatus().equals("1")) {
                        taskPresenter.receive(data.getId());
                    }
                } else if (data.getName().contains("邀请")) {
                    if (data.getTaskStatus().equals("0")) {
                    } else if (data.getTaskStatus().equals("1")) {
                        taskPresenter.receive(data.getId());
                    }
                }


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
    protected void onRestart() {
        super.onRestart();
        taskPresenter.getTaskList();
    }

    @Override
    public void onGetTaskBeanListSuccess(List<TaskBean> taskBeanLists) {
        this.taskBeanLists.clear();
        this.taskBeanLists.addAll(taskBeanLists);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSigninSuccess() {
        ToastUtil.toastLongMessage("签到成功");
        taskPresenter.getTaskList();
    }

    @Override
    public void onReceiveSuccess() {
        ToastUtil.toastLongMessage("领取成功");
        taskPresenter.getTaskList();
    }

    @Override
    public void onGetContinuesignSuccess(int count) {

    }

    @Override
    public void onGetKtzlBeanSuccess(List<KtzlBean> ktzlBeanList, int pages) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
