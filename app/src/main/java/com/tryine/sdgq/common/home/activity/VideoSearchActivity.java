package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.adapter.HotLabelAdapter;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.home.adapter.SdHomeVideoAdapter;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SdVideoHomePresenter;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.presenter.MallHomePresenter;
import com.tryine.sdgq.common.mall.presenter.SearchPresenter;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.common.mall.view.SearchView;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频搜索
 * @author: zhangshuaijun
 * @time: 2021-12-31 13:11
 */
public class VideoSearchActivity extends BaseActivity implements SdHomeVideoView, SearchView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_goods)
    RecyclerView rc_goods;
    @BindView(R.id.rc_tab)
    RecyclerView rc_tab;
    @BindView(R.id.ll_tab)
    LinearLayout ll_tab;

    List<VideoModel> videoModelLists = new ArrayList<>();
    SdHomeVideoAdapter sdHomeVideoAdapter;

    HotLabelAdapter hotLabelAdapter;
    private List<LabelBean> labelList = new ArrayList<>();

    int pageNum = 1;

    SdVideoHomePresenter sdVideoHomePresenter;
    SearchPresenter searchPresenter;

    String searchStr;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, VideoSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_goods;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("视频搜索");
        sdVideoHomePresenter = new SdVideoHomePresenter(this);
        sdVideoHomePresenter.attachView(this);

        searchPresenter = new SearchPresenter(this);
        searchPresenter.attachView(this);
        searchPresenter.searchHot(1);
        ll_tab.setVisibility(View.VISIBLE);

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                searchStr = s.toString();
                if(!TextUtils.isEmpty(searchStr)) {
                    pageNum = 1;
                    sdVideoHomePresenter.getSearchvideolist(searchStr, pageNum);
                    slRefreshLayout.setVisibility(View.VISIBLE);
                    ll_tab.setVisibility(View.GONE);
                }else{
                    slRefreshLayout.setVisibility(View.GONE);
                    ll_tab.setVisibility(View.VISIBLE);
                }
            }
        });


        sdHomeVideoAdapter = new SdHomeVideoAdapter(this, videoModelLists,"0");
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_goods.setLayoutManager(lin);
        rc_goods.setAdapter(sdHomeVideoAdapter);
        sdHomeVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SdVideoDetailActivity.start(mContext,videoModelLists.get(position).getId());
            }
        });

        hotLabelAdapter = new HotLabelAdapter(mContext, labelList);
        //设置布局管理器
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(mContext);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager1.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。
        rc_tab.setLayoutManager(flexboxLayoutManager1);
        rc_tab.setAdapter(hotLabelAdapter);
        hotLabelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                et_name.setText(labelList.get(position).getName());
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
                sdVideoHomePresenter.getSearchvideolist(searchStr, pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(!TextUtils.isEmpty(searchStr)) {
                    pageNum++;
                    sdVideoHomePresenter.getSearchvideolist(searchStr, pageNum);
                }
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
            rc_goods.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_goods.setVisibility(View.VISIBLE);
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
    public void onGetLabelBeanSuccess(List<LabelBean> labelBeanList) {
        labelList.clear();
        labelList.addAll(labelBeanList);
        hotLabelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
