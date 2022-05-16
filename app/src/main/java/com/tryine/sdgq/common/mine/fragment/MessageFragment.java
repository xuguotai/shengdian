package com.tryine.sdgq.common.mine.fragment;

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
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mine.activity.MessageDetailActivity;
import com.tryine.sdgq.common.mine.adapter.CollectVideoAdapter;
import com.tryine.sdgq.common.mine.adapter.MessageAdapter;
import com.tryine.sdgq.common.mine.bean.MessageBean;
import com.tryine.sdgq.common.mine.presenter.CollectPresenter;
import com.tryine.sdgq.common.mine.presenter.MessagePresenter;
import com.tryine.sdgq.common.mine.view.CollectView;
import com.tryine.sdgq.common.mine.view.MessageView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的消息
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class MessageFragment extends BaseFragment implements MessageView {


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<MessageBean> messageBeanLists = new ArrayList<>();
    MessageAdapter messageAdapter;

    MessagePresenter messagePresenter;

    int pageNum = 1;

    int resourcesType = 0;//0-系统消息 1-点赞消息 2-分享消息 3-评论消息 4-打赏消息'

    @Override
    public int getlayoutId() {
        return R.layout.fragment_message_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resourcesType = getArguments().getInt("resourcesType");
        initViews();
        smartRefresh();
        messagePresenter = new MessagePresenter(mContext);
        messagePresenter.attachView(this);
        slRefreshLayout.autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void initViews() {
        setWhiteBar();
        messageAdapter = new MessageAdapter(getContext(), messageBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(messageAdapter);
        messageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MessageDetailActivity.start(mContext, messageBeanLists.get(position));
                messageBeanLists.get(position).setIsRead("1");
                messageAdapter.notifyDataSetChanged();
                messagePresenter.updateMessage(messageBeanLists.get(position).getId());
            }
        });


    }

    public void refresh(){
        pageNum = 1;
        messagePresenter.getMessageList(pageNum, resourcesType);
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
                messagePresenter.getMessageList(pageNum, resourcesType);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                messagePresenter.getMessageList(pageNum, resourcesType);
            }
        });
    }


    @Override
    public void onGetMessageBeanListSuccess(List<MessageBean> messageBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            messageBeanLists.clear();
        }
        messageBeanLists.addAll(messageBeanList);
        if (messageBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetMessagenumberSuccess(int sysSum, int thumbSum, int shareSum, int commentsSum, int exceptionalSum) {

    }

    @Override
    public void onAllReadSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
