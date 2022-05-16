package com.tryine.sdgq.common.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
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
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.adapter.CartAdapter;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.presenter.CartPresenter;
import com.tryine.sdgq.common.mall.view.CartView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购物车
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 13:47
 */
public class MallCartActivity extends BaseActivity implements CartView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_goodsNum)
    TextView tv_goodsNum;
    @BindView(R.id.tv_gl)
    TextView tv_gl;
    @BindView(R.id.tv_jiesuan)
    TextView tv_jiesuan;
    @BindView(R.id.tv_total_price)
    TextView tv_total_price;
    @BindView(R.id.tv_total_price1)
    TextView tv_total_price1;
    @BindView(R.id.check_all)
    CheckBox check_all;

    List<GoodsBean> goodsBeanLists = new ArrayList<>();

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    CartAdapter cartAdapter;

    CartPresenter cartPresenter;
    int pageNum = 1;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MallCartActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_mall_cart;
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        tv_title.setText("购物车");

        cartPresenter = new CartPresenter(this);
        cartPresenter.attachView(this);
        cartPresenter.getCarGoodsList(pageNum);

        cartAdapter = new CartAdapter(this, goodsBeanLists);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_data.setLayoutManager(lin);
        rv_data.setAdapter(cartAdapter);
        cartAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_jian) {
                    if (goodsBeanLists.get(position).getQuantity() > 1) {
                        cartPresenter.updateCar(goodsBeanLists.get(position).getId(), goodsBeanLists.get(position).getQuantity() - 1, position);
                    }
                } else if (view.getId() == R.id.iv_add) {
                    cartPresenter.updateCar(goodsBeanLists.get(position).getId(), goodsBeanLists.get(position).getQuantity() + 1, position);
                }
            }
        });

        cartAdapter.setOnItemCheckedChangeListener(new CartAdapter.OnItemCheckedChangeListener() {
            @Override
            public void onCheckedChange(boolean isChecked, int position) {
                goodsBeanLists.get(position).setChecked(isChecked);
                calculatePrice();
                checkStatus();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pageNum = 1;
        cartPresenter.getCarGoodsList(pageNum);
    }

    @OnClick({R.id.iv_black, R.id.tv_jiesuan, R.id.check_all, R.id.tv_gl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_jiesuan:
                if (tv_gl.getText().equals("管理")) {
                    List<GoodsBean> goodsBeanLists1 = new ArrayList<>();
                    for (GoodsBean goodsBean : goodsBeanLists) {
                        if (goodsBean.isChecked()) {
                            goodsBeanLists1.add(goodsBean);
                        }
                    }
                    if (null != goodsBeanLists1 && goodsBeanLists1.size() > 0) {
                        MallConfirmOrderActivity.start(mContext, goodsBeanLists1);
                    } else {
                        ToastUtil.toastLongMessage("请选择购买的商品");
                    }

                } else {
                    initDelete();
                }

                break;
            case R.id.tv_gl:
                if (tv_gl.getText().equals("管理")) {
                    tv_gl.setText("完成");
                    tv_jiesuan.setText("移除");
                } else {
                    tv_gl.setText("管理");
                    tv_jiesuan.setText("结算");
                }
                break;
            case R.id.check_all:
                if (check_all.isChecked()) {
                    for (GoodsBean goodsBean : goodsBeanLists) {
                        goodsBean.setChecked(true);
                    }
                } else {
                    for (GoodsBean goodsBean : goodsBeanLists) {
                        goodsBean.setChecked(false);
                    }
                }

                cartAdapter.notifyDataSetChanged();
                calculatePrice();

                break;
        }
    }


    private void calculatePrice() {

        double total_price = 0;
        double total_price1 = 0;

        for (GoodsBean goodsBean : goodsBeanLists) {
            if (goodsBean.isChecked()) {
                if (goodsBean.getBeanType() == 0) {
                    total_price = total_price + (Double.parseDouble(goodsBean.getPrice()) * goodsBean.getQuantity());
                } else {
                    total_price1 = total_price1 + (Double.parseDouble(goodsBean.getPrice()) * goodsBean.getQuantity());
                }

            }


        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (total_price > 0 && total_price1 > 0) {
            tv_total_price.setVisibility(View.VISIBLE);
            tv_total_price1.setVisibility(View.VISIBLE);
            tv_total_price.setText(decimalFormat.format(total_price) + " 金豆 + ");
            tv_total_price1.setText(decimalFormat.format(total_price1) + " 银豆");
        } else if (total_price > 0) {
            tv_total_price.setVisibility(View.VISIBLE);
            tv_total_price1.setVisibility(View.GONE);
            tv_total_price.setText(decimalFormat.format(total_price) + " 金豆");
        } else if (total_price1 > 0) {
            tv_total_price.setVisibility(View.GONE);
            tv_total_price1.setVisibility(View.VISIBLE);
            tv_total_price1.setText(decimalFormat.format(total_price1) + " 银豆");
        }

    }


    private void checkStatus() {
        boolean ischeck = true;
        for (GoodsBean goodsBean : goodsBeanLists) {
            if (!goodsBean.isChecked()) {
                ischeck = false;
            }
        }
        check_all.setChecked(ischeck);
    }

    private void initDelete() {
        JSONArray carIdArray = new JSONArray();
        for (GoodsBean goodsBean : goodsBeanLists) {
            if (goodsBean.isChecked()) {
                carIdArray.put(goodsBean.getId());
            }
        }
        if (carIdArray.length() > 0) {
            delGoodsCarDialog(carIdArray);
        } else {
            ToastUtil.toastLongMessage("请选择要删除的商品");
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
                cartPresenter.getCarGoodsList(pageNum);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                cartPresenter.getCarGoodsList(pageNum);
            }
        });
    }


    @Override
    public void onGetCartListSuccess(List<GoodsBean> goodsBeanList, int pages) {
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
        tv_goodsNum.setText("共" + goodsBeanList.size() + "件商品");
        if (goodsBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rv_data.setVisibility(View.GONE);
            tv_total_price.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rv_data.setVisibility(View.VISIBLE);
        }
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateCarSuccess(int quantity, int position) {
        goodsBeanLists.get(position).setQuantity(quantity);
        cartAdapter.notifyDataSetChanged();
        calculatePrice();
    }

    @Override
    public void onGoodsCarDelSuccess() {
        tv_total_price.setVisibility(View.GONE);
        tv_total_price1.setVisibility(View.GONE);
        pageNum = 1;
        cartPresenter.getCarGoodsList(pageNum);

    }

    @Override
    public void onCreateOrderSuccess(String orderNo, String totalPrice) {

    }

    @Override
    public void onPayOrderSuccess() {

    }


    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList, int pages) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    private void delGoodsCarDialog(JSONArray carIdArray) {
        PromptDialog promptDialog = new PromptDialog(this, 0, "提示", "确认删除？", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                cartPresenter.goodsCarDel(carIdArray);
            }

            @Override
            public void cancel() {
            }
        });

    }
}
