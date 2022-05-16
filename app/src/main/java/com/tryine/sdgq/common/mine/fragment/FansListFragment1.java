package com.tryine.sdgq.common.mine.fragment;

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
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.mine.adapter.FansAdapter;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.presenter.MinePresenter;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 粉丝、关注列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 17:19
 */
public class FansListFragment1 extends BaseFragment implements MineView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<FansBean> fansBeanLists = new ArrayList<>();
    FansAdapter fansAdapter;

    MinePresenter minePresenter;

    int pageNum = 1;

    String type = "0";//0关注 1粉丝

    String userId;
    String selectType; //查看类型 0-看自己 1-看他人


    @Override
    public int getlayoutId() {
        return R.layout.fragment_tycard_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        slRefreshLayout.autoRefresh();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getString("type");
        userId = getArguments().getString("userId");
        selectType = getArguments().getString("selectType");
        initViews();
        smartRefresh();
        setWhiteBar();
        minePresenter = new MinePresenter(mContext);
        minePresenter.attachView(this);
        slRefreshLayout.autoRefresh();
    }

    protected void initViews() {

        fansAdapter = new FansAdapter(getContext(), fansBeanLists, type);
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(fansAdapter);
        fansAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_root) {
                    PersonalHomePageActivity.start(mContext, fansBeanLists.get(position).getUserId());
                } else if (view.getId() == R.id.tv_gz) {
                        if (fansBeanLists.get(position).getIsMutual().equals("0")) { //没有相互直接关注接口
                            minePresenter.setFocus(fansBeanLists.get(position).getUserId(), "0");
                        }
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
                minePresenter.getFansList(pageNum, userId, selectType);

            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                minePresenter.getFansList(pageNum, userId, selectType);
            }
        });
    }


    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList) {

    }

    @Override
    public void onGetFansBeanSuccess(List<FansBean> fansBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            fansBeanLists.clear();
        }
        fansBeanLists.addAll(fansBeanList);
        if (fansBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        fansAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFocusSuccess() {
        slRefreshLayout.autoRefresh();
    }

    @Override
    public void onUpdatepasswordSuccess() {

    }

    @Override
    public void onCodeSuccess() {

    }

    @Override
    public void onGetIsLiveSuccess(int isLive, int realStatus, int liveId, String trtcPushAddr) {

    }

    @Override
    public void onGetaboutinfoSuccess(String rewardDesc) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
