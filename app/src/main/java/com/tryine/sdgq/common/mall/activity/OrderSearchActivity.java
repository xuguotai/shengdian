package com.tryine.sdgq.common.mall.activity;

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
import com.google.gson.Gson;
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
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.activity.LiveCourseDetailActivity;
import com.tryine.sdgq.common.live.adapter.LiveAdapter;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.presenter.LiveHomePresenter;
import com.tryine.sdgq.common.live.view.LiveHomeView;
import com.tryine.sdgq.common.mall.adapter.OrderGoodsItemAdapter;
import com.tryine.sdgq.common.mall.bean.OrderGoodsBean;
import com.tryine.sdgq.common.mall.bean.OrderListBean;
import com.tryine.sdgq.common.mall.presenter.OrderPresenter;
import com.tryine.sdgq.common.mall.view.OrderView;
import com.tryine.sdgq.common.user.activity.PayPasswordActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CountDownTextView;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.VerifyPayPwdDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单搜索
 *
 * @author: zhangshuaijun
 * @time: 2021-12-31 13:11
 */
public class OrderSearchActivity extends BaseActivity implements OrderView {
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

    List<OrderListBean> orderListBeanLists = new ArrayList<>();
    CommonAdapter orderItemAdapter;

    int pageNum = 1;

    OrderPresenter orderPresenter;

