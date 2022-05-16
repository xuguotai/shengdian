package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.activity.BargainOrderDetailActivity;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.BargainOrderDetailBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.presenter.BargainPresenter;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.presenter.MallHomePresenter;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CountDownTextView;
import com.tryine.sdgq.view.SDAvatarListLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-31 13:11
 */
public class BargainOrderSearchActivity extends BaseActivity implements BargainView {
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

    List<BargainBean> bargainBeanLists = new ArrayList<>();
    CommonAdapter commonAdapter;

    int pageNum = 1;

    BargainPresenter bargainPresenter;

    String searchStr;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, BargainOrderSearchActivity.class);
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
        tv_title.setText("砍价订单搜索");
        bargainPresenter = new BargainPresenter(this);
        bargainPresenter.attachView(this);
        slRefreshLayout.setVisibility(View.VISIBLE);

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
                    bargainPresenter.getBargainOrderlist(pageNum, "0", searchStr);
                }
            }
        });


        commonAdapter = new CommonAdapter(this, R.layout.item_home_bargain, bargainBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                BargainBean data = (BargainBean) o;
                GlideEngine.createGlideEngine().loadImage(mContext, data.getBargainImgUrl(), holder.getView(R.id.iv_cover));
                holder.setText(R.id.tv_title, data.getBargainName());

                CountDownTextView countdown_text_view = holder.getView(R.id.countdown_text_view);

                float f = Float.valueOf(DateTimeHelper.getSurplusTime(data.getBargainEndTime()));
                countdown_text_view.setMaxTime(Math.round(f));
                countdown_text_view.setmFinishText("砍价已结束");
                countdown_text_view.startCountDown();
                countdown_text_view.setishour(true);
                countdown_text_view.setVisibility(View.GONE);

                TextView tv_status = holder.getView(R.id.tv_status);
                TextView tv_kj = holder.getView(R.id.tv_kj);
                TextView tv_time = holder.getView(R.id.tv_time);
                tv_time.setVisibility(View.GONE);
                if (data.getStatus().equals("1")) {//1:砍价中 2:待付款 3:待核销 4:已完成 5:已过期
                    tv_status.setText("砍价中");
                    tv_kj.setText("去砍价");
                    countdown_text_view.setVisibility(View.VISIBLE);
                    countdown_text_view.setVisibility(View.VISIBLE);
                } else if (data.getStatus().equals("2")) {
                    tv_status.setText("待付款");
                    tv_kj.setText("去付款");
                } else if (data.getStatus().equals("3")) {
                    tv_status.setText("待核销");
                    tv_kj.setText("待核销");
                } else if (data.getStatus().equals("4")) {
                    tv_status.setText("已完成");
                    tv_kj.setText("已完成");
                } else if (data.getStatus().equals("5")) {
                    tv_status.setText("已过期");
                    tv_kj.setText("已过期");
                }

                if(TextUtils.isEmpty(data.getBargainCount()) || data.getBargainCount().equals("null")){
                    holder.setText(R.id.tv_bargainCount, "0人帮我砍价");
                }else{
                    holder.setText(R.id.tv_bargainCount, data.getBargainCount() + "人帮我砍价");
                }

                holder.setText(R.id.tv_appointPrice, data.getCurrentPrice());

                TextView tv_price = holder.getView(R.id.tv_price);
                tv_price.setText("￥" + data.getCostPrice());
                tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_price.getPaint().setAntiAlias(true);

                SDAvatarListLayout sd_head = holder.getView(R.id.sd_head);
                sd_head.setAvatarListListener(data.getHelpHeadImgList());
                tv_kj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BargainOrderDetailActivity.start(mContext, bargainBeanLists.get(position).getId());
                    }
                });
            }
        };
        LinearLayoutManager lin1 = new LinearLayoutManager(this);
        lin1.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_goods.setLayoutManager(lin1);
        rc_goods.setAdapter(commonAdapter);

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
                if(!TextUtils.isEmpty(searchStr)) {
                    pageNum++;
                    bargainPresenter.getBargainOrderlist(pageNum, "0", searchStr);
                }
            }
        });
    }


    @Override
    public void onGetBargainBeanListSuccess(List<BargainBean> bargainBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            bargainBeanLists.clear();
        }
        bargainBeanLists.addAll(bargainBeanList);
        if (bargainBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_goods.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_goods.setVisibility(View.VISIBLE);
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCampusBeanSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onSaveorderSuccess(String orderId) {

    }

    @Override
    public void onGetBargainOrderDetailBeanSuccess(BargainOrderDetailBean bargainOrderDetailBean) {

    }

    @Override
    public void onBargainBuySuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
