package com.tryine.sdgq.common.mine.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.presenter.RechargPresenter;
import com.tryine.sdgq.common.mine.view.RechargeView;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.VerifyPayPwdDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付宝提现
 */
public class AlipayActivity extends BaseActivity implements RechargeView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_account)
    EditText et_account;


    String withdrawType = "";
    String amount = "";
    String serviceCharge = "";

    RechargPresenter rechargPresenter;

    public static void start(Context context, String amount, String serviceCharge) {
        Intent intent = new Intent();
        intent.setClass(context, AlipayActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("serviceCharge", serviceCharge);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_alipay_withdraw;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("支付宝提现");
        rechargPresenter = new RechargPresenter(this);
        rechargPresenter.attachView(this);
        withdrawType = getIntent().getStringExtra("withdrawType");
        amount = getIntent().getStringExtra("amount");
        serviceCharge = getIntent().getStringExtra("serviceCharge");
        if (TextUtils.isEmpty(serviceCharge)) {
            ToastUtil.toastShortMessage("参数异常");
            finish();
            return;
        }


    }


    @OnClick({R.id.iv_black, R.id.tv_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                //提交
                if ("".equals(et_name.getText().toString())) {
                    ToastUtil.toastShortMessage("请输入支付宝姓名");
                    return;
                }

                if ("".equals(et_account.getText().toString())) {
                    ToastUtil.toastShortMessage("请输入支付宝账号");
                    return;
                }

                PromptDialog dialog = new PromptDialog(this, 0, "请核对提现的支付宝账号(" + getTextStr(et_account) + ")", "",
                        "确定", "取消");
                dialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public void insure() {


                        VerifyPayPwdDialog verifyPayPwdDialog = new VerifyPayPwdDialog(mContext, R.style.dialog_center);
                        verifyPayPwdDialog.show();
                        verifyPayPwdDialog.setmOnTextSendListener(new VerifyPayPwdDialog.OnTextSendListener() {
                            @Override
                            public void onbackPwd(String password) {
                                rechargPresenter.withdrawALI("ALI", amount, serviceCharge + "", password, et_account.getText().toString(), et_name.getText().toString());
                                progressDialog.show();
                            }

                            @Override
                            public void dismiss() {

                            }
                        });

//                        rechargPresenter.withdraw("ALI", amount, serviceCharge + "", "","123456");
                    }

                    @Override
                    public void cancel() {

                    }
                });
                dialog.show();
                break;
        }
    }


    @Override
    public void onRechargeSuccess(String param, int payType) {

    }

    @Override
    public void onCreateOrderSuccess(String orderNo) {

    }

    @Override
    public void onGetUserbeanSuccess(int goldenBean, int silverBean) {

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
        ToastUtil.toastLongMessage("提现成功请等待审核!");
        progressDialog.dismiss();
        finish();
    }

    @Override
    public void onGetProtocolSuccess(String agreement) {

    }

    @Override
    public void onFailed(String message) {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage(message);

    }
}
