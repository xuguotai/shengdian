package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.circle.adapter.CircleItemAdapter;
import com.tryine.sdgq.common.circle.adapter.HotLabelAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.presenter.CirclePresenter;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SheetMusicPresenter;
import com.tryine.sdgq.common.home.view.SheetMusicView;
import com.tryine.sdgq.common.mall.presenter.SearchPresenter;
import com.tryine.sdgq.common.mall.view.SearchView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 琴谱搜索
 *
 * @author: zhangshuaijun
 * @time: 2021-12-31 13:11
 */
public class SheetMusicSearchActivity extends BaseActivity implements SheetMusicView, SearchView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_goods)
    RecyclerView rc_tagchild;
    @BindView(R.id.rc_tab)
    RecyclerView rc_tab;
    @BindView(R.id.ll_tab)
    LinearLayout ll_tab;


    HotLabelAdapter hotLabelAdapter;
    private List<LabelBean> labelList = new ArrayList<>();

    List<HomeMenuBean> childMenuList = new ArrayList<>();
    CommonAdapter childAdapter;

    int pageNum = 1;

    SheetMusicPresenter sheetMusicPresenter;
    SearchPresenter searchPresenter;

    String searchStr;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SheetMusicSearchActivity.class);
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
        initSecondAdapter();
        tv_title.setText("琴谱搜索");
        sheetMusicPresenter = new SheetMusicPresenter(this);
        sheetMusicPresenter.attachView(this);


        searchPresenter = new SearchPresenter(this);
        searchPresenter.attachView(this);
        searchPresenter.searchHot(0);
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
                if(!TextUtils.isEmpty(searchStr)){
                    pageNum = 1;
                    sheetMusicPresenter.getSearchMusic(searchStr, pageNum);
                    slRefreshLayout.setVisibility(View.VISIBLE);
                    ll_tab.setVisibility(View.GONE);
                }else{
                    slRefreshLayout.setVisibility(View.GONE);
                    ll_tab.setVisibility(View.VISIBLE);
                }
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

    private void initSecondAdapter() {
        childAdapter = new CommonAdapter(this, R.layout.item_sheetmusic_second, childMenuList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                HomeMenuBean categoryBean = (HomeMenuBean) o;
                holder.setText(R.id.tv_title, categoryBean.getName());
                TextView tv_silverBean = holder.getView(R.id.tv_silverBean);
                ImageView iv_isUnlock = holder.getView(R.id.iv_isUnlock);
                tv_silverBean.setText(categoryBean.getSilverBean());

                if (!categoryBean.getIsUnlock().equals("1")) {
                    iv_isUnlock.setBackgroundResource(R.mipmap.ic_suo);
                } else {
                    iv_isUnlock.setBackgroundResource(R.mipmap.ic_per_right);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SheetMusicDetailActivity.start(mContext, childMenuList.get(position).getId());
                    }
                });
            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_tagchild.setLayoutManager(lin);
        rc_tagchild.setAdapter(childAdapter);

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
        slRefreshLayout.setEnableRefresh(false);
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
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
                if(!TextUtils.isEmpty(searchStr)){
                    pageNum++;
                    sheetMusicPresenter.getSearchMusic(searchStr, pageNum);
                }

            }
        });
    }


    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onGetChildMenuSuccess(List<HomeMenuBean> homeMenuBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            childMenuList.clear();
        }
        childMenuList.addAll(homeMenuBeanList);
        childAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBuypiaonscoreSuccess() {

    }

    @Override
    public void onGetpiaonscoredetailSuccess(SheetMusicBean sheetMusicBean, List<VideoModel> videoModelList) {

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
