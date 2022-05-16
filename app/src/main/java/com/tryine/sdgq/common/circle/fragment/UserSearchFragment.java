package com.tryine.sdgq.common.circle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.circle.adapter.CircleItemAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.presenter.CirclePresenter;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mine.adapter.FansAdapter;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 用户搜索搜索
 *
 * @author: zhangshuaijun
 * @time: 2021-12-31 13:11
 */
public class UserSearchFragment extends BaseFragment implements CircleView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_goods)
    RecyclerView rc_circleData;

    List<FansBean> fansBeanLists = new ArrayList<>();
    FansAdapter fansAdapter;

    int pageNum = 1;

    CirclePresenter circlePresenter;

    String searchStr;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserSearchFragment.class);
        context.startActivity(intent);
    }

    @Override
    public int getlayoutId() {
        return R.layout.fragment_search_circle;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefresh();
        circlePresenter = new CirclePresenter(mContext);
        circlePresenter.attachView(this);
        initViews();
    }


    public void setSearchStr(String searchStr){
        if(!TextUtils.isEmpty(searchStr)) {
            this.searchStr = searchStr;
            pageNum = 1;
            circlePresenter.getsearchuserinfo(searchStr, pageNum);
        }
    }

    protected void initViews() {
        fansAdapter = new FansAdapter(getContext(), fansBeanLists, "1");
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_circleData.setLayoutManager(lin);
        rc_circleData.setAdapter(fansAdapter);
        fansAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_root) {
                    PersonalHomePageActivity.start(mContext, fansBeanLists.get(position).getUserId());
                } else if (view.getId() == R.id.tv_gz) {
                    if ("0".equals("1")) {
                        if (fansBeanLists.get(position).getIsMutual().equals("1")) { //相互直接取消关注接口
                            circlePresenter.setFocus(fansBeanLists.get(position).getUserId(), "1");
                        }
                    } else {
                        if (fansBeanLists.get(position).getIsMutual().equals("0")) { //没有相互直接关注接口
                            circlePresenter.setFocus(fansBeanLists.get(position).getUserId(), "0");
                        }
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
        slRefreshLayout.setEnableRefresh(false);
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(!TextUtils.isEmpty(searchStr)) {
                    pageNum++;
                    circlePresenter.getsearchuserinfo(searchStr, pageNum);
                }
            }
        });
    }


    @Override
    public void onAddCircleSuccess() {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {

    }

    @Override
    public void onGetTopicListSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetHdListSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {

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
            rc_circleData.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_circleData.setVisibility(View.VISIBLE);
        }
        fansAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFocusSuccess(String isFocus) {
        pageNum = 1;
        circlePresenter.getsearchuserinfo(searchStr, pageNum);
    }

    @Override
    public void onGiveSuccess(String type, int position) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


}
