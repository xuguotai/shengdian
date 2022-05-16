package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.tryine.sdgq.common.circle.presenter.BannerPresenter;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.presenter.SheetMusicPresenter;
import com.tryine.sdgq.common.home.view.SheetMusicView;
import com.tryine.sdgq.util.AdvertisingJumpUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerViewPager;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圣典琴谱列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-26 15:17
 */
public class SdSheetMusicListActivity extends BaseActivity implements SheetMusicView {

    @BindView(R.id.rc_tag)
    RecyclerView rc_tag;
    @BindView(R.id.rc_tagchild)
    RecyclerView rc_tagchild;

    @BindView(R.id.banner)
    BannerViewPager bannerView;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;


    List<HomeMenuBean> homeMenuBeanList = new ArrayList<>();
    List<HomeMenuBean> childMenuList = new ArrayList<>();
    CommonAdapter commonAdapter;
    CommonAdapter childAdapter;

    private int selectedTabPosition = 0;

    int pageNum = 1;
    String typeId;

    SheetMusicPresenter sheetMusicPresenter;

    BannerPresenter bannerPresenter;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SdSheetMusicListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sheetmusic_list;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();

        sheetMusicPresenter = new SheetMusicPresenter(this);
        sheetMusicPresenter.attachView(this);
        sheetMusicPresenter.getQpTypeList();
        initViews();
        initSecondAdapter();

        bannerPresenter = new BannerPresenter(mContext);
        bannerPresenter.attachView(new BannerView() {
            @Override
            public void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList) {
                if (null != bannerBeanList && bannerBeanList.size() > 0) {
                    bannerView.initBanner(bannerBeanList, false)//关闭3D画廊效果
                            .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                            .addStartTimer(bannerBeanList.size() > 1 ? 5 : 10000)//自动轮播5秒间隔
                            .finishConfig()//这句必须加
                            .addPoint(bannerBeanList.size())//添加指示器
                            .addRoundCorners(UIUtils.dip2px(5))//圆角
                            .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                                @Override
                                public void onBannerClick(int position) {
                                    BannerBean bannerBean = bannerBeanList.get(position);
                                    AdvertisingJumpUtils.advertisingJump(SdSheetMusicListActivity.this, bannerBean);
                                }
                            });
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.toastLongMessage(message);
            }
        });
        bannerPresenter.getBannerList(5);

    }

    private void initViews() {
        commonAdapter = new CommonAdapter(this, R.layout.item_sheetmusic_first, homeMenuBeanList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                HomeMenuBean categoryBean = (HomeMenuBean) o;
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

                tv_title.setText(categoryBean.getName());
                tv_title_per.setText(categoryBean.getName());

                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedTabPosition = position;
                        pageNum = 1;
                        typeId = homeMenuBeanList.get(selectedTabPosition).getId();
                        if (typeId.equals("-1")) { //-1查询我的琴谱
                            sheetMusicPresenter.getMypiaonscorelist(pageNum);
                        } else {
                            sheetMusicPresenter.getQpChildTypeList(typeId, pageNum);
                        }

                        notifyDataSetChanged();
                    }
                });

            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_tag.setLayoutManager(lin);
        rc_tag.setAdapter(commonAdapter);
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

                if (null != categoryBean.getIsUnlock() && !categoryBean.getIsUnlock().equals("1")) {
                    iv_isUnlock.setBackgroundResource(R.mipmap.ic_suo);
                } else {
                    iv_isUnlock.setBackgroundResource(R.mipmap.ic_per_right);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != childMenuList.get(position).getIsUnlock() && !childMenuList.get(position).getIsUnlock().equals("1")) {
                            PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                                    "需支付" + childMenuList.get(position).getSilverBean() + "银豆解锁", "确认", "取消");
                            promptDialog.show();
                            promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                                @Override
                                public void insure() {
                                    sheetMusicPresenter.buypiaonscore(childMenuList.get(position).getId());
                                }

                                @Override
                                public void cancel() {

                                }
                            });
                        } else {
                            if(typeId.equals("-1")){
                                SheetMusicDetailActivity.start(mContext, childMenuList.get(position).getPiaonScoreId());
                            }else{
                                SheetMusicDetailActivity.start(mContext, childMenuList.get(position).getId());
                            }

                        }
                    }
                });
            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_tagchild.setLayoutManager(lin);
        rc_tagchild.setAdapter(childAdapter);

    }

    @OnClick({R.id.iv_black, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_search:
                SheetMusicSearchActivity.start(mContext);
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
                if (typeId.equals("-1")) { //-1查询我的琴谱
                    sheetMusicPresenter.getMypiaonscorelist(pageNum);
                } else {
                    sheetMusicPresenter.getQpChildTypeList(typeId, pageNum);
                }
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                if (typeId.equals("-1")) { //-1查询我的琴谱
                    sheetMusicPresenter.getMypiaonscorelist(pageNum);
                } else {
                    sheetMusicPresenter.getQpChildTypeList(typeId, pageNum);
                }
            }
        });
    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanLists) {
        homeMenuBeanList.clear();
        homeMenuBeanList.addAll(homeMenuBeanLists);

        HomeMenuBean homeMenuBean = new HomeMenuBean();
        homeMenuBean.setName("我的琴谱");
        homeMenuBean.setId("-1");
        homeMenuBeanList.add(homeMenuBean);

        commonAdapter.notifyDataSetChanged();
        if (null != homeMenuBeanList && homeMenuBeanList.size() > 0) {
            typeId = homeMenuBeanList.get(0).getId();
            //默认加载第一个类型下的列表
            sheetMusicPresenter.getQpChildTypeList(typeId, pageNum);
        }
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
        ToastUtil.toastLongMessage("解锁成功");
        //购物成功刷新列表
        pageNum = 1;
        sheetMusicPresenter.getQpChildTypeList(typeId, pageNum);
    }

    @Override
    public void onGetpiaonscoredetailSuccess(SheetMusicBean sheetMusicBean, List<VideoModel> videoModelList) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
