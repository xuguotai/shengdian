package com.tryine.sdgq.common.circle.fragment;

import android.os.Bundle;
import android.view.View;
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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.activity.TopicDetailActivity;
import com.tryine.sdgq.common.circle.adapter.TopicListAdapter;
import com.tryine.sdgq.common.circle.adapter.VideoItemAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.bean.PersonalBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.presenter.PersonalPresenter;
import com.tryine.sdgq.common.circle.view.PersonalView;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 个人主页-话题
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class PersonalPageTopicFragment extends BaseFragment implements PersonalView {


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<TopicBean> topicBeanLists = new ArrayList<>();
    TopicListAdapter topicListAdapter;

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
        personalPresenter.getPersonalTopicList(pageNum, selectType, userId);

        initViews();
    }

    protected void initViews() {
        topicListAdapter = new TopicListAdapter(getContext(), topicBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(topicListAdapter);
        topicListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TopicDetailActivity.start(mContext,topicBeanLists.get(position).getId());
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
                personalPresenter.getPersonalTopicList(pageNum, selectType, userId);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                personalPresenter.getPersonalTopicList(pageNum, selectType, userId);
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

    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {

    }

    @Override
    public void onGetTopicBeanListSuccess(List<TopicBean> topicBeanList, int pages) {
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
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        topicListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetVideoBeanListSuccess(List<VideoModel> videoModels, int pages) {

    }

    @Override
    public void onGiveSuccess(String type, int position) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
