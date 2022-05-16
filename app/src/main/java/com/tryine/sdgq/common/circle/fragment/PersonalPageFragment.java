package com.tryine.sdgq.common.circle.fragment;

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
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.adapter.PersonalCircleItemAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.bean.PersonalBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.presenter.PersonalPresenter;
import com.tryine.sdgq.common.circle.view.PersonalView;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 个人主页-主页
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class PersonalPageFragment extends BaseFragment implements PersonalView {


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<CircleBean> circleBeanLists = new ArrayList<>();
    PersonalCircleItemAdapter personalCircleItemAdapter;

    PersonalPresenter personalPresenter;
    int pageNum = 1;
    String userId;
    String selectType;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_collect_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWhiteBar();
        smartRefresh();

        userId = getArguments().getString("userId");
        selectType = getArguments().getString("selectType");

        personalPresenter = new PersonalPresenter(mContext);
        personalPresenter.attachView(this);
        personalPresenter.getPersonalCircleList(pageNum, selectType, userId);

        initViews();
    }

    protected void initViews() {

        String type;
        if (SPUtils.getString(Parameter.USER_ID).equals(userId)) {
            type = "0";
        } else {
            type = "1";
        }
        personalCircleItemAdapter = new PersonalCircleItemAdapter(getContext(), circleBeanLists, type);
        rc_data.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rc_data.setAdapter(personalCircleItemAdapter);
        personalCircleItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_delete) {
                    PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                            "是否确认删除", "确定", "再想想");
                    promptDialog.show();
                    promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                        @Override
                        public void insure() {
                            personalPresenter.deletepyq(circleBeanLists.get(position).getId());
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                } if (view.getId() == R.id.tv_giveCount) {
                    personalPresenter.setGive(circleBeanLists.get(position).getId(), circleBeanLists.get(position).getIsGive(), position);
                } else  if (view.getId() == R.id.ll_root){
                    CircleDetailActivity.start(mContext, circleBeanLists.get(position).getId());
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
                personalPresenter.getPersonalCircleList(pageNum, selectType, userId);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                personalPresenter.getPersonalCircleList(pageNum, selectType, userId);
            }
        });
    }


    @Override
    public void onGetPersonaBeanSuccess(PersonalBean personalBean) {

    }

    @Override
    public void onGetLabelListSuccess(List<LabelBean> labelBeanList) {

    }

    @Override
    public void onUpdateUserInfoSuccess() {

    }

    @Override
    public void onUpdateLabelSuccess() {

    }

    @Override
    public void onFocusSuccess() {

    }

    @Override
    public void onDeletePyqSuccess() {
        ToastUtil.toastLongMessage("删除成功");
        pageNum = 1;
        personalPresenter.getPersonalCircleList(pageNum, selectType, userId);
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
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        personalCircleItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetTopicBeanListSuccess(List<TopicBean> topicBeanList, int pages) {

    }

    @Override
    public void onGetVideoBeanListSuccess(List<VideoModel> videoModels, int pages) {

    }

    @Override
    public void onGiveSuccess(String type, int position) {
        if (type.equals("1")) {
            circleBeanLists.get(position).setGiveCount(Integer.parseInt(circleBeanLists.get(position).getGiveCount()) + 1 + "");
        } else {
            circleBeanLists.get(position).setGiveCount(Integer.parseInt(circleBeanLists.get(position).getGiveCount()) - 1 + "");
        }

        circleBeanLists.get(position).setIsGive(type);
        personalCircleItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
