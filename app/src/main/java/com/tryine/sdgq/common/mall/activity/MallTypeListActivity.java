package com.tryine.sdgq.common.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.tryine.sdgq.common.home.activity.SdSheetMusicListActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.presenter.MallHomePresenter;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品分类
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 10:30
 */
public class MallTypeListActivity extends BaseActivity implements MallHomeView {

    @BindView(R.id.rc_tag)
    RecyclerView rc_tag;


    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_tagchild)
    RecyclerView rc_tagchild;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<HomeMenuBean> homeMenuBeanList = new ArrayList<>();
    List<GoodsBean> goodsBeanLists = new ArrayList<>();

    private int selectedTabPosition = 0;
    int pageNum = 1;

    CommonAdapter commonAdapter;
    CommonAdapter goodsAdapter;

    MallHomePresenter mallHomePresenter;

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, MallTypeListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_malltype_list;
    }

    @Override
    protected void init() {
        setWhiteBar();
        selectedTabPosition = getIntent().getIntExtra("type", 0);
        mallHomePresenter = new MallHomePresenter(this);
        mallHomePresenter.attachView(this);
        mallHomePresenter.getGoodsTypeList();
        initViews();
        smartRefresh();
        initSecondAdapter();
    }

    @OnClick({R.id.iv_black, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_search:
                GoodsSearchActivity.start(mContext);
                break;
        }
    }

    private void initViews() {

        commonAdapter = new CommonAdapter(this, R.layout.item_malltype_first, homeMenuBeanList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                HomeMenuBean homeMenuBean = (HomeMenuBean) o;
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_title_per = holder.getView(R.id.tv_title_per);
                LinearLayout ll_title = holder.getView(R.id.ll_title);

                if (selectedTabPosition == position) {
                    tv_title.setVisibility(View.GONE);
                    ll_title.setVisibility(View.VISIBLE);
                } else {
                    tv_title.setVisibility(View.VISIBLE);
                    ll_title.setVisibility(View.INVISIBLE);
                }

                tv_title.setText(homeMenuBean.getTitle());
                tv_title_per.setText(homeMenuBean.getTitle());

                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedTabPosition = position;
                        mallHomePresenter.getGoodsList("", homeMenuBeanList.get(selectedTabPosition).getId(), pageNum);
                        notifyDataSetChanged();
                    }
                });

            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_tag.setLayoutManager(lin);
        rc_tag.setAdapter(commonAdapter);


        goodsAdapter = new CommonAdapter(this, R.layout.item_malltype_three, goodsBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                GoodsBean goodsBean = (GoodsBean) o;
                if (!TextUtils.isEmpty(goodsBean.getImgUrl())) {
                    GlideEngine.createGlideEngine().loadImage(mContext, goodsBean.getImgUrl().split("!#!")[0], holder.getView(R.id.iv_cover));
                } else {
                    GlideEngine.createGlideEngine().loadImage(mContext, goodsBean.getImgUrl(), holder.getView(R.id.iv_cover));
                }
                holder.setText(R.id.tv_salesVolume, "销量 " + goodsBean.getSalesVolume());
                holder.setText(R.id.tv_title, goodsBean.getName());
                ImageView iv_price = holder.getView(R.id.iv_price);

                if (goodsBean.getBeanType() == 0) {
                    iv_price.setBackgroundResource(R.mipmap.ic_jdz);
                    holder.setText(R.id.tv_price, goodsBean.getMarketPrice() + " 金豆");
                } else {
                    iv_price.setBackgroundResource(R.mipmap.ic_ydz);
                    holder.setText(R.id.tv_price, goodsBean.getMarketPrice() + " 银豆");
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoodsDetailActivity.start(mContext, goodsBeanLists.get(position).getId());
                    }
                });
            }
        };
        LinearLayoutManager lin1 = new LinearLayoutManager(this);
        lin1.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_tagchild.setLayoutManager(lin1);
        rc_tagchild.setAdapter(goodsAdapter);


    }


    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                if (null != homeMenuBeanList) {
                    mallHomePresenter.getGoodsList("", homeMenuBeanList.get(0).getId(), pageNum);
                }
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                if (null != homeMenuBeanList) {
                    mallHomePresenter.getGoodsList("", homeMenuBeanList.get(0).getId(), pageNum);
                }
            }
        });
    }


    private void initSecondAdapter() {
//        CommonAdapter commonAdapter = new CommonAdapter(this, R.layout.item_malltype_second, datas) {
//            @Override
//            protected void convert(ViewHolder holder, Object o, int position) {
//                RecyclerView rv_class = holder.getView(R.id.rv_class);
//
//                List<String> datas = new ArrayList<>();
//                datas.add("");
//                datas.add("");
//                datas.add("");
//                datas.add("");
//                addChildGoods(rv_class,datas);
//            }
//        };
//        LinearLayoutManager lin = new LinearLayoutManager(this);
//        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
//        rc_tagchild.setLayoutManager(lin);
//        rc_tagchild.setAdapter(commonAdapter);

    }


//    private void addChildGoods(RecyclerView rv_data, List<String> categoryBeanLists) {
//        goodsAdapter = new CommonAdapter(this, R.layout.item_malltype_three, categoryBeanLists) {
//            @Override
//            protected void convert(ViewHolder holder, Object o, int position) {
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        GoodsDetailActivity.start(mContext);
//                    }
//                });
//
//            }
//        };
//        LinearLayoutManager lin = new LinearLayoutManager(this);
//        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
//        rv_data.setLayoutManager(lin);
//        rv_data.setAdapter(commonAdapter);
//
//    }


    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {
        this.homeMenuBeanList.clear();
        this.homeMenuBeanList.addAll(homeMenuBeanList);
        if (null != homeMenuBeanList && homeMenuBeanList.size() > 0) {
            mallHomePresenter.getGoodsList("", homeMenuBeanList.get(selectedTabPosition).getId(), pageNum);
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            goodsBeanLists.clear();
        }
        goodsBeanLists.addAll(goodsBeanList);
        if (goodsBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_tagchild.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_tagchild.setVisibility(View.VISIBLE);
        }
        goodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetGoodsBeanListSuccess(List<GoodsBean> goodsBeanList) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
