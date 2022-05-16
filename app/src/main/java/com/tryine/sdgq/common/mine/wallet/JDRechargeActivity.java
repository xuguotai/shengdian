package com.tryine.sdgq.common.mine.wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.adapter.JDRechargeAdapter;
import com.tryine.sdgq.common.mine.bean.AliPayResult;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.bean.WechatBean;
import com.tryine.sdgq.common.mine.presenter.RechargPresenter;
import com.tryine.sdgq.common.mine.view.RechargeView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.RechargeAmountDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 金豆充值
 *
 * @author: zhangshuaijun
 * @time: 2021-11-19 16:38
 */
public class JDRechargeActivity extends BaseActivity implements RechargeView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.rv_datalist)
    RecyclerView rv_datalist;

    @BindView(R.id.rb_wx)
    RadioButton rb_wx;
    @BindView(R.id.rb_zfb)
    RadioButton rb_zfb;

    JDRechargeAdapter jdRechargeAdapter;
    List<String> datas = new ArrayList<>();
    int selectedTabPosition = 0;

    RechargPresenter rechargPresenter;

    IWXAPI api;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, JDRechargeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge_jd;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("SD金豆充值");

        rechargPresenter = new RechargPresenter(this);
        rechargPresenter.attachView(this);

        api = WXAPIFactory.createWXAPI(this, "", true);

        initViews();
    }


    private void initViews() {
        datas.add("10");
        datas.add("50");
        datas.add("100");
        datas.add("500");
        datas.add("1000");

        datas.add("其他金额");
        jdRechargeAdapter = new JDRechargeAdapter(this, datas);

        rv_datalist.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv_datalist.setAdapter(jdRechargeAdapter);
        jdRechargeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                jdRechargeAdapter.setselectedTabPosition(position);
                selectedTabPosition = position;
                if (position == datas.size() - 1) {
                    RechargeAmountDialog rechargeAmountDialog = new RechargeAmountDialog(mContext, R.style.dialog_center);
                    rechargeAmountDialog.show();
                    rechargeAmountDialog.setmOnTextSendListener(new RechargeAmountDialog.OnTextSendListener() {
                        @Override
                        public void onMoney(String money) {
                            datas.remove(datas.size() - 1);
                            datas.add(money);
                            jdRechargeAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void dismiss() {

                        }
                    });
                }
            }
        });
    }


    @OnClick({R.id.iv_black, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                //充值
                if ("其他金额".equals(datas.get(selectedTabPosition))) {
                    ToastUtil.toastLongMessage("请选择充值金额");
                    return;
                }
                progressDialog.show();
                rechargPresenter.recharge(datas.get(selectedTabPosition), Integer.parseInt(datas.get(selectedTabPosition)) * 10 + "");


                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        rechargPresenter.getUserbean();
    }

    @Override
    public void onRechargeSuccess(String param, int payType) {
        try {
            progressDialog.dismiss();
            JSONObject obj = new JSONObject(param);
            if (payType == 1) {
                if ("200".equals(obj.optString("code"))) {
                    final Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(JDRechargeActivity.this);
                            Map<String, String> result = alipay.payV2(obj.optString("result"), true);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            } else if (payType == 2) {//微信
                WechatBean resultBean = new Gson().fromJson(param, WechatBean.class);
                //唤起微信支付
                PayReq request = new PayReq();
                request.appId = resultBean.getResult().getAppid();
                request.partnerId = resultBean.getResult().getPartnerid();
                request.prepayId = resultBean.getResult().getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = resultBean.getResult().getNoncestr();
                request.timeStamp = resultBean.getResult().getTimestamp();
                request.sign = resultBean.getResult().getSign();
                api.sendReq(request);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOrderSuccess(String orderNo) {
        if (rb_zfb.isChecked()) {
            rechargPresenter.recharge1(orderNo, "ALI", SPUtils.getString(Parameter.USER_ID), 1);
            return;
        }
        if (rb_wx.isChecked()) {
            if (!api.isWXAppInstalled()) {
                ToastUtils.showShort("未安装微信客户端");
            } else {
                rechargPresenter.recharge1(orderNo, "WXAPP", SPUtils.getString(Parameter.USER_ID), 2);
            }
            return;
        }
    }

    @Override
    public void onGetUserbeanSuccess(int goldenBean, int silverBean) {
        tv_balance.setText(goldenBean + "");

    }

    @Override
    public void onGetUserWalletSuccess(int goldenBean, int goldenBeanobtain, int goldenBeanconp, int silverBean, int silverBeanbtain, int silverBeanconp) {

    }

    @Override
    public void onGetWalletListSuccess(List<PayRecordBean> payRecordBeanList, int pages) {

    }

    @Override
    public void onGetProportionSuccess(String teaBean, String realStatus, String miniWithdraw, String serviceCharge, String proportion) {

    }

    @Override
    public void onWithdrawSuccess() {

    }

    @Override
    public void onGetProtocolSuccess(String agreement) {

    }

    @Override
    public void onFailed(String message) {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage(message);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.toastLongMessage("充值成功");
                        rechargPresenter.getUserbean();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
