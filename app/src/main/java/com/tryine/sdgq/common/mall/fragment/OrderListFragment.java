package com.tryine.sdgq.common.mall.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.activity.ApplyRefundActivity;
import com.tryine.sdgq.common.mall.activity.OrderDetailActivity;
import com.tryine.sdgq.common.mall.activity.ReleaseCommentActivity;
import com.tryine.sdgq.common.mall.adapter.OrderGoodsItemAdapter;
import com.tryine.sdgq.common.mall.adapter.OrderItemAdapter;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
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

/**
 * 订单列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:13
 */
public class OrderListFragment extends BaseFragment implements OrderView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    List<OrderListBean> orderListBeanLists = new ArrayList<>();
    CommonAdapter orderItemAdapter;

    int pageNum = 1;
    String status;

    OrderPresenter orderPresenter;

    UserBean userBean;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        slRefreshLayout.autoRefresh();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
    }

    protected void initViews() {
        setWhiteBar();
        smartRefresh();
        status = getArguments().getString("status");
        orderPresenter = new OrderPresenter(mContext);
        orderPresenter.attachView(this);
        slRefreshLayout.autoRefresh();


        orderItemAdapter = new CommonAdapter(getContext(), R.layout.item_order, orderListBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                OrderListBean data = (OrderListBean) o;
                if(data.getStatus().equals("8")){
                    if(!TextUtils.isEmpty(data.getReceiptAddress())){
                        holder.setText(R.id.statusVal, "邮寄");
                    }else{
                        holder.setText(R.id.statusVal, "待自提");
                    }
                }else{
                    holder.setText(R.id.statusVal, data.getStatusVal());
                }

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


                holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(data.getOrderNo());
                    }
                });
                holder.setOnClickListener(R.id.tv_tk, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRefundActivity.start(getActivity(), data, tv_totalPayPrice.getText().toString());
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
                        ReleaseCommentActivity.start(getActivity(), data);
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
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(orderItemAdapter);


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
                orderPresenter.getOrderList(pageNum, status, "");
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                orderPresenter.getOrderList(pageNum, status, "");
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