    String searchStr;
    UserBean userBean;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, OrderSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_order;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
    }

    @Override
    protected void init() {
        setWhiteBar();
        smartRefresh();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        tv_title.setText("订单搜索");
        orderPresenter = new OrderPresenter(mContext);
        orderPresenter.attachView(this);

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
                    orderPresenter.getOrderList(pageNum, "-1", searchStr);
                }
            }
        });

        orderItemAdapter = new CommonAdapter(this, R.layout.item_order, orderListBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                OrderListBean data = (OrderListBean) o;
                holder.setText(R.id.statusVal, data.getStatusVal());
                LinearLayout ll_cancelTime = holder.getView(R.id.ll_cancelTime);
                CountDownTextView countdown_text_view = holder.getView(R.id.countdown_text_view);
                TextView tv_ztd = holder.getView(R.id.tv_ztd);
                TextView tv_cancel = holder.getView(R.id.tv_cancel);
                TextView tv_tk = holder.getView(R.id.tv_tk);
                TextView tv_pay = holder.getView(R.id.tv_pay);
                TextView tv_fh = holder.getView(R.id.tv_fh);
                TextView tv_confirm = holder.getView(R.id.tv_confirm);
                TextView tv_pj = holder.getView(R.id.tv_pj);
                TextView tv_totalPayPrice = holder.getView(R.id.tv_totalPayPrice);

                ll_cancelTime.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_tk.setVisibility(View.GONE);
                tv_pay.setVisibility(View.GONE);
                tv_fh.setVisibility(View.GONE);
                tv_confirm.setVisibility(View.GONE);
                tv_ztd.setVisibility(View.GONE);
                tv_pj.setVisibility(View.GONE);

                RecyclerView rc_data = holder.getView(R.id.rc_data);
                OrderGoodsItemAdapter orderGoodsItemAdapter = new OrderGoodsItemAdapter(mContext, data.getPiratesOrderDetailListVoList());
                LinearLayoutManager lin = new LinearLayoutManager(mContext);
                lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
                rc_data.setLayoutManager(lin);
                rc_data.setAdapter(orderGoodsItemAdapter);
                orderGoodsItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        OrderDetailActivity.start(mContext, data);
                    }
                });


                double total_price = 0;
                double total_price1 = 0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                for (OrderGoodsBean goodsBean : data.getPiratesOrderDetailListVoList()) {
                    if (goodsBean.getBeanType() == 0) {
                        total_price = total_price + (Double.parseDouble(goodsBean.getUnitPrice()) * goodsBean.getCount());
                    } else {
                        total_price1 = total_price1 + (Double.parseDouble(goodsBean.getUnitPrice()) * goodsBean.getCount());
                    }
                }

                if (total_price > 0 && total_price1 > 0) {
                    holder.setText(R.id.tv_totalPayPrice, decimalFormat.format(total_price) + " SD金豆 + " + decimalFormat.format(total_price1) + " SD银豆");
                } else if (total_price > 0) {
                    holder.setText(R.id.tv_totalPayPrice, decimalFormat.format(total_price) + " SD金豆");
                } else if (total_price1 > 0) {
                    holder.setText(R.id.tv_totalPayPrice, decimalFormat.format(total_price1) + " SD银豆");
                }


                //单状态 (0:订单关闭；1：待付款；2:订单支付超时；3:已取消 4：待评价；5:已完成 ；6:退款中；7:退款完成 8待自提)
                if ("0".equals(data.getStatus())) {

                } else if ("1".equals(data.getStatus())) {
                    tv_cancel.setVisibility(View.VISIBLE);
                    tv_pay.setVisibility(View.VISIBLE);
                    ll_cancelTime.setVisibility(View.VISIBLE);

                    float f = Float.valueOf(DateTimeHelper.getSurplusTime(data.getAutoCancelTime()));
                    countdown_text_view.setMaxTime(Math.round(f));
                    countdown_text_view.setmFinishText("");
                    countdown_text_view.startCountDown();
                    countdown_text_view.setishour(true);
                } else if ("2".equals(data.getStatus())) {

                } else if ("3".equals(data.getStatus())) {

                } else if ("4".equals(data.getStatus())) {
                    tv_pj.setVisibility(View.VISIBLE);

                } else if ("5".equals(data.getStatus())) {

                } else if ("6".equals(data.getStatus())) {

                } else if ("7".equals(data.getStatus())) {

                } else if ("8".equals(data.getStatus())) {
                    tv_ztd.setText("自提点：" + data.getPickAddress());
                    tv_ztd.setVisibility(View.VISIBLE);
                    tv_tk.setVisibility(View.VISIBLE);
                    tv_confirm.setVisibility(View.VISIBLE);

                }


                holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(data.getOrderNo());
                    }
                });
                holder.setOnClickListener(R.id.tv_tk, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRefundActivity.start(OrderSearchActivity.this, data, tv_totalPayPrice.getText().toString());
                    }
                });
                holder.setOnClickListener(R.id.tv_pay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payOrder(data.getOrderNo());
                    }
                });
                holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmOrder(data.getOrderNo());
                    }
                });
                holder.setOnClickListener(R.id.tv_pj, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReleaseCommentActivity.start(OrderSearchActivity.this, data);
                    }
                });
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDetailActivity.start(mContext, data);
                    }
                });


            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(orderItemAdapter);


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
                    orderPresenter.getOrderList(pageNum, "-1", searchStr);
                }
            }
        });
    }


    @Override
    public void onGetOrderBeanListSuccess(List<OrderListBean> orderListBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            orderListBeanLists.clear();
        }
        orderListBeanLists.addAll(orderListBeanList);
        if (orderListBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            rc_data.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            rc_data.setVisibility(View.VISIBLE);
        }
        orderItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetOrderBeanSuccess(OrderListBean orderListBean) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }


    @Override
    public void onRefundSuccess() {

    }

    @Override
    public void onCommentSuccess(int i) {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {

    }

    @Override
    public void onCancelorderSuccess() {
        ToastUtil.toastLongMessage("操作成功");
        slRefreshLayout.autoRefresh();
    }

    @Override
    public void onDetermineSuccess() {
        ToastUtil.toastLongMessage("操作成功");
        slRefreshLayout.autoRefresh();
    }

    @Override
    public void onPayOrderSuccess() {
        ToastUtil.toastLongMessage("支付成功");
        slRefreshLayout.autoRefresh();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    /**
     * 确认收货
     *
     * @param orderNo
     */
    private void confirmOrder(String orderNo) {
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示", "是否确认收货", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                orderPresenter.determine(orderNo);
            }

            @Override
            public void cancel() {
            }
        });
    }


    private void payOrder(String orderNo) {
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示", "是否支付订单", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                if (TextUtils.isEmpty(userBean.getPayPassword())) {
                    ToastUtil.toastLongMessage("请先设置支付密码");
                    PayPasswordActivity.start(mContext);
                    return;
                }
                VerifyPayPwdDialog verifyPayPwdDialog = new VerifyPayPwdDialog(mContext, R.style.dialog_center);
                verifyPayPwdDialog.show();
                verifyPayPwdDialog.setmOnTextSendListener(new VerifyPayPwdDialog.OnTextSendListener() {
                    @Override
                    public void onbackPwd(String password) {
                        orderPresenter.payOrder(orderNo, password);
                    }

                    @Override
                    public void dismiss() {

                    }
                });
            }

            @Override
            public void cancel() {
            }
        });
    }


    /**
     * 取消订单
     *
     * @param orderNo
     */
    private void cancelOrder(String orderNo) {
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示", "确认取消订单", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                orderPresenter.cancelorder(orderNo);
            }

            @Override
            public void cancel() {
            }
        });

    }


}
