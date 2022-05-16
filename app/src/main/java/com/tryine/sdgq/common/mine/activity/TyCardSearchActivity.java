package com.tryine.sdgq.common.mine.activity;

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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.activity.ApplyRefundActivity;
import com.tryine.sdgq.common.mall.activity.OrderDetailActivity;
import com.tryine.sdgq.common.mall.activity.ReleaseCommentActivity;
import com.tryine.sdgq.common.mall.adapter.OrderGoodsItemAdapter;
import com.tryine.sdgq.common.mall.bean.OrderGoodsBean;
import com.tryine.sdgq.common.mall.bean.OrderListBean;
import com.tryine.sdgq.common.mall.presenter.OrderPresenter;
import com.tryine.sdgq.common.mall.view.OrderView;
import com.tryine.sdgq.common.mine.adapter.TyCardListAdapter;
import com.tryine.sdgq.common.mine.bean.CardBean;
import com.tryine.sdgq.common.mine.bean.ExperienceBean;
import com.tryine.sdgq.common.mine.presenter.TyCardPresenter;
import com.tryine.sdgq.common.mine.view.TyCardView;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CountDownTextView;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 体验卡搜索搜索
 *
 * @author: zhangshuaijun
 * @time: 2021-12-31 13:11
 */
public class TyCardSearchActivity extends BaseActivity implements TyCardView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rc_goods)
    RecyclerView rc_data;

    List<CardBean> cardBeanLists = new ArrayList<>();
    TyCardListAdapter tyCardListAdapter;


    int pageNum = 1;

    TyCardPresenter tyCardPresenter;


    String searchStr;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TyCardSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_order;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("体验卡搜索");
        tyCardPresenter = new TyCardPresenter(mContext);
        tyCardPresenter.attachView(this);

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
                if (!TextUtils.isEmpty(searchStr)) {
                    pageNum = 1;
                    tyCardPresenter.getMyCardSearchList(searchStr,pageNum);
                }
            }
        });

        tyCardListAdapter = new TyCardListAdapter(this, cardBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(tyCardListAdapter);
        tyCardListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_zzhy) {
                    TyCardZZActivity.start(mContext,cardBeanLists.get(position).getId());
                }
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
                if (!TextUtils.isEmpty(searchStr)) {
                    pageNum++;
                    tyCardPresenter.getMyCardSearchList(searchStr,pageNum);
                }
            }
        });
    }


    @Override
    public void onGetCardBeanListSuccess(List<CardBean> cardBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            cardBeanLists.clear();
        }
        cardBeanLists.addAll(cardBeanList);
        if (cardBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        tyCardListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetExperienceBeanSuccess(List<ExperienceBean> experienceBeanLists, int pages) {

    }

    @Override
    public void onGetCardBeanListSuccess() {

    }

    @Override
    public void onForwardingSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }





}
