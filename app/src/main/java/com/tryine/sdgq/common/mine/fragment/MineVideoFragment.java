package com.tryine.sdgq.common.mine.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SdVideoHomePresenter;
import com.tryine.sdgq.common.home.presenter.SheetMusicPresenter;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.common.home.view.SheetMusicView;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的视频
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 17:19
 */
public class MineVideoFragment extends BaseFragment implements SdHomeVideoView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<VideoModel> videoModelLists = new ArrayList<>();
    SdHomeVideoAdapter sdHomeVideoAdapter;
    SdVideoHomePresenter sdVideoHomePresenter;

    int pageNum = 1;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_tycard_list;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        smartRefresh();
        sdVideoHomePresenter = new SdVideoHomePresenter(mContext);
        sdVideoHomePresenter.attachView(this);
        slRefreshLayout.autoRefresh();
    }

    protected void initViews() {

        sdHomeVideoAdapter = new SdHomeVideoAdapter(getContext(), videoModelLists,"0");
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(sdHomeVideoAdapter);
        sdHomeVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SdVideoDetailActivity.start(mContext,videoModelLists.get(position).getId());
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
                sdVideoHomePresenter.getSelectmyvideolist(pageNum);

            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                sdVideoHomePresenter.getSelectmyvideolist(pageNum);
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
        ToastUtil.toastLongMessage(message);
    }
}
