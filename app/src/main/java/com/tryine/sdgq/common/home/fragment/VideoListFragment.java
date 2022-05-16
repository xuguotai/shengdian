package com.tryine.sdgq.common.home.fragment;

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
import com.tryine.sdgq.common.circle.adapter.CircleTabBtnAdapter;
import com.tryine.sdgq.common.home.activity.CourseDateilActivity;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.adapter.CourseListAdapter;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SdVideoHomePresenter;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 圣典视频列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:13
 */
public class VideoListFragment extends BaseFragment implements SdHomeVideoView {

    List<String> tabBeanList = new ArrayList<String>();
    @BindView(R.id.rv_tabbtn)
    public RecyclerView rv_tabbtn;
    CircleTabBtnAdapter circleTabBtnAdapter;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<VideoModel> videoModelLists = new ArrayList<>();
    SdHomeVideoAdapter sdHomeVideoAdapter;

    SdVideoHomePresenter sdVideoHomePresenter;
    String typeId;
    int pageNum = 1;
    int orderByType = 0;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_video_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typeId = getArguments().getString("typeId");
        initViews();
        smartRefresh();

        sdVideoHomePresenter = new SdVideoHomePresenter(mContext);
        sdVideoHomePresenter.attachView(this);
        sdVideoHomePresenter.getVideoList(typeId, pageNum, orderByType);
    }

    protected void initViews() {
        setWhiteBar();

        tabBeanList.add("默认");
        tabBeanList.add("最新");
        tabBeanList.add("播放最多");
        if(!typeId.equals("-1")){
            tabBeanList.add("购买最多");
        }

        circleTabBtnAdapter = new CircleTabBtnAdapter(getContext(), tabBeanList);
        rv_tabbtn.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_tabbtn.setAdapter(circleTabBtnAdapter);
        circleTabBtnAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                circleTabBtnAdapter.setSelectedTabPosition(position);
                orderByType = position;
                sdVideoHomePresenter.getVideoList(typeId, pageNum, orderByType);
            }
        });


        sdHomeVideoAdapter = new SdHomeVideoAdapter(getContext(), videoModelLists, "0");
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(sdHomeVideoAdapter);
        sdHomeVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SdVideoDetailActivity.start(mContext, videoModelLists.get(position).getId());
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
                sdVideoHomePresenter.getVideoList(typeId, pageNum, orderByType);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                sdVideoHomePresenter.getVideoList(typeId, pageNum, orderByType);
            }
        });
    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoModel> videoModelList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            videoModelLists.clear();
        }
        videoModelLists.addAll(videoModelList);
        if (videoModelLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        sdHomeVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetZhVideoListSuccess(List<VideoModel> videoModelList) {

    }

    @Override
    public void onGetVideoDetailSuccess(VideoModel videoModel, List<VideoModel> videoModelList, List<SheetMusicBean> sheetMusicBeans) {

    }

    @Override
    public void onUnlockVideoSuccess() {

    }

    @Override
    public void onFocusSuccess(String isFocus) {

    }

    @Override
    public void onCollectSuccess(String isCollect) {

    }

    @Override
    public void onBuypiaonscoreSuccess() {

    }


    @Override
    public void onFailed(String message) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        ToastUtil.toastLongMessage(message);

    }
}
