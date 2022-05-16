package com.tryine.sdgq.common.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.activity.ShopPositionActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.adapter.OrderDetailGoodsItemAdapter;
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
 * 订单详情
 *
 * @author: zhangshuaijun
 * @time: 2021-12-03 15:12
 */
public class OrderDetailActivity extends BaseActivity implements OrderView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    @BindView(R.id.ll_pay)
    LinearLayout ll_pay;
    @BindView(R.id.countdown_text_view)
    CountDownTextView countdown_text_view;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_contact)
    TextView tv_contact;
    @BindView(R.id.tv_addrDes)
    TextView tv_addrDes;

    @BindView(R.id.tv_totalPayPrice)
    TextView tv_totalPayPrice;
    @BindView(R.id.tv_totalPayPrice1)
    TextView tv_totalPayPrice1;
    @BindView(R.id.tv_orderNo)
    TextView tv_orderNo; //订单编号
    @BindView(R.id.tv_createTime)
    TextView tv_createTime; //下单时间

    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_tk)
    TextView tv_tk;
    @BindView(R.id.tv_pay)
    TextView tv_pay;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.tv_pj)
    TextView tv_pj;

    @BindView(R.id.ll_fs)
    LinearLayout ll_fs;
    @BindView(R.id.tv_addrDes1)
    TextView tv_addrDes1;
    @BindView(R.id.tv_psfs)
    TextView tv_psfs;

    CampusBean campusBean;


    OrderDetailGoodsItemAdapter orderDetailGoodsItemAdapter;

    OrderPresenter orderPresenter;

    OrderListBean orderListBean;

    String tkPrice;

    UserBean userBean;

    public static void start(Context context, OrderListBean orderListBean) {
        Intent intent = new Intent();
        intent.setClass(context, OrderDetailActivity.class);
        intent.putExtra("orderListBean", orderListBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_orderdetail;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("订单详情");
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        orderListBean = (OrderListBean) getIntent().getSerializableExtra("orderListBean");
        orderPresenter = new OrderPresenter(mContext);
        orderPresenter.attachView(this);
        orderPresenter.getFicationList();

        initViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
    }


    protected void initViews() {

        ll_pay.setVisibility(View.GONE);
        tv_cancel.setVisibility(View.GONE);
        tv_tk.setVisibility(View.GONE);
        tv_pay.setVisibility(View.GONE);
        tv_confirm.setVisibility(View.GONE);
        tv_pj.setVisibility(View.GONE);


        //单状态 (0:订单关闭；1：待付款；2:订单支付超时；3:已取消 4：待评价；5:已完成 ；6:退款中；7:退款完成 8待自提)
        if ("0".equals(orderListBean.getStatus())) {

        } else if ("1".equals(orderListBean.getStatus())) {
            ll_btn.setVisibility(View.VISIBLE);
            ll_pay.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.VISIBLE);
            tv_pay.setVisibility(View.VISIBLE);

            float f = Float.valueOf(DateTimeHelper.getSurplusTime(orderListBean.getAutoCancelTime()));
            countdown_text_view.setMaxTime(Math.round(f));
            countdown_text_view.setmFinishText("");
            countdown_text_view.startCountDown();
            countdown_text_view.setishour(true);
        } else if ("2".equals(orderListBean.getStatus())) {

        } else if ("3".equals(orderListBean.getStatus())) {

        } else if ("4".equals(orderListBean.getStatus())) {
            ll_btn.setVisibility(View.VISIBLE);
            tv_pj.setVisibility(View.VISIBLE);

        } else if ("5".equals(orderListBean.getStatus())) {

        } else if ("6".equals(orderListBean.getStatus())) {

        } else if ("7".equals(orderListBean.getStatus())) {

        } else if ("8".equals(orderListBean.getStatus())) {
            ll_btn.setVisibility(View.VISIBLE);
            tv_tk.setVisibility(View.VISIBLE);
            tv_confirm.setVisibility(View.VISIBLE);

        }

        if(!TextUtils.isEmpty(orderListBean.getReceiptAddress())){
            ll_fs.setVisibility(View.GONE);
            tv_addrDes1.setVisibility(View.VISIBLE);
            tv_addrDes1.setText(orderListBean.getReceiptAddress());
            tv_psfs.setText("邮寄");
        }else{
            ll_fs.setVisibility(View.VISIBLE);
            tv_addrDes1.setVisibility(View.GONE);
            tv_psfs.setText("自提");
        }



        double total_price = 0;
        double total_price1 = 0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (OrderGoodsBean goodsBean : orderListBean.getPiratesOrderDetailListVoList()) {
            if (goodsBean.getBeanType() == 0) {
                total_price = total_price + (Double.parseDouble(goodsBean.getUnitPrice()) * goodsBean.getCount());
            } else {
                total_price1 = total_price1 + (Double.parseDouble(goodsBean.getUnitPrice()) * goodsBean.getCount());
            }
        }

        if (total_price > 0 && total_price1 > 0) {
            tv_totalPayPrice.setText("实付：" + decimalFormat.format(total_price) + " SD金豆 + " + decimalFormat.format(total_price1) + " SD银豆");
            tv_totalPayPrice1.setText(decimalFormat.format(total_price) + " SD金豆 + " + decimalFormat.format(total_price1) + " SD银豆");
            tkPrice = decimalFormat.format(total_price) + " SD金豆 + " + decimalFormat.format(total_price1) + " SD银豆";
        } else if (total_price > 0) {
            tv_totalPayPrice.setText("实付：" + decimalFormat.format(total_price) + " SD金豆");
            tv_totalPayPrice1.setText(decimalFormat.format(total_price) + " SD金豆");
            tkPrice = decimalFormat.format(total_price) + " SD金豆";
        } else if (total_price1 > 0) {
            tv_totalPayPrice.setText("实付：" + decimalFormat.format(total_price1) + " SD银豆");
            tv_totalPayPrice1.setText(decimalFormat.format(total_price1) + " SD银豆");
            tkPrice = decimalFormat.format(total_price1) + " SD银豆";
        }

        tv_orderNo.setText("订单编号：" + orderListBean.getOrderNo());
        tv_createTime.setText("下单时间：" + orderListBean.getCreateTime());


        orderDetailGoodsItemAdapter = new OrderDetailGoodsItemAdapter(this, orderListBean.getPiratesOrderDetailListVoList());
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(orderDetailGoodsItemAdapter);


    }


    @OnClick({R.id.iv_black, R.id.tv_tk, R.id.tv_pay, R.id.tv_cancel, R.id.tv_pj, R.id.tv_confirm, R.id.tv_fuzhi, R.id.tv_dh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_tk:
                ApplyRefundActivity.start(OrderDetailActivity.this, orderListBean, tkPrice);
                break;
            case R.id.tv_pay:
                payOrder(orderListBean.getOrderNo());
                break;
            case R.id.tv_cancel:
                cancelOrder(orderListBean.getOrderNo());
                break;
            case R.id.tv_pj:
                ReleaseCommentActivity.start(OrderDetailActivity.this, orderListBean);
                break;
            case R.id.tv_confirm:
                confirmOrder(orderListBean.getOrderNo());
                break;
            case R.id.tv_fuzhi:
                copyToClipboard(orderListBean.getOrderNo());
                break;
            case R.id.tv_dh:
                if (null != campusBean) {
                    ShopPositionActivity.start(mContext, campusBean.getLonLatTencent(), campusBean.getAddrDes());
                }
                break;
        }
    }


    @Override
    public void onGetOrderBeanListSuccess(List<OrderListBean> orderListBeanList, int pages) {

    }

    @Override
    public void onGetOrderBeanSuccess(OrderListBean orderListBean) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {
        if (null != orderListBean) {
            for (CampusBean campusBean : campusBeanList) {
                if (campusBean.getId().equals(orderListBean.getReceiptId())) {
                    this.campusBean = campusBean;
                    tv_name.setText(campusBean.getName());
                    tv_contact.setText(campusBean.getContact());
                    tv_addrDes.setText(campusBean.getAddrDes());
                }

            }
        }


    }

    @Override
    public void onCancelorderSuccess() {
        ToastUtil.toastLongMessage("操作成功");
        finish();
    }

    @Override
    public void onDetermineSuccess() {
        ToastUtil.toastLongMessage("操作成功");
        finish();
    }

    @Override
    public void onRefundSuccess() {
        ToastUtil.toastLongMessage("操作成功");
        finish();
    }

    @Override
    public void onCommentSuccess(int i) {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {

    }

    @Override
    public void onPayOrderSuccess() {
        ToastUtil.toastLongMessage("操作成功");
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    orderListBean.setStatus("5");
                    initViews();
                    break;
                case 2:
                    orderListBean.setStatus("6");
                    initViews();
                    break;

            }
        }
    }

}
